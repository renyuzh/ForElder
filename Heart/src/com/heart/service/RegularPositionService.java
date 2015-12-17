package com.heart.service;


import net.sf.json.JSONObject;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.heart.bean.RegularPosition;

public class RegularPositionService {

	public static final String BAIDUBASEURL = "http://api.map.baidu.com/telematics/v3/weather?location=";
	
	public static final String URLPARA = "&output=json&ak=";
	
	public static final String BAIDUKEY = "bT87sfQIb8bXLPgQcMShma5F";
	
	
	
	public boolean getPositionWithWeather(RegularPosition position){
		
		String longtitude = String.valueOf(position.getLongtitude());
		String latitude = String.valueOf(position.getLatitude());
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(BAIDUBASEURL + longtitude + "," + latitude + URLPARA + BAIDUKEY);
		
		RequestConfig globalConfig = RequestConfig.custom().
				setCookieSpec(CookieSpecs.BEST_MATCH).build();
		
        CloseableHttpClient client = HttpClients.custom().
        		setDefaultRequestConfig(globalConfig).build();
        
        RequestConfig localConfig = RequestConfig.copy(globalConfig)
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .build();
        
        HttpGet get = new HttpGet(sb.toString());
        get.setConfig(localConfig);
        
		try {
			CloseableHttpResponse response = client.execute(get);
			
			String weatherDetail = EntityUtils.toString(response.getEntity());
			
			System.out.println("return weather detail : " + weatherDetail);
			
			JSONObject jsonObject = JSONObject.fromObject(weatherDetail);
			
			
			if(jsonObject.getString("status").equals("success"))
			{
				JSONObject weatherJsonObject = jsonObject.getJSONArray("results").getJSONObject(0)
						.getJSONArray("weather_data").getJSONObject(0);
				
				String wendu = weatherJsonObject.getString("date");
				
				int positionBracket = wendu.indexOf("£º");
				
				position.setDegree(wendu.substring(positionBracket+1,wendu.length()-1));
				
				position.setImgUrl(weatherJsonObject.getString("dayPictureUrl"));
				
				position.setTianqi(weatherJsonObject.getString("weather"));
				
				position.setPm25(jsonObject.getJSONArray("results").getJSONObject(0)
					.getString("pm25"));
				
				
				return true;
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		
		RegularPosition position = new RegularPosition();
		position.setArea("xian City");
		position.setDate(5220054848L);
		position.setLongtitude(108.95000);
		position.setLatitude(34.26667);
		
		new RegularPositionService().getPositionWithWeather(position);
		
		System.out.println(JSONObject.fromObject(position));
	
	}
}
