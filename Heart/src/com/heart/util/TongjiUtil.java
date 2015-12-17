package com.heart.util;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class TongjiUtil {

	public static String INC = "$inc";
	
	public static String ALL = "all";
	
	public static void addTongji(String collection, String fakename, int add) throws UnknownHostException{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		 
		DBCollection tongjiCollection = db.getCollection(collection);
		
		BasicDBObject criteria = new BasicDBObject();
		
		criteria.put(CONSTANTS.TONGJI_FAKENAME, fakename);
		
		BasicDBObject action = new BasicDBObject();
		action.put(INC, new BasicDBObject(ALL, add));
		
		tongjiCollection.update(criteria, action);
		
		
		
	}
}
