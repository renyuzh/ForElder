package com.heart.dao;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;

import com.heart.bean.OldmanBean;
import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.heart.util.PwdSercurity;
import com.heart.util.TongjiUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class OldManDao {

	
	/**
	 * @param phone
	 * @return
	 * @throws UnknownHostException
	 */
	public boolean hasSamePhone(String phone) throws UnknownHostException
	{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.OLDMAN);
		
		BasicDBObject query = new BasicDBObject(CONSTANTS.OLDMANPHONE,phone);
		
		DBCursor cursor = collection.find(query);
		
		if(cursor.hasNext())
		{
			return true;
		}
	
		return false;
	}
	
	public void register(String phone,String pwd,String userName) throws Exception{
		
        DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.OLDMAN);
		
		BasicDBObject object = new BasicDBObject();
		object.put(CONSTANTS.OLDMANPHONE, phone);
		
		String codedPwd = PwdSercurity.encode(pwd);
		object.put(CONSTANTS.OLDMANPWD, codedPwd);
		
		object.put(CONSTANTS.OLDMANNAME, userName);
		
		object.put(CONSTANTS.REGISTERDATE, Calendar.getInstance().getTime());
		
		object.put("jianhuren", null);
		
		collection.insert(object);
		
		
		TongjiUtil.addTongji(CONSTANTS.TONGJI, CONSTANTS.OLDMAN, 1);
		
	}
	
	
	public boolean login(String phone,String pwd)
	{
		try {
			String codedPwd = PwdSercurity.encode(pwd);
			
			DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
				
			DBCollection collection = db.getCollection(CONSTANTS.OLDMAN);
			
			BasicDBObject query = new BasicDBObject();
			
			query.put(CONSTANTS.OLDMANPHONE, phone);
			
			
		    BasicDBObject project = new BasicDBObject();
		    project.put(CONSTANTS.OLDMANPWD, 1);
		    project.put(CONSTANTS.ID,0);
		    
			DBCursor cursor = collection.find(query,project);
			
			if(cursor.hasNext())
			{
				
				if(cursor.next().get(CONSTANTS.OLDMANPWD).equals(codedPwd))
				{
					return true;
				}
			}
			
			return false;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * @param msg
	 * @throws UnknownHostException 
	 */
	public void bindForOldMan(String oldman,String guardian) throws UnknownHostException{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.GUARDIAN);
		

		BasicDBObject query = new BasicDBObject();
		query.put(CONSTANTS.GUARDIANPHONE, guardian);
		
		BasicDBObject project = new BasicDBObject();
		project.put(CONSTANTS.ID, 1);
		
		DBObject guardianID = collection.findOne(query, project);
		
		ObjectId gId = (ObjectId) guardianID.get(CONSTANTS.ID);
		

		collection = db.getCollection(CONSTANTS.OLDMAN);
		query = new BasicDBObject();
		query.put(CONSTANTS.OLDMANPHONE, oldman);
		
		DBObject update = new BasicDBObject();
		update.put("$set", new BasicDBObject("jianhuren", gId));
		
		collection.update(query, update);
		
		
	}
	
	/**
	 * @return
	 * @throws UnknownHostException 
	 */
	public ArrayList<OldmanBean> getUnBindOldman() throws UnknownHostException{
		
		ArrayList<OldmanBean> unBindList = new ArrayList<OldmanBean>();
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		DBCollection collection = db.getCollection(CONSTANTS.OLDMAN);
		
		BasicDBObject query = new BasicDBObject();
		query.put("jianhuren", null);
		
		BasicDBObject project = new BasicDBObject();
		project.put(CONSTANTS.OLDMANPHONE, 1);
		project.put(CONSTANTS.ID, 1);
		project.put(CONSTANTS.OLDMANNAME, 1);
		project.put("jianhuren", 1);
		
		DBCursor cursor = collection.find(query);
		while(cursor.hasNext())
		{
			OldmanBean oldmanBean = new OldmanBean();
			
			DBObject dbObject = cursor.next();
			oldmanBean.setBindman(null);
			oldmanBean.setName(dbObject.get(CONSTANTS.OLDMANNAME).toString());
			oldmanBean.setObjectId((ObjectId) dbObject.get(CONSTANTS.ID));
			oldmanBean.setPhone(dbObject.get(CONSTANTS.OLDMANPHONE).toString());
		
		}
		
		return unBindList;
	}
	
	
	public OldmanBean searchForUnBindOldByPhone(String phone) throws UnknownHostException{
		
		OldmanBean oldmanBean = new OldmanBean();
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		DBCollection collection = db.getCollection(CONSTANTS.OLDMAN);
		
		BasicDBObject query = new BasicDBObject();
		query.put("jianhuren", null);
		query.put(CONSTANTS.OLDMANPHONE, phone);
		
		BasicDBObject project = new BasicDBObject();
		
		project.put(CONSTANTS.ID, 1);
		project.put(CONSTANTS.OLDMANNAME, 1);
		
		DBObject dbObject = collection.findOne(query, project);
		
		if(dbObject != null)
		{
			oldmanBean.setBindman(null);
			oldmanBean.setName(dbObject.get(CONSTANTS.OLDMANNAME).toString());
			oldmanBean.setPhone(phone);
			oldmanBean.setObjectId((ObjectId) dbObject.get(CONSTANTS.ID));
		}
		
		return oldmanBean;
	}
	
	public OldmanBean getOldManByPhone(String phone) throws UnknownHostException{
		
		OldmanBean oldmanBean = new OldmanBean();
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		DBCollection collection = db.getCollection(CONSTANTS.OLDMAN);
		
		BasicDBObject query = new BasicDBObject();
		query.put(CONSTANTS.OLDMANPHONE, phone);
		
		BasicDBObject project = new BasicDBObject();
		
		project.put(CONSTANTS.ID, 1);
		project.put(CONSTANTS.OLDMANNAME, 1);
		
		DBObject dbObject = collection.findOne(query, project);
		
		if(dbObject != null)
		{
			oldmanBean.setName(dbObject.get(CONSTANTS.OLDMANNAME).toString());
			oldmanBean.setPhone(phone);
			oldmanBean.setObjectId((ObjectId) dbObject.get(CONSTANTS.ID));
		}
		
		return oldmanBean;
		
	}
	
	public static void main(String[] args) throws Exception {
		
		/*  DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		  
		  DBCollection collection = db.getCollection(CONSTANTS.TONGJI);
		  
		  BasicDBObject criteria = new BasicDBObject();
		  criteria.put("fakename", "oldman");
		  
		  BasicDBObject action = new BasicDBObject();
		  action.put("$inc", new BasicDBObject("all", 1));
		  
		  collection.update(criteria, action);*/
		 
		  
	//	new OldManDao().register("8844", "123", "czp");
		
		//new OldManDao().bindForOldMan("157820155", "5512348");
		
		//new OldManDao().getUnBindOldman();
		
		OldmanBean oldmanBean = new OldManDao().searchForUnBindOldByPhone("18789888");
		
		JSONObject jsonObject = JSONObject.fromObject(oldmanBean);
		
		System.out.println(jsonObject.toString());
		
	}
}
