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
public class Cooling extends Node{
    private Boolean isActive;
    
    public Cooling(String nodePath, String nodeAddress){
        super(nodePath, nodeAddress);
        this.isActive=false;
    }
    
    public Boolean checkActive(){ //checks whether dehumidifier already working
        return this.isActive;
    }
    
    public void setActive(Boolean isActive){
        this.isActive=isActive;
    }
    
}
