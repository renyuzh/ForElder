package org.heartwings.network;

public class HeartPositionMsg extends HeartMessage {
	private double longitude;
	private double latitude;
	private String arear;
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getArear() {
		return arear;
	}
	public void setArear(String arear) {
		this.arear = arear;
	}
	public HeartPositionMsg(double longitude, double latitude, String arear) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.arear = arear;
	}
}
