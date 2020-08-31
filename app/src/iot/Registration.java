package iot;

import java.net.InetAddress;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class Registration extends CoapResource {
	public Registration(String name) {
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

			if (path.contains("thermostat")) {
				Thermostat thermostat = new Thermostat(path, inetAddress.getHostAddress());
				if (!MainApp.thermostats.contains(thermostat)) {
					MainApp.thermostats.add(thermostat);
					observe(thermostat);
				}
			} else if (path.contains("cooling")) {
				Cooling cooler = new Cooling(path, inetAddress.getHostAddress());
				if (!MainApp.coolers.contains(cooler))
					MainApp.coolers.add(cooler);
			}
		}
	}
	private static void observe(Thermostat sensor) {
		MainApp.coapObserverTemperature.add(new TemperatureObserver(sensor));
		MainApp.coapObserverTemperature.get(MainApp.coapObserverTemperature.size() - 1).startObserving();
		//MainApp.coapObserverTemperature.get(MainApp.coapObserverTemperature.get(MainApp.coapObserverTemperature.size() - 1).startObserving();
	}
}