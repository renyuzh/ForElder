package com.heart.admin;

import java.net.UnknownHostException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.heart.bean.EmergencyPhone;
import com.heart.bean.HeartMsg;
import com.heart.bean.HeartNews;
import com.heart.dao.MsgDao;
import com.heart.listen.mina.MinaServerHandler;
import com.heart.util.CONSTANTS;



public class AdminService {


	public static int SINGLESEND = 5000;
	
	public int getTodayMsg() {

		try {
			int result = new AdminDao().getTodayMsg();
			return result;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getTodayFan(){
		
		try {
			int result = new AdminDao().getTodayFan();
			return result;
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getAllFan(){
		
		try {
			int result = new AdminDao().getAllFanNum();
			return result;
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void sendAll(String content,String ... collections) throws UnknownHostException{
		
		HeartMsg heartMsg = new HeartMsg();
		
		heartMsg.setCreateTime(System.currentTimeMillis());
		heartMsg.setMsgContent(content);
		heartMsg.setMsgType(CONSTANTS.SYSTEM);
		heartMsg.setFromUserName("");
	
		
		ArrayList<String> phones = new AdminDao().getAllPhones(collections);
		
		int size = phones.size();
		
		int count = (int) Math.ceil((size / (SINGLESEND + 0.0)));
		
		for(int i = 0; i < count; i++)
		{
	        new SendThread(i * SINGLESEND, phones, size, heartMsg).start();
		}   
		
	}
	
	private class SendThread extends Thread{
		   
		public int start;
		public ArrayList<String> phones;
		public int size;
		public HeartMsg heartMsg;
		public SendThread(int start, ArrayList<String> phones,int size,
				HeartMsg heartMsg) {
			super();
			this.start = start;
			this.phones = phones;
			this.size = size;
			this.heartMsg = heartMsg;
		}
		
		@Override
		public void run(){
			
		    MsgDao msgDao = new MsgDao();
			for(int i = start; (i < start + SINGLESEND) && (i < size); i++)
			{
				
				String phone = phones.get(i);
			//	System.out.println(i + " "  + phone);
				heartMsg.setToUserName(phone);	
				String msg = JSONObject.fromObject(heartMsg).toString();

				if(MinaServerHandler.activeSessionMap.containsKey(phone))
				{
					//System.out.println("this guy online...");
					MinaServerHandler.activeSessionMap.get(phone).write(msg);
					
					
				}
				
		
				else {
					//System.out.println("save in db...");
					 msgDao.saveUnReadMsg(msg);
				}
			}
		}
		
	}
	
	/**
	 * @param heartNews
	 * @throws UnknownHostException 
	 */
	public void pushNews(HeartNews heartNews) throws UnknownHostException{
		
		HeartMsg heartMsg = new HeartMsg();
		
		heartMsg.setCreateTime(System.currentTimeMillis());
		
		heartMsg.setFromUserName("");
		
		heartMsg.setMsgContent(JSONObject.fromObject(heartNews).toString());
		
		heartMsg.setMsgType(CONSTANTS.SYSTEM);
		
		ArrayList<String> phones = new AdminDao().getAllPhones(CONSTANTS.GUARDIAN);
		
		
        int size = phones.size();
		
		int count = (int) Math.ceil((size / (SINGLESEND + 0.0)));
		
		
		for(int i = 0; i < count; i++)
		{
			new SendThread(i * SINGLESEND, phones, size, heartMsg).start();
		}
		
	}
	

	
	public static void main(String[] args) {
		
		HeartMsg heartMsg = new HeartMsg();
		heartMsg.setCreateTime(System.currentTimeMillis());
		heartMsg.setFromUserName("");
		heartMsg.setToUserName("18729583733");
		
		ArrayList<String> paths = new ArrayList<>();
		paths.add("/heartUpload/3.jpg");
		
		
		heartMsg.setMsgContent(JSONArray.fromObject(paths).toString());
		
		heartMsg.setMsgType("image");
		
		
		String msg = JSONObject.fromObject(heartMsg).toString();
		
		
		HeartMsg phone = new HeartMsg();
		
		phone.setFromUserName("");
		phone.setToUserName("18729583733");
		
		ArrayList<EmergencyPhone> emergencyPhones = new ArrayList<>();
		
		EmergencyPhone emergencyPhone = new EmergencyPhone();
		emergencyPhone.setName("hello");
		emergencyPhone.setPhone("18789482356");
		
		emergencyPhones.add(emergencyPhone);
		
		phone.setMsgType("emergency_list");
		phone.setCreateTime(System.currentTimeMillis());
		
		phone.setMsgContent(JSONArray.fromObject(emergencyPhones).toString());
		
		
		
		new MsgDao().saveUnReadMsg(msg);
		
	     //  new MsgDao().saveUnReadMsg(JSONObject.fromObject(phone).toString());
		
	}

}
