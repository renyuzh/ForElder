package com.nwpu.heartwings.activities;

import java.util.concurrent.ExecutionException;

import com.nwpu.heartwings.R;
import com.nwpu.heartwings.util.ImageLoadDialog;
import com.nwpu.heartwings.util.ReadBitMapUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		      
		   super.onCreate(savedInstanceState);
		   
		   setContentView(R.layout.big_image_view);
		   
		   final String imagePath = getIntent().getStringExtra("image_path");
		   final ImageView imageView = (ImageView)findViewById(R.id.image_show_big);
		   new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					imageView.setImageBitmap(ReadBitMapUtil.getShareBitmap(imagePath));
				} catch (InterruptedException | ExecutionException e) {
					
					imageView.setImageResource(R.drawable.missingfile);
					
					e.printStackTrace();
				}
			}
		}).start();
		  
		   
		   final ImageLoadDialog dialog = new ImageLoadDialog(this);
		   
		   dialog.show();
		   
		   new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				dialog.dismiss();
			}
		}, 1200);
	 }
	 
	 @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        // TODO Auto-generated method stub
	        finish();
	        return true;
	    }
}
