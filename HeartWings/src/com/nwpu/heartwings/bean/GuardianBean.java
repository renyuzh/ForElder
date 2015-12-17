package com.nwpu.heartwings.bean;

import java.util.List;

public class GuardianBean {

	private String phone;
	private String userName;
	
	private String photoUrl;
	
	private String pwd;
	
	private String objectID;

	private List<OldmanBeanLocal> oldmanBeans;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public List<OldmanBeanLocal> getOldmanBeans() {
		return oldmanBeans;
	}

	public void setOldmanBeans(List<OldmanBeanLocal> oldmanBeans) {
		this.oldmanBeans = oldmanBeans;
	}
	
	
}
