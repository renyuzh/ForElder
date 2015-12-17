package com.nwpu.heartwings.app;

import java.util.HashMap;

import android.R.integer;
import android.app.Application;

public class HeartApp extends Application {

	private static HeartApp heartApp;
	
	public  int currentPage = 0;
	
	public HashMap<String, String> oldmanHashMap = new HashMap<>();
	
	public static HeartApp getInstance(){
		return heartApp;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		heartApp = this;
	}
}
