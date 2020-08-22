/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;

/**
 *
 * @author dariamargheritamaggi
 */
import java.util.ArrayList;


import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class CoapObserverHumidity {//extends CoapClient 
    private HumiditySensor sensor;
    CoapObserveRelation coapObserveRelation;
    
    public CoapObserverHumidity(HumiditySensor humidityResource) {
		//super(sensor.getResourceURI());   //gets the URI
		this.sensor = humidityResource;
	}
    public void startObserving() {
		coapObserveRelation = this.observe(new CoapHandler () {
			public void onLoad(CoapResponse response) {
				try {
					String value;
					JSONObject jo = (JSONObject) JSONValue.parseWithException(response.getResponseText());
					int index;
                                         int humidityThreshold=50;
	
					if (jo.containsKey("humidity")) {
						value = jo.get("humidity").toString();
						Integer numericValue = Integer.parseInt(value.trim());
	
						if (numericValue > humidityThreshold) {
							index = MainApp.humiditySensors.indexOf(sensor);
							Dehumidifier dehumidifierInst = MainApp.dehumidifiers.get(index);
							
							if (!dehumidifierInst.checkActive()) 
								dehumidifierInst.setActive(true);							
						}
	
					} else {
						System.out.println("Humidity value not found.");
						return;
					}
	
					ArrayList<String> resourceValues = sensor.getHumidity();
					resourceValues.add(value);
					MainApp.humiditySensors.get(MainApp.humiditySensors.indexOf(sensor))
							.setHumidityValues(resourceValues);
	
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
	
			public void onError() {
				System.out.println("Observing Error Detected.");
			}
		});
	}
    public HumiditySensor getHumidityResource() {
		return sensor;
	}

    public void setHumidityResource(HumiditySensor humiditySensor) {
		this.sensor = humiditySensor;
	};
}
