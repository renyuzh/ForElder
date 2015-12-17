package com.heart.service;

import java.net.UnknownHostException;

import net.sf.json.JSONObject;

import com.heart.bean.HeartMsg;
import com.heart.bean.HeartReturnBindMsg;
import com.heart.dao.OldManDao;
import com.heart.util.CONSTANTS;
import com.mongodb.util.JSON;

public class OldManService {

	public boolean register(String phone,String pwd,String userName){		
		try {
			new OldManDao().register(phone, pwd, userName);	
			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public String parseReturnBindMsgWithName(Object message) throws UnknownHostException{
		
		HeartReturnBindMsg heartReturnBindMsg = new HeartReturnBindMsg();
		
		JSONObject jsonObject = JSONObject.fromObject(message);
		
		String oldmanPhone = jsonObject.getString(CONSTANTS.FROMUSERNAME);
		
		
		heartReturnBindMsg.setCreateTime(jsonObject.getLong(CONSTANTS.CREATETIME));
		heartReturnBindMsg.setFromUserName(oldmanPhone);
		heartReturnBindMsg.setToUserName(jsonObject.getString(CONSTANTS.TOUSERNAME));
		heartReturnBindMsg.setMsgContent(jsonObject.getString(CONSTANTS.MSGCONTENT));
		heartReturnBindMsg.setOldmanName(new OldManDao().getOldManByPhone(oldmanPhone).getName());
		heartReturnBindMsg.setMsgType("returnbind");
		
		return JSONObject.fromObject(heartReturnBindMsg).toString();
	}
	
	public static void main(String[] args) throws UnknownHostException {
		
		/*HeartMsg heartMsg = new HeartMsg();
		
		heartMsg.setCreateTime(System.currentTimeMillis());
		heartMsg.setFromUserName("18729583733");
		heartMsg.setToUserName("110");
		heartMsg.setMsgContent("1");
		heartMsg.setMsgType("returnbind");
		
		System.out.println(new OldManService().parseReturnBindMsgWithName(JSONObject.fromObject(heartMsg)));*/
	}
	
}
