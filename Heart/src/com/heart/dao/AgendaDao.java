package com.heart.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;


import com.heart.bean.HeartMsg;
import com.heart.listen.mina.MinaServerHandler;
import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.sun.swing.internal.plaf.metal.resources.metal;

public class AgendaDao {

	public static final String[] MSGTYPES = 
			 new String[]{"schedule_single_event","take_pills","birthday_event"};
	
	public ArrayList<HeartMsg> getMainAgenda(String[] phones)
			throws UnknownHostException {

		ArrayList<HeartMsg> heartMsgs = new ArrayList<HeartMsg>();

		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);

		DBCollection collection = db.getCollection(CONSTANTS.UNREADMESSAGE);

		BasicDBObject query = new BasicDBObject();
		query.append("msgType", new BasicDBObject("$in", MSGTYPES)).append("toUserName",
				new BasicDBObject("$in", phones));

		BasicDBObject project = new BasicDBObject();
		project.append("createTime", 1);
		project.append("toUserName", 1);
		project.append("msgContent", 1);
		project.append("msgType", 1);

		BasicDBObject orderBy = new BasicDBObject("createTime", -1);

		DBCursor cursor = collection.find(query, project).sort(orderBy)
				.limit(10);

		while (cursor.hasNext()) {

			HeartMsg heartMsg = new HeartMsg();

			DBObject object = cursor.next();

			
		//	heartMsg.setCreateTime((long) object.get("createTime"));

		    heartMsg.setMsgContent(object.get("msgContent").toString());
		    
		    heartMsg.setToUserName(object.get("toUserName").toString());
		    
		    heartMsg.setMsgType(object.get("msgType").toString());
		    
		    heartMsg.setMsgID(object.get("_id").toString());
		    
		    heartMsgs.add(heartMsg);

		}

		
		if(heartMsgs.size() < 10){
			
			collection = db.getCollection(CONSTANTS.READEDMESSAGE);
			cursor = collection.find(query, project).sort(orderBy)
					.limit(10 - heartMsgs.size());
			
			while (cursor.hasNext()) {

				HeartMsg heartMsg = new HeartMsg();

				DBObject object = cursor.next();
				
			//	heartMsg.setCreateTime((long) object.get("createTime"));

			    heartMsg.setMsgContent(object.get("msgContent").toString());
			    
			    heartMsg.setToUserName(object.get("toUserName").toString());
			    heartMsg.setMsgType(object.get("msgType").toString());
			    heartMsg.setMsgID(object.get("_id").toString());
			    
			    heartMsgs.add(heartMsg);

			}
			
		}
		
		return heartMsgs;
	}
	
	/*
	public static void main(String[] args) throws UnknownHostException {
		
		ArrayList<HeartMsg> heartMsgs = new AgendaDao().getMainAgenda(new String[]{"18729583733"});
		
		System.out.println(JSONArray.fromObject(heartMsgs));
	}*/
	
	public void deleteAgenda(String messageID,String oldmanphone,int clientMsgID) throws UnknownHostException
	{
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);

		DBCollection collection = db.getCollection(CONSTANTS.READEDMESSAGE);
		
		ObjectId objectId = new ObjectId(messageID);
		
		BasicDBObject condition = new BasicDBObject("_id",objectId);
		
		WriteResult writeResult = collection.remove(condition);
		
		System.out.println(writeResult.getN());
		
		if(writeResult.getN() > 0)
		{
			// ·¢ËÍMina
			
			HeartMsg heartMsg = new HeartMsg();
			heartMsg.setCreateTime(System.currentTimeMillis());
			heartMsg.setFromUserName("");
			heartMsg.setToUserName(oldmanphone);
			heartMsg.setMsgType("cancel_take_pills_event");
			heartMsg.setMsgContent(String.valueOf(clientMsgID));
			
			
			if(MinaServerHandler.activeSessionMap.containsKey(oldmanphone)){
				MinaServerHandler.activeSessionMap.get(oldmanphone).write(JSONObject.fromObject(heartMsg).toString());
			}
			else {
				MsgDao msgDao = new MsgDao();
				 msgDao.saveUnReadMsg(JSONObject.fromObject(heartMsg).toString());
			}
			
		}
		else {
			collection = db.getCollection(CONSTANTS.UNREADMESSAGE);
			
			collection.remove(condition);
		}
		
	}
	
	
	public void deleteSingleEvent(String _id) throws UnknownHostException{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);

		DBCollection collection = db.getCollection(CONSTANTS.READEDMESSAGE);
		
		ObjectId objectId = new ObjectId(_id);
		
		
		BasicDBObject condition = new BasicDBObject("_id",objectId);
		
		WriteResult writeResult = collection.remove(condition);
		
		if(writeResult.getN() == 0)
		{
            collection = db.getCollection(CONSTANTS.UNREADMESSAGE);
			
			collection.remove(condition);
		}
	}
	
	public static void main(String[] args) throws UnknownHostException {
		
		new AgendaDao().deleteSingleEvent("53ea029fc0827fe095e4d034");
	}
}
