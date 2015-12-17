package com.heart.bean;

import java.util.List;

public class PeriodEvent {

	private short hour;
	private short minute;
	private List<Integer> weekday;
	private String content;
	private long id;

	public short getHour() {
		return hour;
	}

	public void setHour(short hour) {
		this.hour = hour;
	}

	public short getMinute() {
		return minute;
	}

	public void setMinute(short minute) {
		this.minute = minute;
	}

	public List<Integer> getWeekday() {
		return weekday;
	}

	public void setWeekday(List<Integer> weekday) {
		this.weekday = weekday;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PeriodEvent(short hour, short minute, List<Integer> weekday,
			String content, long id) {
		this.hour = hour;
		this.minute = minute;
		this.weekday = weekday;
		this.content = content;
		this.id = id;
	}

	public PeriodEvent() {
	}

}
