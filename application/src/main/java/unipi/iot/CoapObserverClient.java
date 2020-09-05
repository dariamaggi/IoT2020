package unipi.iot;

import java.util.ArrayList;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class CoapObserverClient extends CoapClient {
	private Thermostat thermostat;
	CoapObserveRelation coapObserveRelation;

	public CoapObserverClient(Thermostat thermostat) {
		super(thermostat.getResourceURI());
		this.thermostat = thermostat;
	}

	public void startObserving() {
		coapObserveRelation = this.observe(new CoapHandler () {
			public void onLoad(CoapResponse response) {
				try {
					String value;
					JSONObject jo = (JSONObject) JSONValue.parseWithException(response.getResponseText());
					Integer lowerThreshold = 10, upperThreshold = 30, index;
	
					if (jo.containsKey("temperature")) {
						value = jo.get("temperature").toString();
						Integer numericValue = Integer.parseInt(value.trim());
	
						if (numericValue < lowerThreshold) {
							index = MainApp.thermostats.indexOf(thermostat);
							Cooling cooler = MainApp.coolers.get(index);
							Boolean state = cooler.getState();
							if (!state)
								cooler.setState(false);
						}
	
						if (numericValue > upperThreshold) {
							index = MainApp.thermostats.indexOf(thermostat);
							Cooling cooler = MainApp.coolers.get(index);
							Boolean state = cooler.getState();
							if (state) 
								cooler.setState(true);							
						}
	
					} else {
						System.out.println("Humidity value not found.");
						return;
					}
	
					ArrayList<String> resourceValues = thermostat.getTemperatureValues();
					resourceValues.add(value);
					MainApp.thermostats.get(MainApp.thermostats.indexOf(thermostat))
							.setTemperatureValues(resourceValues);
	
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
	
			public void onError() {
				System.out.println("Error in observing.");
			}
		});
	}

	public Thermostat getTemperatureResource() {
		return thermostat;
	}

	public void setHumidityResource(Thermostat thermostat) {
		this.thermostat = thermostat;
	};
}
