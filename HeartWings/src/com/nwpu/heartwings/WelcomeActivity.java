package com.nwpu.heartwings;

import java.util.ArrayList;

import com.heart.bean.OldmanBean;
import com.nwpu.heartwings.activities.FuncionActivity;
import com.nwpu.heartwings.activities.LoginActivity;
import com.nwpu.heartwings.app.HeartApp;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;

import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends ActionBarActivity {

	private SharedPreferences preferences;
	public static final int DISPLAY_LENGTH = 1000;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);

	
		
		preferences = getSharedPreferences(CONSTANTS.PREFERENCE, MODE_PRIVATE);
		

		
		
		boolean isLogin = preferences.getBoolean(CONSTANTS.HASLOGIN, false);
		
		final Intent intent = new Intent();
		
		if(isLogin){
			
		       new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						 
						// 先去初始化 oldmanHashMap
						ArrayList<OldmanBean> oldmanBeans =
								LocalFileUtil.searchForAllBinded(LocalFileUtil.getThisClient(WelcomeActivity.this));
						
						
						HeartApp.getInstance().oldmanHashMap.clear();
						
						for(OldmanBean oldmanBean : oldmanBeans){
							   
							 HeartApp.getInstance().oldmanHashMap.put(oldmanBean.getPhone(), oldmanBean.getName());
						}
						
						
						intent.setClass(WelcomeActivity.this, FuncionActivity.class);
						startActivity(intent);
						WelcomeActivity.this.finish();
						overridePendingTransition(R.anim.fade, R.anim.hold);
					}
				}, DISPLAY_LENGTH);
		}
		else {
			
	       new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				  
				intent.setClass(WelcomeActivity.this, LoginActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		}, DISPLAY_LENGTH);
			
		}
		
	}




}
