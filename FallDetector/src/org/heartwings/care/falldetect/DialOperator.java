package org.heartwings.care.falldetect;

import org.heartwings.care.Constants;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 【单例】【异步】拨号器，发送消息拨打指定电话。
 * @author Inno520
 */
public class DialOperator {
	private class MyHandler extends Handler {
		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public synchronized void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MESSAGE_REQUEST_DIAL:
				String phoneNumber = (String) msg.obj;
				phoneNumber = phoneNumber.replaceAll("[ -]", "");
				Log.i("Dial", "Try to dial number: " + phoneNumber);
				Intent intent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + phoneNumber));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				FallDetectService.getFallDetectService().startActivity(intent);
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			super.handleMessage(msg);
		}
	}

	private static Context context = null;
	private static DialOperator dialOperator = null;

	public static DialOperator getDialOperator() {
		if (dialOperator == null)
			dialOperator = new DialOperator(context);
		return dialOperator;
	}

	public static void setContext(Context context) {
		DialOperator.context = context;
	}

	public static void removeAllDial() {
		dialOperator.dialHandler.removeMessages(Constants.MESSAGE_REQUEST_DIAL);
	}

	private Handler dialHandler;

	public Handler getHandler() {
		return dialHandler;
	}

	private HandlerThread handlerThread;

	private DialOperator(Context context) {
		handlerThread = new HandlerThread("Dial_Helper");
		handlerThread.start();
		dialHandler = new MyHandler(handlerThread.getLooper());
	}

	private EndCallListner callListener = null;

	DialOperator registerEndCallListener() {
		if (callListener == null) {
			callListener = new EndCallListner();
		}
		TelephonyManager mTM = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
		return this;
	}

	DialOperator unregisterEndCallListener() {
		if (callListener != null) {
			TelephonyManager mTM = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			mTM.listen(callListener, PhoneStateListener.LISTEN_NONE);
		}
		return this;
	}
}
