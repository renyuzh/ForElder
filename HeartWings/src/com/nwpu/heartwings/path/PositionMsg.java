package com.nwpu.heartwings.path;


import com.heart.bean.RegularPosition;


public class PositionMsg {

	private int userAvata;
	private String userName;
	
	private RegularPosition regularPosition;

	public int getUserAvata() {
		
		return userAvata;
	}

	public void setUserAvata(int userAvata) {
		this.userAvata = userAvata;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public RegularPosition getRegularPosition() {
		return regularPosition;
	}

	public void setRegularPosition(RegularPosition regularPosition) {
		this.regularPosition = regularPosition;
	}
	
}
