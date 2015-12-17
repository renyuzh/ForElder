package com.heart.service;

import java.net.UnknownHostException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.heart.bean.OldmanBean;
import com.heart.dao.GuardianDao;

public class GuardianService {

	public boolean register(String phone,String pwd,String userName){
		
		try {
			new GuardianDao().register(phone, pwd, userName);
			
			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
    public ArrayList<OldmanBean> initBinded(String jianhu) throws UnknownHostException{
    	
    	ArrayList<OldmanBean> oldmanBeans = new ArrayList<>();
    	
    	oldmanBeans = new GuardianDao().getBindedOldMan(jianhu);
    	
    	return oldmanBeans;
    }
    
    
   public static void main(String[] args) throws UnknownHostException {
	   
	System.out.println(JSONArray.fromObject(new GuardianService().initBinded("18789482356")));
	  
   }
}
