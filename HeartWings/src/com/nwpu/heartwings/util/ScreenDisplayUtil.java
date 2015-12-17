package com.nwpu.heartwings.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;



public class ScreenDisplayUtil {
	   
	    @SuppressLint("NewApi")
		public static int getWidth(Context context){
	    	 
	    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    	Display display = wm.getDefaultDisplay();
	    	Point size = new Point();
	    	display.getSize(size);
	    	return size.x;
	    }
	    
	
}
