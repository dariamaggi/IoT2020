/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipi.iot2020;
import org.eclipse.californium.core.CoapServer;

/**
 *
 * @author dariamargheritamaggi
 */

public class ServerResource extends CoapServer { 

	public void startServer() {
		System.out.println("Server started");
		this.add(new RegistrationResources("registration"));
		this.start();
	}
}