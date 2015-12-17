package com.nwpu.heartwings;

import com.nwpu.heartwings.util.CONSTANTS;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class LuanchActivity extends ActionBarActivity {

	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		preferences = getSharedPreferences(CONSTANTS.PREFERENCE, MODE_PRIVATE);
		
		boolean isFirstUse = preferences.getBoolean(CONSTANTS.ISFIRST, true);
		
		Intent intent = new Intent();
		if(isFirstUse){
			
			intent.setClass(LuanchActivity.this, GuideActivity.class);
			startActivity(intent);
			
			Editor editor = preferences.edit();
			editor.putBoolean(CONSTANTS.ISFIRST, false);
			editor.commit();
			
			this.finish();
		}
		else {
			
			intent.setClass(LuanchActivity.this, WelcomeActivity.class);
			startActivity(intent);
			this.finish();
		}

		
		
	}



}
