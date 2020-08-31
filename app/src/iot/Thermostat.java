package iot;

import java.util.ArrayList;

public class Thermostat extends Sensor{
	
	private ArrayList<String> temperatureValues = new ArrayList<String>();
	private Integer MAXCAPACITY = 10;

	public Thermostat(String path, String address){
		super(path, address);
	}
	public String getURI() {
		return super.getURI();
	}


	public String getLastValue(){
		return temperatureValues.get(temperatureValues.size() - 1);
	}


	public void setLastValue(String value){
		temperatureValues.add(value);
		if(temperatureValues.size() > this.MAXCAPACITY)
			temperatureValues.remove(0);
	}

	public ArrayList<String> getTemperature(){
		return this.temperatureValues;
	}

	public void setTemperature(ArrayList<String> temperatureValues){
		this.temperatureValues = temperatureValues;
	}

}