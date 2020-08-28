package unipi.iot;

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
					Integer lowerThreshold = 10, upperThreshold = 30, index;
	
					if (jo.containsKey("temperature")) {
						value = jo.get("temperature").toString();
						Integer numericValue = Integer.parseInt(value.trim());
	
						if (numericValue < lowerThreshold) {
							index = MainApp.thermostats.indexOf(sensor);
							Cooling cooler = MainApp.coolers.get(index);
							Boolean state = cooler.getState();
							if (!state)
								cooler.setState(true);
						}
	
						if (numericValue > upperThreshold) {
							index = MainApp.thermostats.indexOf(sensor);
							Cooling cooler = MainApp.coolers.get(index);
							Boolean state = cooler.getState();
							if (!state)
								cooler.setState(false);
						}
	
					} else {
						System.out.println("It wasn't possible to get the value.");
						return;
					}
					System.out.println("TODO: add");
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


			public HumidityResource getHumidityResource() {
				return humidityResource;
			}

			public void setHumidityResource(HumidityResource humidityResource) {
				this.humidityResource = humidityResource;
			};
			
		}