package com.nwpu.heartwings.bean;

public class TimePair {

	private int hour;
	private int minute;
	
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public TimePair(int hour, int minute) {
		super();
		this.hour = hour;
		this.minute = minute;
	}
	
	
	
}
