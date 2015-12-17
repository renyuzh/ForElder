package com.nwpu.heartwings.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UpdateSharedPreferences {

	public static void updateBoolean(String preferencesName, String key, boolean newValue,Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName,
				android.content.Context.MODE_PRIVATE);
		
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, newValue);
		editor.commit();
	}
	
/*	public static void updateString(final String preferencesName, final String key,final String newValue,final Context context)
	{
		 new Thread(){

			@Override
			public void run() {
				
				SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName,
						android.content.Context.MODE_PRIVATE);
				
				Editor editor = sharedPreferences.edit();
				
				editor.putString(key, newValue);
				editor.commit();
			}
		 }.start();
	}*/

	public static void updateMapString(final String preferencesName,final Map<String, String> values,
			    final Context context){
		
		     new Thread(){

				@Override
				public void run() {
				       
					SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName,
							android.content.Context.MODE_PRIVATE);
					
					Editor editor = sharedPreferences.edit();
					
					for(String key: values.keySet()){
						
						  editor.putString(key, values.get(key));
					}
					
					editor.commit();
				}
		    	 
		    	   
		     }.start();
	}
}
		
