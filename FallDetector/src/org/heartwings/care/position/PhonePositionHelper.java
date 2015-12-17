package org.heartwings.care.position;

import org.heartwings.care.Constants;
import org.heartwings.care.MainActivity;
import org.heartwings.network.NetworkOperationHelper;

import android.os.HandlerThread;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class PhonePositionHelper {
	private class LocationListener implements TencentLocationListener {
		@Override
		public void onLocationChanged(TencentLocation location, int error,
				String reason) {
			Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD, "New location received.");
			Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD,
					String.valueOf(location.getLongitude()) + ","
							+ String.valueOf(location.getLatitude()));
			Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD,
					"Accuracy: " + String.valueOf(location.getAccuracy()));
			Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD,
					"Name: " + location.getName());
			Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD,
					"Address: " + location.getAddress());
			NetworkOperationHelper.getNetworkOperationHelper().upload_position(
					MainActivity.getMainActivity().getUsername(),
					MainActivity.getMainActivity().getPassword(),
					location.getLongitude(), location.getLatitude(),
					location.getAddress(), System.currentTimeMillis());
		}
		@Override
		public void onStatusUpdate(String name, int status, String desc) {
			// TODO Auto-generated method stub
		}
	}

	private static PhonePositionHelper phonePositionHelper = null;

	public static PhonePositionHelper getPhonePositionHelper() {
		if (phonePositionHelper == null) {
			phonePositionHelper = new PhonePositionHelper();
		}
		return phonePositionHelper;
	}

	private TencentLocationRequest locationRequest;
//	private TencentLocationRequest immediateLocationRequest;
	private LocationListener locationListener;
	private TencentLocationManager locationManager;
	private HandlerThread locationSensorThread;

	private PhonePositionHelper() {
		locationListener = new LocationListener();
		locationRequest = TencentLocationRequest.create();
		locationRequest.setInterval(Constants.POSITION_GATHER_INTERVAL);
//		immediateLocationRequest = TencentLocationRequest.create();
//		immediateLocationRequest.setInterval(Constants.POSITION_IMMEDIATE_INTERVAL);
//		
		locationManager = TencentLocationManager.getInstance(MainActivity
				.getTheApplicationContext());
	}

	private TencentLocation retrieveCurrentLocation() {
		
		return null;
	}

	public boolean startGathering() {
		Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD,
				"Location uploader has started.");
		locationSensorThread = new HandlerThread("LocationThread");
		locationSensorThread.start();
		int error = locationManager.requestLocationUpdates(locationRequest,
				locationListener, locationSensorThread.getLooper());
		if (error == 0) {
			Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD, "Success to register.");
		} else {
			Log.e(Constants.DEBUG_TAG_LOCATION_UPLOAD, "FAILED to register.");
		}
		return error == 0;
	}

	public void stopGathering() {
		Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD,
				"Location uploader has stopped");
		locationManager.removeUpdates(locationListener);
		locationSensorThread.getLooper().quit();
	}
}
