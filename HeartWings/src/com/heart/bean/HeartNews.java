package com.heart.bean;

import java.io.Serializable;

public class HeartNews implements Serializable{

	private static final long serialVersionUID = 7449697236791608183L;
	
	private String title;
	private String newsContent;
	private String newsImg;
	
	private String id;
	
	private String newsAbstract;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNewsContent() {
		return newsContent;
	}
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	public String getNewsImg() {
		return newsImg;
	}
	public void setNewsImg(String newsImg) {
		this.newsImg = newsImg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNewsAbstract() {
		return newsAbstract;
	}
	public void setNewsAbstract(String newsAbstract) {
		this.newsAbstract = newsAbstract;
	}
	
	
}
