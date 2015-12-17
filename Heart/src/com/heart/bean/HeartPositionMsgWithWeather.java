package com.heart.bean;

public class HeartPositionMsgWithWeather extends HeartMsg {

	private String area;
	private String degrees;
	private String dayPicUrl;
	private String weather;
	
	private String pm25;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDegrees() {
		return degrees;
	}
	public void setDegrees(String degrees) {
		this.degrees = degrees;
	}
	public String getDayPicUrl() {
		return dayPicUrl;
	}
	public void setDayPicUrl(String dayPicUrl) {
		this.dayPicUrl = dayPicUrl;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	

	
}
