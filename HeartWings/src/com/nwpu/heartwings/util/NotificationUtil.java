package com.nwpu.heartwings.util;

import com.nwpu.heartwings.BindResultActivity;

import android.R;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
public class NotificationUtil {

	private static NotificationManager nm;
	
	@SuppressLint("InlinedApi")
	public static void sendNotification(Context context, String title, String message){
		
		
		nm = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(context);
		
		Intent nofifyIntent = new Intent(context, BindResultActivity.class);
	
		nofifyIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		PendingIntent intent = PendingIntent.getActivity(context, 0, nofifyIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
		
		builder.setContentIntent(intent);
		
		builder.setContentTitle(title);
		builder.setContentText(message);
		builder.setTicker("一条新消息");
		builder.setWhen(System.currentTimeMillis());
		builder.setSmallIcon(R.drawable.sym_action_email);
		builder.setAutoCancel(true);
		nm.notify(0x177, builder.build());

	}
}
