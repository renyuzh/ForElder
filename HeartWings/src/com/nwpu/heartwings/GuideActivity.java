package com.nwpu.heartwings;

import com.nwpu.heartwings.guide.GuideFragmentAdapter;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.view.DirectionalViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.os.Bundle;

public class GuideActivity extends ActionBarActivity {

	private DirectionalViewPager mDirectionalViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_guide);

		mDirectionalViewPager = (DirectionalViewPager)findViewById(R.id.guide_pager);
		
		mDirectionalViewPager.setAdapter(new GuideFragmentAdapter(getSupportFragmentManager()));
	}




}
