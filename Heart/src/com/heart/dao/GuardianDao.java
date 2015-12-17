package com.heart.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.types.ObjectId;

import com.heart.bean.OldmanBean;
import com.heart.util.CONSTANTS;
import com.heart.util.GetDBClient;
import com.heart.util.PwdSercurity;
import com.heart.util.TongjiUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class GuardianDao {

	/**
	 * @param phone
	 * @return
	 * @throws UnknownHostException
	 */
	public boolean hasSamePhone(String phone) throws UnknownHostException{
		
	    DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.GUARDIAN);
		
		BasicDBObject query = new BasicDBObject(CONSTANTS.GUARDIANPHONE,phone);
		
		DBCursor cursor = collection.find(query);
		
		if(cursor.hasNext())
		{
			return true;
		}
	
		return false;
	}
	
	public void register(String phone,String pwd,String userName) throws Exception{
		
		    DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
			
			DBCollection collection = db.getCollection(CONSTANTS.GUARDIAN);
			
			BasicDBObject object = new BasicDBObject();
			object.put(CONSTANTS.GUARDIANPHONE, phone);
			
			String codedPwd = PwdSercurity.encode(pwd);
			object.put(CONSTANTS.GUARDIANPWD, codedPwd);
			
			object.put(CONSTANTS.GUARDIANNAME, userName);
			
		    object.put(CONSTANTS.REGISTERDATE, Calendar.getInstance().getTime());
		    
		  //  object.put("bindings", null);
			
			collection.insert(object);
			
			TongjiUtil.addTongji(CONSTANTS.TONGJI, CONSTANTS.GUARDIAN, 1);
			
			
	}
	
	
	
	public boolean login(String phone,String pwd){
		
		try {
			String codedPwd = PwdSercurity.encode(pwd);
			
			DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
				
			DBCollection collection = db.getCollection(CONSTANTS.GUARDIAN);
			
			BasicDBObject query = new BasicDBObject();
			
			query.put(CONSTANTS.GUARDIANPHONE, phone);
			
			
		    BasicDBObject project = new BasicDBObject();
		    project.put(CONSTANTS.GUARDIANPWD, 1);
		    project.put(CONSTANTS.ID,0);
		    
			DBCursor cursor = collection.find(query,project);
			
			if(cursor.hasNext())
			{
				
				if(cursor.next().get(CONSTANTS.GUARDIANPWD).equals(codedPwd))
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
	 * bindings:[{oldmanid:xxx},{oldmanid:xxx}]
	 * @param oldman
	 * @param guardian
	 * @throws UnknownHostException 
	 */
	public void addBindForGuardian(String oldman, String guardian) throws UnknownHostException{
		
	
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		DBCollection collection = db.getCollection(CONSTANTS.OLDMAN);
		
		BasicDBObject query = new BasicDBObject();
		query.put(CONSTANTS.OLDMANPHONE, oldman);
		
		BasicDBObject project = new BasicDBObject();
		project.put(CONSTANTS.ID, 1);
		
	    ObjectId oId = (ObjectId) collection.findOne(query, project).get(CONSTANTS.ID);
	    
		
	    
	    collection = db.getCollection(CONSTANTS.GUARDIAN);
	    
	    query = new BasicDBObject();
	    query.put(CONSTANTS.GUARDIANPHONE, guardian);
	    
	    
	    project = new BasicDBObject();
	    project.put("bindings", 1);
	    project.put(CONSTANTS.ID, 0);
	    
	    BasicDBObject update = new BasicDBObject();

	    update.put("$push", new BasicDBObject("bindings", new BasicDBObject("oldmanid", oId)));
	    collection.update(query, update);
	    
	    
	}
	
	/**
	 * @param jianhu
	 * @return
	 * @throws UnknownHostException
	 */
	public ArrayList<OldmanBean> getBindedOldMan(String jianhu) throws UnknownHostException{
		
		ArrayList<OldmanBean> bindedList = new ArrayList<OldmanBean>();
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
	    DBCollection collection = db.getCollection(CONSTANTS.GUARDIAN);
		
	    BasicDBObject query = new BasicDBObject();
	    query.put(CONSTANTS.GUARDIANPHONE, jianhu);
	    
	    BasicDBObject project = new BasicDBObject();
	   
	    project.put("bindings.oldmanid", 1);
	    project.put(CONSTANTS.ID, 0);
	    
	    DBCursor cursor = collection.find(query, project);
	   
	    ArrayList<ObjectId> oldmanIds = new ArrayList<ObjectId>();
	    
	    try {
			while(cursor.hasNext())
			{ 
				
				JSONArray array = JSONArray.fromObject(cursor.next().get("bindings").toString());
				for(int i = 0; i < array.size(); i++)
				{
					String temp = array.getJSONObject(i).get("oldmanid").toString();
					oldmanIds.add(new ObjectId(JSONObject.fromObject(temp).getString("$oid")));
				}
			}
		} catch (Exception e) {
			
			System.out.println("Error in the read init bindings...");
			return bindedList;
			
		}
	    
	
	    
	    collection = db.getCollection(CONSTANTS.OLDMAN);
	    query = new BasicDBObject();
	   
	    project = new BasicDBObject();
	    project.put(CONSTANTS.OLDMANNAME, 1);
	    project.put(CONSTANTS.OLDMANPHONE, 1);
	    project.put(CONSTANTS.ID,1);
	    
	    for(ObjectId objectId : oldmanIds)
	    {
	    	query.put(CONSTANTS.ID, objectId);
	    	
	    	
	    	OldmanBean oldmanBean = new OldmanBean();
	    	
	    	DBObject dbObject = collection.findOne(query, project);
	    	
	    	oldmanBean.setName(dbObject.get(CONSTANTS.OLDMANNAME).toString());
	    	//oldmanBean.setObjectId((ObjectId) dbObject.get(CONSTANTS.ID));
	    	oldmanBean.setPhone(dbObject.get(CONSTANTS.OLDMANPHONE).toString());
	    	
	    	bindedList.add(oldmanBean);
	    }
	    
		return bindedList;
	}
	
	
	
	public void changepwd(String guardian,String newPwd) throws Exception{
		
		DB db = GetDBClient.GetClient().getDB(CONSTANTS.DBNAME);
		
		DBCollection collection = db.getCollection(CONSTANTS.GUARDIAN);
		
		
		String md5Pwd = PwdSercurity.encode(newPwd);
		
		BasicDBObject query = new BasicDBObject("phone",guardian);
		
		BasicDBObject update = new BasicDBObject()
		          .append("$set", new BasicDBObject("pwd",md5Pwd));
		
		collection.update(query, update);
		
	}
	
	public static void main(String[] args) throws Exception {
		
        
		new GuardianDao().getBindedOldMan("187772112");
	}
}
