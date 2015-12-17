package org.heartwings.care.util;

import com.tencent.map.geolocation.TencentLocation;

public class FallDownReport {
	private TencentLocation location;
	private Long timestamp;
	private String name;
	private String password;
	public TencentLocation getLocation() {
		return location;
	}
	public void setLocation(TencentLocation location) {
		this.location = location;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
