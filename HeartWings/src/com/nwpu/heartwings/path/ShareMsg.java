package com.nwpu.heartwings.path;

import java.util.List;

public class ShareMsg {

	private long time;
	
	private List<String> photoUrls;
	
	private String userAvat;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public ShareMsg(long time, List<String> photoUrls, String userAvat) {
		super();
		this.time = time;
		this.photoUrls = photoUrls;
		this.userAvat = userAvat;
	}

	
	
	
}
