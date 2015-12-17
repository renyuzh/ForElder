package com.heart.dao;

import java.net.UnknownHostException;
import java.util.Date;

import com.heart.bean.FallRecordBean;
import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class FallDao {

	public void saveFallRecord(FallRecordBean fallRecord) throws UnknownHostException{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection("fallrecord");
		
		BasicDBObject dbObject =
				 new BasicDBObject().append("oldmanphone", fallRecord.getOldmanPhone())
				 .append("falldate", fallRecord.getDate());
		
		collection.insert(dbObject);
		
		
	} 
}
