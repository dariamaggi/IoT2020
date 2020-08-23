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
public class Node {
    private String nodePath;
	private String nodeAddress;

	public Node(String nodePath, String nodeAddress) {
		this.nodePath = nodePath;
		this.nodeAddress = nodeAddress;
	}

	public String getPath() {
		return nodePath;
	}

	public void setPath(String path) {
		this.nodePath = path;
	}

	public String getAddress() {
		return nodeAddress;
	}

	public void setAddress(String address) {
		this.nodeAddress = address;
	}

	public String getNodeURI() {
		return "coap://[" + this.nodeAddress + "]:5683/" + this.nodePath;
	}
}
