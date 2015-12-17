package com.nwpu.heartwings.services;

import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.MSGUtil;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class MinaService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
	    
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
/*	    SharedPreferences preferences = getSharedPreferences(CONSTANTS.PREFERENCE, MODE_PRIVATE);
	    final String name = preferences.getString("login_phone", "");
	        
	    new Thread(){
	    	
			@Override
			public void run() {
			     
			    MSGUtil.session = MSGUtil.getIOSession(name);
			    MSGUtil.session.getCloseFuture().awaitUninterruptibly();
			    MSGUtil.connector.dispose();
			}
	    	
	    }.start();*/
	    
		return START_STICKY;
	}

	@Override
	public void onCreate() {
	
		SharedPreferences preferences = getSharedPreferences(CONSTANTS.PREFERENCE, MODE_PRIVATE);
	    final String name = preferences.getString("login_phone", "");
	    
	    new Thread(){

			@Override
			public void run() {
			      
			    MSGUtil.session = MSGUtil.getIOSession(name);
			    MSGUtil.session.getCloseFuture().awaitUninterruptibly();
			    MSGUtil.connector.dispose();
			}
	    	
	    	
	    }.start();

	}
	
	

	
	
	
}
