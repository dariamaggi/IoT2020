package unipi.iot;


public class Thermostat extends Sensor{
	
	private ArrayList<String> temperatureValues = new ArrayList<String>();

	public Thermostat(String path, String address){
		super(path, address);
	}

	public String getLastValue(){
		return temperatureValues[temperatureValues.size() - 1];
	}

	public ArrayList<String> getTemperature(){
		return this.temperatureValues;
	}

	public void setTemperature(ArrayList<String> temperatureValues){
		this.temperatureValues = temperatureValues;
	}

	public void setLastValues(String value){
		temperatureValues.add(value);
	}

}