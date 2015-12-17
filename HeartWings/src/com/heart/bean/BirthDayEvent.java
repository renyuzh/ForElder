package com.heart.bean;


public class BirthDayEvent {

	private int month;
	private int day;
	private String content;
	
	private int id;
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public BirthDayEvent(int month, int day, String content) {
		super();
		this.month = month;
		this.day = day;
		this.content = content;
	}
	public BirthDayEvent() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
