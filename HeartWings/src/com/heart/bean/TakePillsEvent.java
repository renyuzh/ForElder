package com.heart.bean;

import java.util.List;

public class TakePillsEvent {

	private List<Integer> theTimes_hour;
	private List<Integer> theTimes_minute;
	private int id;
	private String content;
	
	public List<Integer> getTheTimes_hour() {
		return theTimes_hour;
	}
	public void setTheTimes_hour(List<Integer> theTimes_hour) {
		this.theTimes_hour = theTimes_hour;
	}
	public List<Integer> getTheTimes_minute() {
		return theTimes_minute;
	}
	public void setTheTimes_minute(List<Integer> theTimes_minute) {
		this.theTimes_minute = theTimes_minute;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
	
}
