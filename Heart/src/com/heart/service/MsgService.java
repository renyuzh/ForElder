package com.heart.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.mina.core.session.IoSession;

import sun.util.logging.resources.logging;

import com.heart.bean.HeartMsg;
import com.heart.bean.HeartPositionMsg;
import com.heart.bean.HeartPositionMsgWithWeather;
import com.heart.dao.MsgDao;
import com.heart.util.CONSTANTS;
import com.sun.security.ntlm.Client;

public class MsgService {

	public static final String AREA = "area";
	
	public static final String URLBASE = "http://api.map.baidu.com/telematics/v3/weather?location=";
	public static final String URLTWO = "&output=json&ak=";
	
	public static final String BAIDUAK = "bT87sfQIb8bXLPgQcMShma5F";
	
	public static final String LONGITUDE = "longitude";
	public static final String LANTITUDE = "latitude";
	
	public static final String GETMETHOD = "GET";
	
	
	public static final String BAIDUSTATUS = "status";
	public static final String SUCCESS = "success";
	public static final String BAIDURESULTS = "results";
	public static final String BAIDUWEATHER = "weather_data";
	
	public static final String BAIDUDATE = "date";
	
	public static final String BAIDUPIC = "dayPictureUrl";
	
	public static void sendMsg(List<HeartMsg> heartMsgs, IoSession session) throws UnknownHostException
	{
		
		MsgDao msgDao = new MsgDao();
		for(HeartMsg heartMsg : heartMsgs)
		{
			if(!session.isConnected())
			{
				break;
			}
			
			JSONObject jsonObject = JSONObject.fromObject(heartMsg);
			session.write(jsonObject.toString());
			
			
			msgDao.transferMsg(heartMsg.getMsgID(), heartMsg);
			
		}
	}
	
	/**
	 * @param rawPositionMsg
	 * @return
	 * @throws IOException
	 */	
	public String detailPositionMsg(Object rawPositionMsg) throws IOException{
		
		JSONObject rawObject = JSONObject.fromObject(rawPositionMsg);
		
		HeartPositionMsgWithWeather heartPositionMsgWithWeather = new HeartPositionMsgWithWeather();
		
		heartPositionMsgWithWeather.setCreateTime(rawObject.getLong(CONSTANTS.CREATETIME));
		heartPositionMsgWithWeather.setFromUserName(rawObject.getString(CONSTANTS.FROMUSERNAME));
		heartPositionMsgWithWeather.setToUserName(rawObject.getString(CONSTANTS.TOUSERNAME));
		heartPositionMsgWithWeather.setMsgType(CONSTANTS.POSITION);
		
		heartPositionMsgWithWeather.setArea(rawObject.getString(AREA));
		
		String longitude = rawObject.getString(LONGITUDE);
		String lantitude = rawObject.getString(LANTITUDE);
		
	
		
		StringBuilder sb = new StringBuilder();
		sb.append(URLBASE+longitude+","+lantitude+URLTWO+BAIDUAK);
		
      
		RequestConfig globalConfig = RequestConfig.custom().
				setCookieSpec(CookieSpecs.BEST_MATCH).build();
		
        CloseableHttpClient client = HttpClients.custom().
        		setDefaultRequestConfig(globalConfig).build();
        
        RequestConfig localConfig = RequestConfig.copy(globalConfig)
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .build();
        HttpGet get = new HttpGet(sb.toString());
        get.setConfig(localConfig);
        
		CloseableHttpResponse response = client.execute(get);
		
		String weatherDetail = EntityUtils.toString(response.getEntity());
		
		JSONObject jsonObject = JSONObject.fromObject(weatherDetail);
		
		System.out.println(weatherDetail);
		
		if(jsonObject.getString(BAIDUSTATUS).equals(SUCCESS))
		{
			JSONObject weatherJsonObject = jsonObject.getJSONArray(BAIDURESULTS).getJSONObject(0)
					.getJSONArray(BAIDUWEATHER).getJSONObject(0);
		
			String wendu = weatherJsonObject.getString(BAIDUDATE);
			
			int positionBracket = wendu.indexOf("(");
			
			heartPositionMsgWithWeather.setDegrees(wendu.substring(positionBracket+1,wendu.length()-1));
			
			heartPositionMsgWithWeather.setDayPicUrl(weatherJsonObject.getString(BAIDUPIC));
			
			heartPositionMsgWithWeather.setWeather(weatherJsonObject.getString("weather"));
		
			heartPositionMsgWithWeather.setPm25(jsonObject.getJSONArray(BAIDURESULTS).getJSONObject(0)
					.getString("pm25"));
			
		}
	
		get.releaseConnection();
	    
		
		return JSONObject.fromObject(heartPositionMsgWithWeather).toString();
	}

	

	
	public static void main(String[] args) throws IOException {
		
		HeartPositionMsg heartPositionMsg = new HeartPositionMsg();
		heartPositionMsg.setArea("shanghai");
		heartPositionMsg.setCreateTime(System.currentTimeMillis());
		heartPositionMsg.setFromUserName("123");
		heartPositionMsg.setToUserName("456");
		heartPositionMsg.setLongitude(108.95000);
		heartPositionMsg.setLatitude(34.26667);
		heartPositionMsg.setMsgType(CONSTANTS.POSITION);
		
		
		new MsgService().detailPositionMsg(JSONObject.fromObject(heartPositionMsg).toString());
		
		
		
		
	}
}
