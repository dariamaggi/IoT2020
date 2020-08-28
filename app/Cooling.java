package unipi.iot;

//cooling actuator

public class Cooling extends Sensor{
	private Boolean status = false;

	public Sensor(String path, String address) {
		super(path, address);
	}

	public Boolean checkActive(){
		return this.status;
	}

	public void setStatus(Boolean status){
		this.status = status;
	}
}