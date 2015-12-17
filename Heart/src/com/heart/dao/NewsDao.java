package com.heart.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import com.heart.bean.HeartNews;
import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class NewsDao {
	
	
	public static final int NEWS_COUNT = 8;

	public void saveNews(HeartNews heartNews) throws UnknownHostException{
		
        DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection("pushnews");
		
		
		BasicDBObject object = new BasicDBObject();
		
		object.put("title", heartNews.getTitle());
		object.put("createTime", System.currentTimeMillis());
		object.put("content", heartNews.getNewsContent());
		object.put("abstract", heartNews.getNewsAbstract());
		object.put("imgpath", heartNews.getNewsImg());
		object.put("readcount", 0);
		
		collection.insert(object);
	
	}
	
	
	public ArrayList<HeartNews> fetchNews(long date) throws UnknownHostException{
		
        DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection("pushnews");
		
        ArrayList<HeartNews> heartNewsList = new ArrayList<>();
        
        BasicDBObject query = new BasicDBObject("createTime",new BasicDBObject("$gt",date));
        
        BasicDBObject orderyBy = new BasicDBObject("createTime",-1);
        DBCursor cursor = collection.find(query).sort(orderyBy).limit(NEWS_COUNT);
        
        while(cursor.hasNext()){
        	
        	DBObject object = cursor.next();
        	
        	HeartNews heartNews = new HeartNews();
        	
        	heartNews.setNewsContent(object.get("content").toString());
        	heartNews.setTitle(object.get("title").toString());
        	heartNews.setNewsImg(object.get("imgpath").toString());
        	heartNews.setId(object.get("_id").toString());
        	heartNews.setNewsAbstract(object.get("abstract").toString());
        	heartNewsList.add(heartNews);
        	
        }
        
        return heartNewsList;
	}
	
	public static void main(String[] args) throws UnknownHostException {
		
		new NewsDao().fetchNews(System.currentTimeMillis());
	}
}
