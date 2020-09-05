package unipi.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class MainApp {

	public static ArrayList<CoapObserverClient> coapObserverClients = new ArrayList<CoapObserverClient>();
	public static ArrayList<Thermostat> thermostats = new ArrayList<Thermostat>();
	public static ArrayList<Cooling> coolers = new ArrayList<Cooling>();

	public static void main(String[] args) throws IOException, InterruptedException {

		runServer();
		showOperations();

		while (true) {
			try {
				Integer selectedOperation = insertInputLine();
				Integer index, gap = null;

				switch (selectedOperation) {
				case 0:
					showResources();
					break;
				case 1:
					if ((index = getNodeFromId()) != null)
						changeIrrigationState("ON", coolers.get(index), true);
					break;
				case 2:
					if ((index = getNodeFromId()) != null)
						changeIrrigationState("OFF", coolers.get(index), false);
					break;
				case 3:
					showResourcesInformation();
					break;
				case 4:
					if ((index = getNodeFromId()) != null) {
						String value = thermostats.get(index).getLastTemperatureValue();
						System.out.println("Last temperature value registered: " + value);
					}
					break;
				case 5:
					if ((index = getNodeFromId()) != null)
						gap = thermostats.get(index).getTemperatureGap();
						if (gap!= null && gap > 2)
							System.out.println("Attention! Gap detected in zone SHOW ZONE");
							
					
					break;
				case 6:
					System.exit(0);
					break;
				default:
					showOperations();
					break;
				}

			} catch (Exception e) {
				System.out.println("Invalid input, here are the available commands\n");
				showOperations();
				e.printStackTrace();
			}
		}
	}

	public static void runServer() {
		new Thread() {
			public void run() {
				Server server = new Server();
				server.startServer();
			}
		}.start();
	}

	public static Integer getNodeFromId() {
		System.out.print("Insert the node id: ");
		Integer index = insertInputLine();
		System.out.println();
		if (index == -1)
			return null;
		if (thermostats.size() > index)
			return index;
		System.out.println("The selected node does not exists.");
		return null;
	}

	public static Integer insertInputLine() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String line = bufferedReader.readLine();
			Integer value = -1;
			if (isNumeric(line))
				value = Integer.parseInt(line);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null)
			return false;
		try {
			@SuppressWarnings("unused")
			Integer number = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static void showResources() {
		System.out.println("List of the Resources in the system:");
		for (int i = 0; i < thermostats.size(); i++) {
			Thermostat thermostat = thermostats.get(i);
			Cooling cooler = coolers.get(i);
			System.out.println(
					+i + "\tThermostat: " + thermostat.getAddress() + " " + thermostat.getPath());
			System.out.println(+i + "\tCooling System: " + cooler.getAddress() + " "
					+ cooler.getPath() + Boolean.toString(cooler.getState())+"\n");
		}
	}

	public static void showResourcesInformation() {
		System.out.println("Information about the resources: \n");
		for (int i = 0; i < thermostats.size(); ++i) {
			showSingleResourceInformation(i);
			System.out.println("\n -------------------------------------------- \n");
		}
	}

	public static void changeIrrigationState(String state, Cooling cooler, Boolean value) {
		CoapClient client = new CoapClient(cooler.getResourceURI());
		CoapResponse response = client.post("state=" + state, MediaTypeRegistry.TEXT_PLAIN);
		String code = response.getCode().toString();
		if (!code.startsWith("2")) {
			System.out.println("Error: " + code);
			return;
		}
		cooler.setState(value);
		System.out.println("Cooling system switched to: " + state);
	}

	

	public static void showSingleResourceInformation(Integer index) {
		Cooling cooler = coolers.get(index);
		String stateValue = cooler.getState() ? "ON" : "OFF";
		System.out.println(index + "\t" + cooler.getAddress() + " " + cooler.getPath()
				+ "\n\t\tState: " + stateValue + "\n");

		Thermostat thermostat = thermostats.get(index);
		System.out.println(index + "\t" + thermostat.getAddress() + " " + thermostat.getPath());
		ArrayList<String> list = thermostat.getTemperatureValues();
		for (int j = 0; j < list.size(); ++j)
			System.out.println("\t\tId: " + j + "\tValue: " + list.get(j));
	}

	public static void showOperations() {
		System.out.println("Available options:");
		System.out.println("0-> show resources");
		System.out.println("1-> start cooling system");
		System.out.println("2-> stop cooling");
		System.out.println("3-> nodes status");
		System.out.println("4-> show last registered temperature");
		System.out.println("5-> show mean the gap");
		System.out.println("6-> show zone");
		System.out.println("7-> exit");
	}
}
