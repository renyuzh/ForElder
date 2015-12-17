package org.heartwings.network;

public class EmergencyPhone {
	private String phone;
	private String name;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public EmergencyPhone() {
	}
	public EmergencyPhone(String phone, String name) {
		super();
		this.phone = phone;
		this.name = name;
	}
}
