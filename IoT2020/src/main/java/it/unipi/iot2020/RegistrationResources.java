/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;

import java.net.InetAddress;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.server.resources.CoapExchange;


/**
 *
 * @author dariamargheritamaggi
 */
public class RegistrationResources extends CoapResource { 
    public RegistrationResources(String name) {
		super(name);
	}
    
    public void handleGET(CoapExchange exchange) {
		exchange.accept();

		InetAddress inetAddress = exchange.getSourceAddress();
		CoapClient client = new CoapClient("coap://[" + inetAddress.getHostAddress() + "]:5683/.well-known/core");
		CoapResponse response = client.get();
		
		String code = response.getCode().toString();
		if (!code.startsWith("2")) {
			System.out.println("Error: " + code);
			return;
		}

		String responseText = response.getResponseText();
		Integer startIndex = 0, endIndex;

		while (true) {
			startIndex = responseText.indexOf("</");
			if (startIndex == -1)
				break;
			endIndex = responseText.indexOf(">");
			String path = responseText.substring(startIndex + 2, endIndex);
			responseText = responseText.substring(endIndex + 1);

			if (path.contains("sens_humidity")) {
				HumiditySensor humiditySensor = new HumiditySensor(path, inetAddress.getHostAddress());
				if (!MainApp.humiditySensors.contains(humiditySensor)) { //adds
					MainApp.humiditySensors.add(humiditySensor);
					observeHumidity(humiditySensor);
				}
			} else if (path.contains("act_humidity")) {
				Dehumidifier dehumifierActuator = new Dehumidifier(path, inetAddress.getHostAddress());
				if (!MainApp.dehumidifiers.contains(dehumifierActuator))
					MainApp.dehumidifiers.add(dehumifierActuator);
                                
			} else if (path.contains("sens_temperature")){
                TemperatureSensor temperatureSensor= new TemperatureSensor(path, inetAddress.getHostAddress());
                if(!MainApp.temperatureSensors.contains(temperatureSensor)){ //adds
					MainApp.temperatureSensors.add(temperatureSensor);
					observeTemperature(temperatureSensor);
					}
				}
			} else if (path.contains("act_temperature")){
                Cooling coolingActuator= new Cooling(path, inetAddress.getHostAddress());
                if(!MainApp.coolers.contains(coolingActuator)){ //adds
					MainApp.coolers.add(coolingActuator);
                                
   
		}
	}
    
    
    }
    private static void observeHumidity(HumiditySensor humiditySensor) {
		MainApp.coapObserverClients.add(new CoapObserverHumidity(humiditySensor));
		MainApp.coapObserverClients.get(MainApp.coapObserverClients.size() - 1).startObserving();
	}
    private static void observeTemperature(TemperatureSensor temperatureSensor) {
		MainApp.coapObserverClients.add(new CoapObserverTemperature(temperatureSensor));
		MainApp.coapObserverClients.get(MainApp.coapObserverClients.size() - 1).startObserving();
	}
}
