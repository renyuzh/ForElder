package com.nwpu.heartwings.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class FreshDialogUtil {

	public static void showDialog(Context context){
		
		final ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(null);
		progressDialog.setMessage("正在刷新中...");
		
		progressDialog.show();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				progressDialog.dismiss();
			}
		}, 2000);
	}
}
