
package it.unipi.iot2020;

import org.eclipse.californium.core.CoapServer;

public class Server extends CoapServer {

	public void startServer() {
		System.out.println("Server started!");
		this.add(new Registration("Registration"));
		this.start();
	}
}