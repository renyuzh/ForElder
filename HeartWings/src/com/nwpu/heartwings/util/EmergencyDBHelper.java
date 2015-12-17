package com.nwpu.heartwings.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class EmergencyDBHelper extends SQLiteOpenHelper{

	public static final String CREATE_EMERGENCY_TABLE = 
			"create table emergency(_id integer primary key autoincrement,phone,name,oldmanphone)";
	
	public EmergencyDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_EMERGENCY_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		
	}

	
}
