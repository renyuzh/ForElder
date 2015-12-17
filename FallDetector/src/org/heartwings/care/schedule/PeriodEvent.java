package org.heartwings.care.schedule;

public class PeriodEvent {
	private String content;
	private long fromTime;
	private long toTime;
	private int perioud;
	private short hour;
	private short minute;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getFromTime() {
		return fromTime;
	}
	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}
	public long getToTime() {
		return toTime;
	}
	public void setToTime(long toTime) {
		this.toTime = toTime;
	}
	public int getPerioud() {
		return perioud;
	}
	public void setPerioud(int perioud) {
		this.perioud = perioud;
	}
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
	public PeriodEvent(String content, long fromTime, long toTime, int perioud,
			short hour, short minute) {
		super();
		this.content = content;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.perioud = perioud;
		this.hour = hour;
		this.minute = minute;
	}
	
}
