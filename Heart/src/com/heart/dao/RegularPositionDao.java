package com.heart.dao;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import net.sf.json.JSONArray;


import com.heart.bean.RegularPosition;
import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class RegularPositionDao {

	public static final String POSITIONCOLLECTION = "positions";
	
	public void savePosition(RegularPosition position) throws UnknownHostException{
		
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(POSITIONCOLLECTION);
		
		BasicDBObject object = new BasicDBObject();
		object.put("oldmanphone", position.getOldmanPhone());
		object.put("thedate", position.getDate());
		object.put("area", position.getArea());
		object.put("longtitude", position.getLongtitude());
		object.put("latitude", position.getLatitude());
		object.put("degree", position.getDegree());
		object.put("picurl", position.getImgUrl());
		object.put("pm25", position.getPm25());
		object.put("tianqi", position.getTianqi());
		
		collection.insert(object);
		
		
	}
	
	public RegularPosition getHeartPositionMsgWithWeatherByOldManPhone(String phone) 
			throws UnknownHostException{
		
		RegularPosition regularPosition = 
				new RegularPosition();
		
	    DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(POSITIONCOLLECTION);
		
		
		BasicDBObject match = new BasicDBObject()
		              .append("$match", new BasicDBObject().append("oldmanphone", phone));
		
		BasicDBObject group =
				new BasicDBObject();
		group.put("$group", 
				new BasicDBObject().
				append("_id","$oldmanphone").
				append("lateast", new BasicDBObject("$max","$thedate")));
		
		
		ArrayList<DBObject> agg = new ArrayList<>();
		agg.add(match);
		agg.add(group);
		
		AggregationOutput aggregationOutput = collection.aggregate(agg);
		
        Iterator<DBObject> iterator = aggregationOutput.results().iterator();
        
        while(iterator.hasNext())
        {
        	
        	Long thatDate = (Long) iterator.next().get("lateast");
            
        	BasicDBObject query = new BasicDBObject().append("thedate", thatDate)
        			.append("oldmanphone", phone);
        	
        	BasicDBObject project = new BasicDBObject().append("longtitude", 1)
        			.append("latitude", 1).append("area", 1);
        	
        	DBCursor cursor = collection.find(query,project);
        	
        	while(cursor.hasNext()){
        		
        		DBObject dbObject = cursor.next();
        
        		regularPosition.setLongtitude((double) dbObject.get("longtitude"));
        		regularPosition.setLatitude((double) dbObject.get("latitude"));
        		System.out.println(dbObject.get("area"));
        	}
        }
		
		return regularPosition;
		
		
	}
	
	
	
	public ArrayList<RegularPosition> getBindOldman20Position(String[] oldmanPhone) throws UnknownHostException, UnsupportedEncodingException{
		
		 ArrayList<RegularPosition> regularPositions = new ArrayList<>();
		 
		  DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
			
		  DBCollection collection = db.getCollection(POSITIONCOLLECTION);
			
		 
		 BasicDBObject query = new BasicDBObject();
		 
		 query.append("oldmanphone", new BasicDBObject("$in",oldmanPhone));
		 
		 BasicDBObject project = new BasicDBObject();
		 project.append("thedate", 1).append("oldmanphone", 1)
		      .append("area", 1).append("picurl", 1)
		      .append("degree", 1).append("pm25", 1)
		      .append("tianqi", 1).append("_id", 0);
		 
		 BasicDBObject order = 
				 new BasicDBObject().append("thedate", -1);
		 
		 DBCursor cursor = collection.find(query, project).sort(order).limit(15);
		 
		 while(cursor.hasNext()){
			 
			 DBObject object = cursor.next();
			 
			 RegularPosition regularPosition = new RegularPosition();
			 
			 String area = object.get("area").toString();

			 regularPosition.setArea(area);
			 regularPosition.setDate((long) object.get("thedate"));
			 regularPosition.setDegree(object.get("degree").toString());
			 regularPosition.setImgUrl(object.get("picurl").toString());
			 regularPosition.setOldmanPhone(object.get("oldmanphone").toString());
			 regularPosition.setPm25(object.get("pm25").toString());
			 regularPosition.setTianqi(object.get("tianqi").toString());
			
			 
			 regularPositions.add(regularPosition);
		 }
		 
		 return regularPositions;
	}

	public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException {
		
		/*RegularPosition position = new RegularPositionDao().getHeartPositionMsgWithWeatherByOldManPhone("18729583733");
		
		System.out.println(JSONObject.fromObject(position));*/
		
		ArrayList<RegularPosition> positions = new RegularPositionDao().getBindOldman20Position(new String[]{"18729583733"});
		
		JSONArray array = JSONArray.fromObject(positions);
		System.out.println(array);
		
	}
}
