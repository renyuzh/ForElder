package com.heart.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.heart.bean.HeartMsg;
import com.heart.bean.RegularShare;
import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


/**
 * @author CZP
 *
 */
public class MsgDao {

	public void saveMsg(Object msg, String collectionName){
		
		try {
			JSONObject jsonObject = JSONObject.fromObject(msg.toString());
			
			DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
			
			DBCollection collection = db.getCollection(collectionName);
			
			BasicDBObject object = new BasicDBObject();
			
			object.put(CONSTANTS.FROMUSERNAME, jsonObject.getString(CONSTANTS.FROMUSERNAME));
			object.put(CONSTANTS.TOUSERNAME, jsonObject.getString(CONSTANTS.TOUSERNAME));
			Date d = new Date(Long.valueOf(jsonObject.getString(CONSTANTS.CREATETIME)));
			object.put(CONSTANTS.CREATETIME, d);
			object.put(CONSTANTS.MSGTYPE, jsonObject.getString(CONSTANTS.MSGTYPE));
			object.put(CONSTANTS.MSGCONTENT, jsonObject.getString(CONSTANTS.MSGCONTENT));
			
	
			if(jsonObject.getString(CONSTANTS.MSGTYPE).equals(CONSTANTS.POSITION))
			{
				object.put(CONSTANTS.AREA, jsonObject.getString(CONSTANTS.AREA));
				object.put(CONSTANTS.DEGREES, jsonObject.getString(CONSTANTS.DEGREES));
				object.put(CONSTANTS.WEATHER, jsonObject.getString(CONSTANTS.WEATHER));
				object.put(CONSTANTS.DAY_PIC, jsonObject.getString(CONSTANTS.DAY_PIC));
			}
			
			collection.insert(object);
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param msg
	 * @throws UnknownHostException
	 */
	public void saveUnReadMsg(Object msg) 
	{
		
		try {
			JSONObject jsonObject = JSONObject.fromObject(msg.toString());
			
			DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
			
			DBCollection collection = db.getCollection(CONSTANTS.UNREADMESSAGE);
			
			BasicDBObject object = new BasicDBObject();
			
			object.put(CONSTANTS.FROMUSERNAME, jsonObject.getString(CONSTANTS.FROMUSERNAME));
			object.put(CONSTANTS.TOUSERNAME, jsonObject.getString(CONSTANTS.TOUSERNAME));
			Date d = new Date(Long.valueOf(jsonObject.getString(CONSTANTS.CREATETIME)));
			object.put(CONSTANTS.CREATETIME, d);
			object.put(CONSTANTS.MSGTYPE, jsonObject.getString(CONSTANTS.MSGTYPE));
			object.put(CONSTANTS.MSGCONTENT, jsonObject.getString(CONSTANTS.MSGCONTENT));
			
			collection.insert(object);
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * @param userName
	 * @return
	 * @throws UnknownHostException
	 */
	public List<HeartMsg> fetchUnReadMsg(String userName) throws UnknownHostException{
		
		List<HeartMsg> heartMsgs = new ArrayList<HeartMsg>();
		
        DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.UNREADMESSAGE);
		
		BasicDBObject query = new BasicDBObject();
		query.put(CONSTANTS.TOUSERNAME, userName);
		
		DBCursor cursor = collection.find(query);
		
		while(cursor.hasNext()){
			
			DBObject dbObject = cursor.next();
			
			HeartMsg heartMsg = new HeartMsg();
			
			heartMsg.setCreateTime(((java.util.Date) dbObject.get(CONSTANTS.CREATETIME)).getTime());
			heartMsg.setFromUserName((String) dbObject.get(CONSTANTS.FROMUSERNAME));
			heartMsg.setMsgContent((String) dbObject.get(CONSTANTS.MSGCONTENT));
			heartMsg.setMsgID(dbObject.get(CONSTANTS.ID).toString());
		    heartMsg.setMsgType((String) dbObject.get(CONSTANTS.MSGTYPE));
			heartMsg.setToUserName(userName);
			
			heartMsgs.add(heartMsg);
			
		}
		
		
		return heartMsgs;
	}

	/**
	 * @param msgID
	 * @param heartMsg
	 * @throws UnknownHostException 
	 */
	public void transferMsg(String msgID, HeartMsg heartMsg) throws UnknownHostException{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = null;
		BasicDBObject document = null;
		
		collection = db.getCollection(CONSTANTS.UNREADMESSAGE);
		ObjectId objectId = new ObjectId(msgID);
		document = new BasicDBObject();
		document.put(CONSTANTS.ID, objectId);
		
		collection.remove(document);
		
		DBCollection insertCollection = db.getCollection(CONSTANTS.READEDMESSAGE);
		
		BasicDBObject insertDbObject = new BasicDBObject();
		insertDbObject.put(CONSTANTS.FROMUSERNAME, heartMsg.getFromUserName());
		insertDbObject.put(CONSTANTS.TOUSERNAME, heartMsg.getToUserName());
		insertDbObject.put(CONSTANTS.CREATETIME, new Date(heartMsg.getCreateTime()));
		insertDbObject.put(CONSTANTS.MSGCONTENT, heartMsg.getMsgContent());
		insertDbObject.put(CONSTANTS.MSGTYPE, heartMsg.getMsgType());
		
		insertCollection.insert(insertDbObject);
		
	}
	
	
	public ArrayList<RegularShare> getLatestShare(String[] phones) throws UnknownHostException{
		
		ArrayList<RegularShare> regularShares = new ArrayList<>();
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.UNREADMESSAGE);
		
		BasicDBObject query = new BasicDBObject();
		query.append("msgType", "image")
		     .append("toUserName", new BasicDBObject("$in",phones));
		
		BasicDBObject project = new BasicDBObject();
		project.append("createTime", 1);
		project.append("toUserName", 1);
		project.append("msgContent", 1);
		
		BasicDBObject orderBy = new BasicDBObject("createTime",-1);
		
		DBCursor cursor = collection.find(query, project).sort(orderBy).limit(5);
		
		while(cursor.hasNext()){
			
			RegularShare regularShare = new RegularShare();
			
			DBObject object = cursor.next();
			
			Date date = ((Date)object.get("createTime"));
			
			regularShare.setDate(date.getTime());

			JSONArray array = JSONArray.fromObject(object.get("msgContent"));
			
			ArrayList<String> paths = new ArrayList<>();
			
			for(int i = 0; i < array.size(); i++){
				
				paths.add(array.getString(i));
			}
			
		    regularShare.setPicUrls(paths);
			
		    regularShare.setOldmanPhone(object.get("toUserName").toString());
		    
		    regularShares.add(regularShare);
		}
		
		if(regularShares.size() < 5)
		{
			collection = db.getCollection(CONSTANTS.READEDMESSAGE);
			
			cursor = collection.find(query, project).sort(orderBy).limit(5 - regularShares.size());
			
			while(cursor.hasNext()){
				
				RegularShare regularShare = new RegularShare();
				
				DBObject object = cursor.next();
				
				Date date = ((Date)object.get("createTime"));
				regularShare.setDate(date.getTime());

				JSONArray array = JSONArray.fromObject(object.get("msgContent"));
				
				ArrayList<String> paths = new ArrayList<>();
				
				for(int i = 0; i < array.size(); i++){
					
					paths.add(array.getString(i));
				}
				
			    regularShare.setPicUrls(paths);
				
			    regularShare.setOldmanPhone(object.get("toUserName").toString());
			    
			    regularShares.add(regularShare);
			}
		}
		
	//	System.out.println(JSONArray.fromObject(regularShares));
		
		return regularShares;
	}
	

}
