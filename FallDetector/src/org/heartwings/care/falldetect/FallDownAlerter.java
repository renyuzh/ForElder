package org.heartwings.care.falldetect;

import java.util.Set;

import org.heartwings.care.Constants;
import org.heartwings.care.MainActivity;
import org.heartwings.care.photoshare.PhotoSharingService;
import org.heartwings.care.util.AndroidRingstonePlayer;
import org.heartwings.network.NetworkOperationHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

public class FallDownAlerter {
	private Vibrator vibrator;
	private long[] viberatePattern = { 0, 1000, 1000 };
	private String messageContent = "你好世界，这是一个测试。";
	private boolean isNotifyingConfirm = false;
	private boolean userSelected = false;
	private Context context;
	private AlertDialog confirmDialog;

	private AndroidRingstonePlayer arp;

	/**
	 * 确认摔倒后执行的动作
	 */
	private void action() {
		Log.i("FallDetector", "Taking action.");
		NetworkOperationHelper.getNetworkOperationHelper().report_falldown(
				context.getSharedPreferences(
						Constants.NAME_SHARED_PREFERENCE_MAIN,
						Context.MODE_PRIVATE).getString("username", ""),
				System.currentTimeMillis());
		try {
			Set<String> phoneNumbers = context
					.getSharedPreferences(
							Constants.NAME_SHARED_PREFERENCE_MAIN,
							context.MODE_PRIVATE).getStringSet(
							Constants.NAME_SHARED_PREFERENCE_MAIN, null);
			if (phoneNumbers == null || phoneNumbers.size()==0) {
				Log.w("FallDetect", "Emergency phone numbers is not set.");
			} else {
				for (String phoneNumber : phoneNumbers) {
					Log.i("FallDetect", "Register to dial: " + phoneNumber);
					phoneNumber = phoneNumber.replaceAll("[ -]", "");
//					Intent intent = new Intent("android.intent.action.CALL",
//							Uri.parse("tel:" + phoneNumber));
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.startActivity(intent);
					Message msg = new Message();
					msg.what = Constants.MESSAGE_REQUEST_DIAL;
					msg.obj = phoneNumber;
					DialOperator.getDialOperator().getHandler().sendMessage(msg);
				}
			}
			// DialOperator.getDialOperator().getHandler().send
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FallDownAlerter(Context context) {
		this.context = context;
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		try {
			arp = new AndroidRingstonePlayer(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This class is in a mess, generally, this function displays a system
	 * dialog to confirm whether wrong or not with a vibration and 30-second
	 * timeout
	 */
	public void dialogForTime() {
		userSelected = false;
		confirmDialog = new AlertDialog.Builder(context)
				.setTitle("如果是误判请取消")
				.setMessage("已检测到摔倒，如果这是一次误报，请点击'误报'")
				.setPositiveButton("这是误报",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								arp.stop();
								userSelected = true;
								isNotifyingConfirm = false;
								vibrator.cancel();
							}
						})
				.setNegativeButton("我真的摔倒了",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								arp.stop();
								userSelected = true;
								isNotifyingConfirm = false;
								vibrator.cancel();
								action();
							}
						}).create();
		confirmDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		confirmDialog.setCancelable(false);
		confirmDialog.setCanceledOnTouchOutside(false);
		confirmDialog.show();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				vibrator.vibrate(viberatePattern, 0);
				arp.play();
				try {
					Thread.sleep(Constants.NOTIFY_TIME_OUT);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!userSelected) {
					vibrator.cancel();
					arp.stop();
					confirmDialog.dismiss();
					confirmDialog = null;
					isNotifyingConfirm = false;
					action();
				}
			}
		}, "dialog");
		thread.start();
	}

	public void notifyTheDanger() {
		Log.i("FallDetect",
				"A fall down has been confirmed, now notifying to the user.");
		if (!isNotifyingConfirm) {
			isNotifyingConfirm = true;
			dialogForTime();
		}
	}
}