package com.nwpu.heartwings.util;

import com.nwpu.heartwings.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class ImageLoadDialog extends Dialog {

	public ImageLoadDialog(Context context) {
		super(context,R.style.ImageloadingDialogStyle);
		
	}

	private ImageLoadDialog(Context context, int theme) {
        super(context, theme);
    }
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		 
          super.onCreate(savedInstanceState);
          
          setContentView(R.layout.image_loading);
	 }
	
	
}
