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
					Double upper = 23.0, lower = 25.0;
					if (jo.containsKey("temperature")) {
						value = jo.get("temperature").toString();
						Double sensedTemperature = Double.parseDouble(value.trim());
						
						if (this.sensor.size() > 1){
							if (sensedTemperature - Double.parseDouble(this.sensor.getLastValue() > 0.25 || sensedTemperature > upper){
								Cooling cooling = MainApp.cooler.get(MainApp.thermostats.indexOf(sensor));
								if(!cooling.checkActive())
									cooling.setActive(true):
								else
									System.out.println("Already in operation!");							
							}else if (sensedTemperature  < lower){
								Cooling cooling = MainApp.cooler.get(MainApp.thermostats.indexOf(sensor));
								if(cooling.checkActive())
									cooling.setActive(false):
								else
									System.out.println("Already idle!");
							}
							MainApp.thermostats.get(MainApp.thermostats.indexOf(sensor)).setLastValues(value);

					
					} else {
						System.out.println("It wasn't possible to get the value.");
						return;
					}
					
	
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