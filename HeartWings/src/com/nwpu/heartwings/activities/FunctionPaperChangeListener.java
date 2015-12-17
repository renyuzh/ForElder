package com.nwpu.heartwings.activities;

import com.nwpu.heartwings.util.ScreenDisplayUtil;

import android.app.Activity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class FunctionPaperChangeListener implements OnPageChangeListener{

	MyPaper mPaper;
	
	double ONE;
	double TWO;
	double THREE;
	
	float ZORE = 0.0f;
	
	int currIndex = 0;
	
	public FunctionPaperChangeListener(Activity activity){
		
		   try {
			
			   mPaper = (MyPaper)activity;
			    
			   int width  = ScreenDisplayUtil.getWidth(activity);
			   ONE = width / 4.0;
			   TWO = ONE * 2;
			   THREE = ONE * 3;
			   
		} catch (ClassCastException e) {
			   throw new ClassCastException("activity cannot be casted.");
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
		    
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
		  
	}

	@Override
	public void onPageSelected(int position) {
		
		  mPaper.getCurrentPosition(position);
		  
		
		  
	}

	static interface MyPaper{
		   
		public int getCurrentPosition(int position);
		     
		     
	}
	
}
