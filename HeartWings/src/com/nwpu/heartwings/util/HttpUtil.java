package com.nwpu.heartwings.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Message;
import android.util.Log;

public class HttpUtil {

	 public static final String BASEURL = "http://114.215.122.96/Heart/";
//	public static final String BASEURL = "http://10.128.52.177:8080/Heart/";
	

	public static Message getRequst(final String url)
			throws InterruptedException, ExecutionException {

		FutureTask<Message> task = new FutureTask<Message>(
				new Callable<Message>() {

					@Override
					public Message call() throws Exception {

						Message message = new Message();
						message.what = CONSTANTS.HTTPGET;
						HttpGet get = new HttpGet(url);
						HttpClient httpClient = new DefaultHttpClient();
						HttpResponse response = httpClient.execute(get);
						if (response.getStatusLine().getStatusCode() == 200) {

							message.obj = EntityUtils.toString(response
									.getEntity());
						} else {
							message.obj = null;
						}

						return message;
					}

				});
		new Thread(task).start();

		return task.get();
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

			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(post);

		} catch (Exception e) {

			return CONSTANTS.LOGIN_ERR_TAG;
		}

		if (response.getStatusLine().getStatusCode() == 200) {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				return CONSTANTS.LOGIN_ERR_TAG;
			}
		} else
			return CONSTANTS.LOGIN_ERR_TAG;
	}

	public static String postRawRequest(String url, List<NameValuePair> params) {

		HttpPost post = new HttpPost(url);

		HttpResponse response;

		try {
			post.setEntity(new UrlEncodedFormEntity(params));

			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {

				return EntityUtils.toString(response.getEntity());

			} else {
				return CONSTANTS.LOGIN_ERR_TAG;
			}
		} catch (ParseException | IOException e) {
		
			e.printStackTrace();
		}
		finally{
			
			
		}
		return url;

	}
}
