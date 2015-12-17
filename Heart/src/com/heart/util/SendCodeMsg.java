package com.heart.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendCodeMsg {

	public static String sendMsg(String phone, String code) {

		try {
			String postData = CONSTANTS.MSGACCOUNT
					+ phone
					+ CONSTANTS.CONTENT
					+ java.net.URLEncoder.encode(CONSTANTS.MSG1 + code
							+ CONSTANTS.MSG2, "utf-8");

			URL url = new URL(CONSTANTS.MSGURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Length", "" + postData.length());
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream(), "UTF-8");
			out.write(postData);
			out.flush();
			out.close();
			
			 if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                System.out.println("connect failed!");
	                return "error";
	            }
	           
	            String line, result = "";
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	            while ((line = in.readLine()) != null) {
	                result += line + "\n";
	            }
	            in.close();
	            return result;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "error";
	}
	
  
}
