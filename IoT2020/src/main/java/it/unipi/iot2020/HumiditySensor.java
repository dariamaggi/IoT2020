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
public class HumiditySensor extends Node{
    private ArrayList<String> humidity; //only need the latest value?
    
    public HumiditySensor(String nodePath, String nodeAddress) {
		super(nodePath, nodeAddress);
	}
    public ArrayList<String> getHumidity(){
        return this.humidity;
    }
    
    public void setHumidityValues(ArrayList<String> list) {
		int valuesLimit = 10;
		if (list.size() > valuesLimit)
			list.remove(0);
		this.humidity = list;
	}
}
