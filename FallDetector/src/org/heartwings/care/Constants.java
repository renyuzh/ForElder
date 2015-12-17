/**
 * 
 */
package org.heartwings.care;

import java.net.URI;

/**
 * @author Inno520
 * 
 */
public class Constants {
	/**
	 * @author Inno520
	 * 和摔倒检测算法相关的参数，有些乱，含义也不清晰。所以这些参数由我来调整。
	 */
	public static class FallDetectDspParams {

		/**
		 * 传感器的采样率（毫秒）
		 */
		public final static double DSPPARAM_SAMPLE_PERIOUD = 20.0;
		/**
		 * 加速度下阈值（m/s^2）
		 */
		public final static double DSPPARAM_LOWER_THRESHOLD = 7.1;
		/**
		 * 加速度上阈值（m/s^2）
		 */
		public final static double DSPPARAM_HIGHER_THRESHOLD = 19.0;
		/**
		 * 自由落体至少需要的时间
		 */
		public final static double DSPPARAM_FREE_FALL_TIME_REQUIRED = 40.0;
		/**
		 * 需要稳定的毫秒数
		 */
		public final static double DSPPARAM_STABLE_TIME_REQUIRED = 1000.0;
		public final static double DSPPARAM_MINIMAL_ANGLE_REQUIRED = 30.0;
		/**
		 * 自由落体状态的最长持续时间
		 */
		public final static double DSPPARAM_FREE_FALL_WINDOW_TIME = 400;
		/**
		 * 在Stable状态时容许的非稳定值的次数
		 */
		public final static int DSPPARAM_STABLE_STATE_UNSTABLE_TOLERANT_COUNT = 10;
	}

	/**
	 * 摔倒后弹出的确认是否误判的对话框显示多少毫秒。
	 */
	public final static int NOTIFY_TIME_OUT = 120000;

	public final static String ADDRESS_PREFIX_INTERNAL = "http://10.128.52.177:8080/Heart";
	public final static String ADDRESS_PREFIX_PUBLISHED = "http://114.215.122.96/Heart";
	
	/**
	 * 所有地址的前缀
	 */
	public final static String ADDRESS_PREFIX = ADDRESS_PREFIX_INTERNAL;

	/**
	 * 登录Servlet的地址
	 */
	public final static String LOGIN_ADDRESS = ADDRESS_PREFIX + "/oldManLogin";
	
	/**
	 * 摔倒报告Servlet的地址
	 */
	public static final String ADDRESS_FALLDOWN_REPORT = ADDRESS_PREFIX
			+ "/addFallRecord";

	/**
	 * 注册Servlet的地址
	 */
	public final static String REGISTER_ADDRESS = ADDRESS_PREFIX
			+ "/oldManRegister";
	public static final String LOGIN_PHONE = "phone";
	public static final String LOGIN_PWD = "pwd";

	public final static String UPLOAD_POSITION_ADDRESS = ADDRESS_PREFIX
			+ "/addPositionRecord";

	// 照片共享服务器
	public static final String PHOTO_SHARE_SERVER_ADDRESS_PUBLISHED = "114.215.122.96";
	public static final String PHOTO_SHARE_SERVER_ADDRESS_INTERNAL = "10.128.52.177";
	
	
	/**
	 * Apache Mina服务器的地址
	 */
	public static final String PHOTO_SHARE_SERVER_ADDRESS = PHOTO_SHARE_SERVER_ADDRESS_INTERNAL;
	/**
	 * Apache Mina服务器的端口号
	 */
	public static final int PHOTO_SHARE_SERVER_PORT = 33333;

	public static final int MESSAGE_REQUEST_LOGIN = 1;
	public static final int MESSAGE_REQUEST_DOWNLOAD_IMAGE = 2;
	public static final int MESSAGE_REQUEST_UPLOAD_POSITION = 8;
	public static final int MESSAGE_REQUEST_REPORT_FALLDOWN = 13;
	public static final int MESSAGE_LOGIN_SUCCESS = 3;
	public static final int MESSAGE_LOGIN_FAILED_WRONG_PASSWORD = 4;
	public static final int MESSAGE_LOGIN_FAILED_CONNECTION_ISSUE = 5;
	public static final int MESSAGE_DOWNLOAD_SUCCESS = 6;
	public static final int MESSAGE_DOWNLOAD_FAILED = 7;
	public static final int MESSAGE_BIND_REQUEST = 10;
	public static final int MESSAGE_REQUEST_DIAL = 11;

	public static final String NAME_SHARED_PREFERENCE_MAIN_ACTIVITY = "SharedPreferenceMainActivity";
	public static final String NAME_SHARED_PREFERENCE_MAIN = "SharedPreferenceMain";

	/**
	 * 采集地理位置的毫秒数
	 */
	public static final long POSITION_GATHER_INTERVAL = 30 * 60 * 1000;
	// public static final long POSITION_IMMEDIATE_INTERVAL = 1000;
	public static final String DEBUG_TAG_LOCATION_UPLOAD = "LocationUpload";

	public static final String FILENAME_SINGLE_EVENT = "singleEvents";
	public static final String FILENAME_PERIOD_EVENT = "periodEvents";
}
