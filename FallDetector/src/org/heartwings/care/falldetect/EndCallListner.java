package org.heartwings.care.falldetect;

import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class EndCallListner extends PhoneStateListener {
	public final static String LOG_TAG = "CallState";

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		if (TelephonyManager.CALL_STATE_RINGING == state) {
			Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
		}
		if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
			// wait for phone to go offhook (probably set a boolean flag) so you
			// know your app initiated the call.
			Log.i(LOG_TAG, "OFFHOOK");
		}
		if (TelephonyManager.CALL_STATE_IDLE == state) {
			Log.i(LOG_TAG, "IDLE");
			Handler handler = DialOperator.getDialOperator().getHandler();
			synchronized (handler) {
				handler.notify();
			}
		}
	}
}
