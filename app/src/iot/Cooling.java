package iot;

public class Cooling extends Sensor{
		private Boolean status = false;

		public Cooling(String path, String address) {
			super(path, address);
		}

		public Boolean checkActive(){
			return this.status;
		}

		public void setActive(Boolean status){
			this.status = status;
		}
	}