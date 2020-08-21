/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;

/**
 *
 * @author dariamargheritamaggi
 */
public class Dehumidifier extends Node {
    private Boolean isActive;
    
    public Dehumidifier(String nodePath, String nodeAddress){
        super(nodePath, nodeAddress);
        this.isActive=false; //by default it is OFF
    }
    public Boolean checkActive(){ //checks whether dehumidifier already working
        return this.isActive;
    }
    public void setActive(Boolean isActive){
        this.isActive=isActive;
    }
}
