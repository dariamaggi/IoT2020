package unipi.iot;

public class Sensor {
	private String path;
	private String address;

	public Sensor(String path, String address) {
		this.path = path;
		this.address = address;
	}
	
	public String getURI() {
		return "coap://[" + this.address + "]:5683/" + this.path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
}