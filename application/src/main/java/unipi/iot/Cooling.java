package unipi.iot;

public class Cooling extends Resource {
	private Boolean state;

	public Cooling(String path, String address) {
		super(path, address);
		state = false;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}
}
