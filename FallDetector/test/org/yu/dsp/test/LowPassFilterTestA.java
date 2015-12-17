package org.yu.dsp.test;

import org.heartwings.care.falldetect.LowPassFilter;

import android.test.InstrumentationTestCase;
import android.util.Log;

public class LowPassFilterTestA extends InstrumentationTestCase {

	public void testNextValue() {
		LowPassFilter lpf;
		lpf = new LowPassFilter(80, 20);
		Log.i("TEST", String.valueOf(lpf.nextValue(1)));
		Log.i("TEST", String.valueOf(lpf.nextValue(30)));
		Log.i("TEST", String.valueOf(lpf.nextValue(10)));
//		for(int i=0;i<50;i++)
//			Log.i("TEST", String.valueOf(lpf.nextValue(i)));
//		Log.i("TEST", String.valueOf(lpf.nextValue(1000)));
//		Log.i("TEST", String.valueOf(lpf.nextValue(1000)));
//		for(int i=52;i<100;i++)
//			Log.i("TEST", String.valueOf(lpf.nextValue(i)));
	}
}
