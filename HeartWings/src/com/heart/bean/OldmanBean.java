package com.heart.bean;

import org.bson.types.ObjectId;

public class OldmanBean {

	private ObjectId objectId;
	private String phone;
	private String name;
	private String picUrl;
	private ObjectId bindman;
	
	public ObjectId getObjectId() {
		return objectId;
	}
	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public ObjectId getBindman() {
		return bindman;
	}
	public void setBindman(ObjectId bindman) {
		this.bindman = bindman;
	}
	
	
}
