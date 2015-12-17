package com.heart.bean;

import java.util.ArrayList;


public class RegularShare {

	private String oldmanPhone;
	private String oldmanName;
	
	private ArrayList<String> picUrls;
	
	private Long date;

	public String getOldmanPhone() {
		return oldmanPhone;
	}

	public void setOldmanPhone(String oldmanPhone) {
		this.oldmanPhone = oldmanPhone;
	}

	public String getOldmanName() {
		return oldmanName;
	}

	public void setOldmanName(String oldmanName) {
		this.oldmanName = oldmanName;
	}

	public ArrayList<String> getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(ArrayList<String> picUrls) {
		this.picUrls = picUrls;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}
	
	
}
