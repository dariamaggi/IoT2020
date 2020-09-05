package unipi.iot;

import java.util.ArrayList;

public class Thermostat extends Resource {
	private ArrayList<String> temperatures = new ArrayList<String>();

	public Thermostat(String path, String address) {
		super(path, address);
	}

	public ArrayList<String> getTemperatureValues() {
		return this.temperatures;
	}

	public void setTemperatureValues(ArrayList<String> list) {
		int valuesLimit = 3;
		if (list.size() > valuesLimit)
			list.remove(0);
		this.temperatures = list;
	}
	public Integer getTemperatureGap() {
		if(temperatures.size()>2)
			return Integer.parseInt(this.temperatures.get(this.temperatures.size()-1))-Integer.parseInt(this.temperatures.get(this.temperatures.size()-2));
		return null;
	}  
	public String getLastTemperatureValue() {
		if (!temperatures.isEmpty())
			return this.temperatures.get(temperatures.size()-1);
		return "NA";
	}

}