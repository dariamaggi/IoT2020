package package it.unipi.iot2020;

import java.util.ArrayList;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class TemperatureObserver extends CoapClient {
	private Thermostat sensor;
	private CoapObserveRelation relation;

	public TemperatureObserver(Thermostat sensor) {
		super(sensor.getURI());
		this.sensor = sensor;
	}

	public void startObserving() {
		coapObserveRelation = this.observe(new CoapHandler () {
			public void onLoad(CoapResponse reply) {
				try {
					String value;
					JSONObject jo = (JSONObject) JSONValue.parseWithException(reply.getResponseText());
					Integer index;
					Double upper = 23.0;
					if (jo.containsKey("temperature")) {
						value = jo.get("temperature").toString();
						Double sensedTemperature = Double.parseDouble(value.trim());
						
						if (sensedTemperature - Double.parseDouble(this.sensor.getLastValue() > 0.25){
							//TODO
						}

					
					} else {
						System.out.println("It wasn't possible to get the value.");
						return;
					}
					MainApp.thermostats.get(MainApp.thermostats.indexOf(sensor)).setLastValues(value);
					
	
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
	
			public void onError() {
				System.out.println("Error in the observation of sensor: "+sensor.getAddress());
			}
		});
	}
}