package com.nwpu.heartwings.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class BindInfoDBHelper extends SQLiteOpenHelper {

	public static final String CREATE_TABLE_SQL = 
			 "create table binding (_id integer primary key autoincrement,phone,name,thisclient)";
	public BindInfoDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_TABLE_SQL);
		
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		
		  System.out.println("db version from " + oldV + " to " + newV);

	}

}
