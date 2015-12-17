package com.nwpu.heartwings.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetWork {

	public static boolean isNetAvailable(Context context)
	{
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.
				getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
		if(networkInfo == null || !networkInfo.isAvailable())
		{
			return false;		
		}
		
		return true;
	}
}
