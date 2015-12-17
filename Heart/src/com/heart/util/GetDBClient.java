package com.heart.util;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class GetDBClient {

	private static MongoClient mongoClient = null;
	
	private static GetDBClient getDBClientInstance = null;
	
	private GetDBClient() throws UnknownHostException{
		
		mongoClient = new MongoClient(CONSTANTS.SERVERIP, CONSTANTS.MONGOPORT);
	}
	
	public static MongoClient GetClient() throws UnknownHostException{
		
		if(getDBClientInstance == null)
		{
			getDBClientInstance = new GetDBClient();
		}
		
		return mongoClient;
	}
	
	
}
