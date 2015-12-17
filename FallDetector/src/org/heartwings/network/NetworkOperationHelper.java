package org.heartwings.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.heartwings.care.Constants;
import org.heartwings.care.util.UsernamePasswordPair;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

/**
 * @author Inno520 <单例> 负责登陆、下载等网络操作
 * 
 */
public class NetworkOperationHelper {
	private HandlerThread networkOperationHandlerThread;
	private NetworkOperationHandler networkOperationHandler;
	private static NetworkOperationHelper networkOperationHelper = null;

	public static NetworkOperationHelper getNetworkOperationHelper() {
		if (networkOperationHelper == null) {
			networkOperationHelper = new NetworkOperationHelper();
		}
		return networkOperationHelper;
	}

	private NetworkOperationHelper() {
		networkOperationHandlerThread = new HandlerThread(
				"NetworkOperationThread");
		networkOperationHandlerThread.start();
		networkOperationHandler = new NetworkOperationHandler(
				networkOperationHandlerThread.getLooper());
	}

	public Looper getOperatorLooper() {
		return networkOperationHandlerThread.getLooper();
	}

	public void login(String username, String password, Handler onResult) {
		Message msg = new Message();
		msg.what = Constants.MESSAGE_REQUEST_LOGIN;
		msg.obj = new LoginRequest(username, password, onResult);
		networkOperationHandler.sendMessage(msg);
	}

	public void download(Context context, String targetUrl, Handler onResult) {
		Message msg = new Message();
		msg.what = Constants.MESSAGE_REQUEST_DOWNLOAD_IMAGE;
		msg.obj = new DownloadRequest(context, targetUrl, onResult);
		networkOperationHandler.sendMessage(msg);
	}

	public void upload_position(String username, String password,
			double longitude, double latitude, String area, long date) {
		Message msg = new Message();
		msg.what = Constants.MESSAGE_REQUEST_UPLOAD_POSITION;
		msg.obj = new UploadPositionRequest(username, password, longitude,
				latitude, area, date);
		networkOperationHandler.sendMessage(msg);
	}

	public void report_falldown(String username, long dateTime) {
		Log.i("Network", "Try to report this falldown: " + username);
		Message msg = new Message();
		msg.what = Constants.MESSAGE_REQUEST_REPORT_FALLDOWN;
		msg.obj = new ReportFallDownRequest(username, dateTime);
		networkOperationHandler.sendMessage(msg);
	}

	private class ReportFallDownRequest {
		private String username;
		private long dateTime;

		public ReportFallDownRequest(String username, long dateTime) {
			super();
			this.username = username;
			this.dateTime = dateTime;
		}
	}

	private class DownloadRequest {
		private String targetURL;
		private Handler onResult;
		private Context context;

		public DownloadRequest(Context context, String targetURL,
				Handler onResult) {
			this.targetURL = targetURL;
			this.onResult = onResult;
			this.context = context;
		}
	}

	private class LoginRequest implements UsernamePasswordPair {
		private String username;
		private String password;
		private Handler onResult;

		public LoginRequest(String username, String password, Handler onResult) {
			this.username = username;
			this.password = password;
			this.onResult = onResult;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}

	private class UploadPositionRequest {
		private String username;
		private String password;
		private double longitude;
		private double latitude;
		private String area;
		private long date;

		public UploadPositionRequest(String username, String password,
				double longitude, double latitude, String area, long date) {
			super();
			this.username = username;
			this.password = password;
			this.longitude = longitude;
			this.latitude = latitude;
			this.area = area;
			this.date = date;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		public double getLongitude() {
			return longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public String getArea() {
			return area;
		}

		public long getDate() {
			return date;
		}
	}

	public class NetworkOperationHandler extends Handler {
		public final HttpClient httpClient;
		public final HttpParams defaultHttpParams = new BasicHttpParams();

		public NetworkOperationHandler(Looper looper) {
			super(looper);
			defaultHttpParams.setParameter("charset", HTTP.UTF_8);
			HttpConnectionParams.setConnectionTimeout(defaultHttpParams,
					10 * 1000);
			HttpConnectionParams.setSoTimeout(defaultHttpParams, 10 * 1000);
			httpClient = new DefaultHttpClient(defaultHttpParams);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MESSAGE_REQUEST_LOGIN:
				Log.i("Login", "Request to login");
				LoginRequest loginRequest = (LoginRequest) msg.obj;
				String username = loginRequest.username;
				String password = loginRequest.password;
				HttpPost post = new HttpPost(Constants.LOGIN_ADDRESS);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(Constants.LOGIN_PHONE,
						username));
				params.add(new BasicNameValuePair(Constants.LOGIN_PWD, password));
				try {
					post.setEntity(new UrlEncodedFormEntity(params));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					HttpResponse response = httpClient.execute(post);
					String result;
					if (response.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(response.getEntity());
						Log.i("Login", "Login received result " + result);
						if (result.equals("ok")) {
							Message message = new Message();
							message.obj = loginRequest;
							message.what = Constants.MESSAGE_LOGIN_SUCCESS;
							loginRequest.onResult.sendMessage(message);
						} else {
							loginRequest.onResult
									.sendEmptyMessage(Constants.MESSAGE_LOGIN_FAILED_WRONG_PASSWORD);
						}
					} else {
						result = null;
						loginRequest.onResult
								.sendEmptyMessage(Constants.MESSAGE_LOGIN_FAILED_CONNECTION_ISSUE);
					}
				} catch (ClientProtocolException e) {
					loginRequest.onResult
							.sendEmptyMessage(Constants.MESSAGE_LOGIN_FAILED_CONNECTION_ISSUE);
					e.printStackTrace();
				} catch (IOException e) {
					loginRequest.onResult
							.sendEmptyMessage(Constants.MESSAGE_LOGIN_FAILED_CONNECTION_ISSUE);
					e.printStackTrace();
				}
				break;
			case Constants.MESSAGE_REQUEST_DOWNLOAD_IMAGE:
				DownloadRequest downloadRequest = (DownloadRequest) msg.obj;
				// String uri = downloadRequest.targetURL;
				String targetString = Constants.ADDRESS_PREFIX
						+ downloadRequest.targetURL;
				Log.i("Download", "Try to download file from " + targetString);
				HttpGet httpGet = new HttpGet(targetString);
				try {
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						InputStream inputStream = httpEntity.getContent();
						Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						Log.i("Download", "Retrieved the file, try to save it");

						File externalPath = Environment
								.getExternalStorageDirectory();
						File dir = new File(externalPath.getAbsolutePath()
								+ "/qiangua/");
						dir.mkdirs();
						File file = new File(dir, String.valueOf(System
								.currentTimeMillis()) + ".jpg");
						String filePath = file.getAbsolutePath();
						FileOutputStream output = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
						output.flush();
						output.close();

						Log.i("Download",
								"File Save Complete; add it to content provider.");

						// Add the file to the content provider
						ContentValues values = new ContentValues();
						values.put(Images.Media.DATE_TAKEN,
								System.currentTimeMillis());
						values.put(Images.Media.MIME_TYPE, "image/jpeg");
						values.put(MediaStore.MediaColumns.DATA, filePath);

						Uri resultUri = downloadRequest.context
								.getContentResolver().insert(
										Images.Media.EXTERNAL_CONTENT_URI,
										values);

						// String resultUri =
						// MediaStore.Images.Media.insertImage(
						// downloadRequest.context.getContentResolver(),
						// bitmap, UUID.randomUUID().toString(),
						// "Image I have received");

						if (resultUri != null) {
							downloadRequest.onResult
									.sendEmptyMessage(Constants.MESSAGE_DOWNLOAD_SUCCESS);
						} else {
							downloadRequest.onResult
									.sendEmptyMessage(Constants.MESSAGE_DOWNLOAD_FAILED);
						}
					}
				} catch (ClientProtocolException e) {
					downloadRequest.onResult
							.sendEmptyMessage(Constants.MESSAGE_DOWNLOAD_FAILED);
					e.printStackTrace();
				} catch (IOException e) {
					downloadRequest.onResult
							.sendEmptyMessage(Constants.MESSAGE_DOWNLOAD_FAILED);
					e.printStackTrace();
				}
				break;
			case Constants.MESSAGE_REQUEST_UPLOAD_POSITION:
				UploadPositionRequest uploadPositionRequest = (UploadPositionRequest) msg.obj;

				HttpPost httpPost = new HttpPost(
						Constants.UPLOAD_POSITION_ADDRESS);
				httpPost.addHeader("charset", HTTP.UTF_8);
				List<NameValuePair> uploadParams = new ArrayList<NameValuePair>();
				uploadParams.add(new BasicNameValuePair("oldmanPhone",
						uploadPositionRequest.getUsername()));
				uploadParams.add(new BasicNameValuePair("oldmanPwd",
						uploadPositionRequest.getPassword()));
				uploadParams.add(new BasicNameValuePair("longtitude", String
						.valueOf(uploadPositionRequest.getLongitude())));
				uploadParams.add(new BasicNameValuePair("latitude", String
						.valueOf(uploadPositionRequest.getLatitude())));
				uploadParams.add(new BasicNameValuePair("area", String
						.valueOf(uploadPositionRequest.getArea())));
				uploadParams.add(new BasicNameValuePair("date", String
						.valueOf(uploadPositionRequest.getDate())));

				try {
					httpPost.setEntity(new UrlEncodedFormEntity(uploadParams,
							"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					Log.e("UploadAddressOperator", "Unsupported encoding UTF-8");
				}
				try {
					HttpResponse response = httpClient.execute(httpPost);
					if (response.getStatusLine().getStatusCode() == 200) {
						Log.i(Constants.DEBUG_TAG_LOCATION_UPLOAD,
								"Location has been succesfully uploaded. ");
					} else {
						Log.e(Constants.DEBUG_TAG_LOCATION_UPLOAD,
								"Upload failed due to network problem with code "
										+ String.valueOf(response
												.getStatusLine()
												.getStatusCode()));
						Log.e(Constants.DEBUG_TAG_LOCATION_UPLOAD, "URL: "
								+ Constants.UPLOAD_POSITION_ADDRESS);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case Constants.MESSAGE_REQUEST_REPORT_FALLDOWN:
				ReportFallDownRequest request = (ReportFallDownRequest) msg.obj;
				HttpPost httpPost1 = new HttpPost(
						Constants.ADDRESS_FALLDOWN_REPORT);
				httpPost1.addHeader("charset", HTTP.UTF_8);
				List<NameValuePair> uploadParams1 = new ArrayList<NameValuePair>();
				uploadParams1.add(new BasicNameValuePair("oldman_phone",
						request.username));
				uploadParams1.add(new BasicNameValuePair("date", String
						.valueOf(request.dateTime)));
				try {
					httpPost1.setEntity(new UrlEncodedFormEntity(uploadParams1,
							"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					Log.e("Network", "Unsupported encoding UTF-8");
				}
				try {
					HttpResponse response = httpClient.execute(httpPost1);
					if (response.getStatusLine().getStatusCode() == 200) {
						Log.i("Network",
								"Fall down report has been sent.");
					} else {
						Log.e("Network",
								"Upload failed due to network problem with code "
										+ String.valueOf(response
												.getStatusLine()
												.getStatusCode()));
						Log.e("Network", "URL: "
								+ Constants.ADDRESS_FALLDOWN_REPORT);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			super.handleMessage(msg);
		}
	}
}
