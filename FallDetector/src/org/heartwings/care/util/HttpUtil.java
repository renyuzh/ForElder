package org.heartwings.care.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.heartwings.care.Constants;

import android.os.Message;
import android.util.Log;

public class HttpUtil {

	public static final String BASEURL = Constants.ADDRESS_PREFIX + "/";

	public static HttpClient httpClient = new DefaultHttpClient();

	public static Message getRequst(final String url)
			throws InterruptedException, ExecutionException,
			ClientProtocolException, IOException {
		Message message = new Message();
		message.what = CONSTANTS.HTTPGET;
		HttpGet get = new HttpGet(url);
		HttpResponse response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			message.obj = EntityUtils.toString(response.getEntity());
		} else {
			message.obj = null;
		}

		return message;
	}

	public static String postRequest(final String url,
			final Map<String, String> rawParams) {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		HttpResponse response;
		try {
			post.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
			return CONSTANTS.LOGIN_ERR_TAG;
		}

		if (response.getStatusLine().getStatusCode() == 200) {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
				return CONSTANTS.LOGIN_ERR_TAG;
			}
		} else {
			Log.e("Register", "Server responses not 200");
			return CONSTANTS.LOGIN_ERR_TAG;
		}
	}
}
