package org.heartwings.care.falldetect;

import java.util.List;

import org.heartwings.care.Constants.FallDetectDspParams;
import org.heartwings.care.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

public class FallDetectService extends Service {
	private static boolean started;

	private static FallDetectService fallDetectService = null;

	public static FallDetectService getFallDetectService() {
		return fallDetectService;
	}

	public static boolean getStarted() {
		return started;
	}

	class MySensorEventListener implements SensorEventListener {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			Log.i("Sensor",
					"Sensor accuracy has changed to "
							+ String.valueOf(accuracy));
		}

		private int lastState = -1;

		@Override
		public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				fallDetector.feedData((double) event.timestamp / 1000000.0,
						event.values[0], event.values[1], event.values[2]);
				switch (fallDetector.getStatus()) {
				case ThresholdFallDetector.Confirmed:
					try {
						fallDownAlerter.notifyTheDanger();
						fallDetector
								.setStatus(ThresholdFallDetector.Collecting);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case ThresholdFallDetector.Stable:
					if (lastState != ThresholdFallDetector.Stable) {
						Toast.makeText(getApplicationContext(), "Stable",
								Toast.LENGTH_SHORT).show();
						Log.i("FallDetect", "Get to stable");
					}
					break;
				case ThresholdFallDetector.Freefall:
					if (lastState != ThresholdFallDetector.Freefall) {
						Log.i("FallDetect", "Get to freefall");
					}
					break;
				case ThresholdFallDetector.Collecting:
					if (lastState != ThresholdFallDetector.Collecting) {
						Log.i("FallDetect", "Get to collecting");
					}
					break;
				}
				lastState = fallDetector.getStatus();
				break;
			case Sensor.TYPE_GYROSCOPE:
				fallDetector.feedGyroscope(
						(double) event.timestamp / 1000000.0, event.values[0],
						event.values[1], event.values[2]);
				break;
			}
		}
	}

	private HandlerThread handlerThread;
	private Handler handler;
	private Looper looper;

	private SensorManager sensorManager;
	private ThresholdFallDetector fallDetector;
	private Sensor accelerometerSensor;
	private Sensor gyroscopeSensor;
	private MySensorEventListener mySensorEventListener;

	private boolean isStarted;
	private PowerManager powerManager;
	private WakeLock wakeLock;
	private FallDownAlerter fallDownAlerter;

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		fallDetectService = this;
		try {
			powerManager = (PowerManager) getApplicationContext()
					.getSystemService(Context.POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					"MyWakeLock");
			fallDownAlerter = new FallDownAlerter(getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

		isStarted = false;
		FallDetectService.started = false;
		Log.i("FallDetect", "Service for fall detect is creating.");
		sensorManager = (SensorManager) this.getApplicationContext()
				.getSystemService(SENSOR_SERVICE);

		fallDetector = new ThresholdFallDetector(FallDetectDspParams.DSPPARAM_SAMPLE_PERIOUD,
				FallDetectDspParams.DSPPARAM_HIGHER_THRESHOLD, FallDetectDspParams.DSPPARAM_LOWER_THRESHOLD,
				FallDetectDspParams.DSPPARAM_STABLE_TIME_REQUIRED,
				FallDetectDspParams.DSPPARAM_FREE_FALL_TIME_REQUIRED,
				FallDetectDspParams.DSPPARAM_FREE_FALL_WINDOW_TIME,
				FallDetectDspParams.DSPPARAM_MINIMAL_ANGLE_REQUIRED,
				FallDetectDspParams.DSPPARAM_STABLE_STATE_UNSTABLE_TOLERANT_COUNT);

		List<Sensor> accelerometerList = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		List<Sensor> gyroscopeList = sensorManager
				.getSensorList(Sensor.TYPE_GYROSCOPE);
		if (accelerometerList.size() <= 0) {
			Log.e("Sensor", "Unsupported sensor: ACCELEROMETER");
			accelerometerSensor = null;
		} else {
			accelerometerSensor = accelerometerList.get(0);
		}
		if (gyroscopeList.size() <= 0) {
			Log.e("Sensor", "Unsupported sensor: GYROSCOPE");
			gyroscopeSensor = null;
		} else {
			gyroscopeSensor = gyroscopeList.get(0);
		}

		Log.i("FallDetect",
				"Creation: try to create a handler thread for receiving sensor data.");

		try {
			handlerThread = new HandlerThread("Sensor Data Listener",
					Process.THREAD_PRIORITY_DISPLAY);
			handlerThread.start();
			looper = handlerThread.getLooper();
			handler = new Handler(looper);
			mySensorEventListener = new MySensorEventListener();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set the service as ForegroundService
		// Intent notificationIntent = new Intent(this, MainActivity.class);
		// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);
		Notification notification = new Notification.Builder(this)
				.setSmallIcon(R.drawable.logo123).setContentTitle("牵挂正在保护您的安全")
				.build();
		startForeground(1314, notification);

		Log.i("FallDetect", "Creation: completed.");

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		fallDetectService = null;
		Log.i("FallDetect", "Service for fall detect is destroying.");
		sensorManager.unregisterListener(mySensorEventListener);
		isStarted = false;
		fallDetector = null;
		stopForeground(true);
		FallDetectService.started = false;
		looper.quit();
		DialOperator.getDialOperator().unregisterEndCallListener();
		DialOperator.removeAllDial();
		wakeLock.release();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!isStarted) {
			Log.i("FallDetect", "Service for fall detect is starting.");
			try {
				wakeLock.acquire();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sensorManager.registerListener(mySensorEventListener,
					accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST,
					handler) == false) {
				Log.e("Sensor", "Fail to register accelerometer sensor.");
			} else {
				if (sensorManager.registerListener(mySensorEventListener,
						gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST,
						handler) == false) {
					sensorManager.unregisterListener(mySensorEventListener);
					Log.e("Sensor", "Fail to register gyroscope sensor.");
				} else {
					Log.i("FallDetect", "Start to collect sensor data.");
					isStarted = true;
					FallDetectService.started = true;
				}
			}
			DialOperator.setContext(getApplicationContext());
			DialOperator.getDialOperator().registerEndCallListener();
		}
		return super.onStartCommand(intent, START_STICKY, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
