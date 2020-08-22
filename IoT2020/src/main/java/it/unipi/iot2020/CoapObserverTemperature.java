/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;

import java.util.ArrayList;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
/**
 *
 * @author dariamargheritamaggi
 */
public class CoapObserverTemperature extends CoapClient {
    private TemperatureSensor sensor;
    CoapObserveRelation coapObserveRelation;
    
    public CoapObserverTemperature(TemperatureSensor sensor) {
		super(humidityResource.getNodeURI());
		this.sensor= sensor;
	}
    public void startObserving() {
		coapObserveRelation = this.observe(new CoapHandler () {
			public void onLoad(CoapResponse response) {
				try {
					String value;
					JSONObject jo = (JSONObject) JSONValue.parseWithException(response.getResponseText());
					Integer index;
	
					if (jo.containsKey("temperature")) {
						value = jo.get("temperature").toString();
						Double numericValue = Double.parseDouble(value.trim());
	
						if (sensor.gapDetected(numericValue)) {
							index = MainApp.temperatureSensors.indexOf(sensor);
							Cooling coolingActuator = MainApp.coolers.get(index);
							if (!coolingActuator.checkActive())
								coolingActuator.setActive(true);
						}
	
						
	
					} else {
						System.out.println("Temperature value not found.");
						return;
					}
	
					ArrayList<String> resourceValues = sensor.getTemperature();
					resourceValues.add(value);
                    resourceValues.remove(0); //get out oldest value
					MainApp.temperatureSensors.get(MainApp.temperatureSensors.indexOf(sensor))
							.setTemperatureValues(resourceValues);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
	
			public void onError() {
				System.out.println("Observing Error Detected.");
			}
		});
	}
    public TemperatureSensor getHumidityResource() {
		return sensor;
	}

    public void setTemperatureSensor(TemperatureSensor temperatureSensor) {
		this.sensor = temperatureSensor;
	};


}
