package com.heart.admin;

import java.net.UnknownHostException;
import java.util.ArrayList;


import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.heart.util.HeartDateUtil;
import com.heart.util.PwdSercurity;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class AdminDao {

	public static String ADMIN = "admincollection";
	public static String ADMINPWD = "adminpwd";
	public static String ADMINNAME = "adminname";


	
	public boolean adminLogin(String pwd, String name) {

		try {
			DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);

			DBCollection collection = db.getCollection(ADMIN);

			BasicDBObject query = new BasicDBObject();

			query.put(ADMINNAME, name);

			BasicDBObject project = new BasicDBObject();

			project.put(ADMINPWD, 1);
			project.put(CONSTANTS.ID, 1);

			DBCursor cursor = collection.find(query, project);

			String realPwd = PwdSercurity.encode(pwd);

			if (cursor.hasNext()) {
				if (cursor.next().get(ADMINPWD).equals(realPwd)) {
					return true;
				}

			}

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}

	}
	
	public void register(String name, String pwd) throws Exception{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);

		DBCollection collection = db.getCollection(ADMIN);
		
		BasicDBObject object = new BasicDBObject();
		
		String realPwd = PwdSercurity.encode(pwd);
		
		object.put(ADMINNAME, name);
		object.put(ADMINPWD, realPwd);
		
		collection.insert(object);
		
	}
	

	public int getTodayMsg() throws UnknownHostException{
		
		int result = 0;
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collectionReadMsg = db.getCollection(CONSTANTS.UNREADMESSAGE);
		
		BasicDBObject query = new BasicDBObject();
		
		query.put(CONSTANTS.CREATETIME, new BasicDBObject("$gt", HeartDateUtil.getStartDateOfToday()));
		
		result += collectionReadMsg.count(query);
		
		collectionReadMsg = db.getCollection(CONSTANTS.READEDMESSAGE);
		
		result += collectionReadMsg.count(query);
		
		return result;
	}
	

	public int getTodayFan() throws UnknownHostException{
		
		int result = 0;
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.GUARDIAN);
		
		
		BasicDBObject query = new BasicDBObject();
		
		query.put(CONSTANTS.REGISTERDATE, new BasicDBObject("$gt",HeartDateUtil.getStartDateOfToday()));
		
		result += collection.count(query);
		
		collection = db.getCollection(CONSTANTS.OLDMAN);
		
		result += collection.count(query);
		
		return result;
	}
	
	
	public int getAllFanNum() throws UnknownHostException{
		
		int result = 0;
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.TONGJI);
		
		BasicDBObject query = new BasicDBObject();
		query.put(CONSTANTS.TONGJI_FAKENAME, new BasicDBObject("$in", new String[]{CONSTANTS.OLDMAN,CONSTANTS.GUARDIAN}));
		
		BasicDBObject project = new BasicDBObject();
		project.put(CONSTANTS.TONGJI_ALL, 1);
		project.put(CONSTANTS.ID, 0);
		
		DBCursor cursor =collection.find(query, project);
		while(cursor.hasNext())
		{
			result += ((Double)cursor.next().get(CONSTANTS.TONGJI_ALL)).intValue();
		}
		
		return result;
	}
	
	
	public ArrayList<String> getAllPhones(String ... strings ) throws UnknownHostException{
		
		ArrayList<String> phoneList = new ArrayList<String>();
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = null;
		
		DBCursor cursor = null;
		
		BasicDBObject object = new BasicDBObject();
		object.put(CONSTANTS.OLDMANPHONE, 1);
		object.put(CONSTANTS.ID, 0);
		
		for(String collectionName : strings)
		{
			collection = db.getCollection(collectionName);
			
			cursor = collection.find(null,object);
			
			while(cursor.hasNext())
			{
				// CONSTANTS.GUARDIANPHONE Ò²ÊÇ phone
				String s = cursor.next().get(CONSTANTS.OLDMANPHONE).toString();
				phoneList.add(s);
				
			}
		}
		
		cursor.close();
		
		return phoneList;
	}
	

	
}
