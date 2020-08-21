/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;

import java.util.ArrayList;

/**
 *
 * @author dariamargheritamaggi
 */
public class TemperatureSensor extends Node{
    	private ArrayList<String> temperature = new ArrayList<String>(); 
        //temperature[0]is older, temperature[1] is latest
        
        public TemperatureSensor(String nodePath, String nodeAddress){
            super(nodePath, nodeAddress); //when it gets created it need to check if longer than 2
        }
        
        public ArrayList<String> getTemperature(){
            return this.temperature;
        }
        
        public void addTemperatureValue(String currentTemperature){
            temperature.add(currentTemperature);
            if (temperature.size()>2)
                    temperature.remove(0);
        }
        
        public Boolean gapDetected(Double newestValue){
          
           return (newestValue-Double.parseDouble(temperature.get(1))>0.25)?true:false; 
        }
        public void setTemperatureValues(ArrayList<String> list) {
		
		this.temperature = list;
	}
}
