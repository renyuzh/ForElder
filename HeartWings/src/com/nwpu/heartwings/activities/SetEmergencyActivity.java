package com.nwpu.heartwings.activities;


import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.heart.bean.EmergencyPhone;
import com.heart.bean.HeartMsg;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.listview.SlideCutListView;
import com.nwpu.heartwings.listview.SlideCutListView.RemoveDirection;
import com.nwpu.heartwings.listview.SlideCutListView.RemoveListener;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.provider.ContactsContract;

@SuppressLint("HandlerLeak")
public class SetEmergencyActivity extends ActionBarActivity implements RemoveListener{

	public static final int PICK_CONTRACT = 0x8;
	
	public SlideCutListView listView;
	
	SimpleCursorAdapter cursorAdapter;
	Cursor cursor;
	
	String oldmanphone;
	
	private HashMap<Integer, Integer> map = new HashMap<>();
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 0x9)
			{
				System.out.println("run return 0x9...");
				cursor = LocalFileUtil.fetchEmergencyCursor(oldmanphone);
				convertCursor(cursor);
				cursorAdapter.swapCursor(cursor);
				cursorAdapter.notifyDataSetChanged();
			}
			
			else if(msg.what == 0x111){
				
		        DialogUtil.showDialog(SetEmergencyActivity.this, "紧急号码完成同步！");    
				
			}
		}
		
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		oldmanphone = getIntent().getStringExtra("thisOldman");
		
		setContentView(R.layout.activity_set_emergency);


		
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
       
		listView = (SlideCutListView)findViewById(R.id.emergency_list);
		
		listView.setRemoveListener(this);
		
		cursor = LocalFileUtil.fetchEmergencyCursor(oldmanphone);
		convertCursor(cursor);
		
		String[] from = {"phone","name"};
		int[] to = {R.id.emer_phone,R.id.emer_name};
	    cursorAdapter =
				new SimpleCursorAdapter(this, R.layout.emergency_item, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		listView.setAdapter(cursorAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_emergency, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.add_emergency) {
			
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			
			startActivityForResult(intent, PICK_CONTRACT);
			return true;
		}
		
		if(id == R.id.send_emergency){
			   
			      final String thisClient = LocalFileUtil.getThisClient(this);
			      final HeartMsg heartMsg = new HeartMsg();
			      heartMsg.setCreateTime(System.currentTimeMillis());
			      heartMsg.setFromUserName(thisClient);
			      heartMsg.setToUserName(oldmanphone);
			      heartMsg.setMsgType("emergency_list");
			      
			      
			      new Thread(new Runnable() {
					
					@Override
					public void run() {
						  
						  ArrayList<EmergencyPhone> emegencyPhones = new ArrayList<>();
						 Cursor c = LocalFileUtil.fetchEmergencyCursor(oldmanphone);
						 
						  while(c.moveToNext()){	     
							  
							    EmergencyPhone emegencyPhone = new EmergencyPhone();
							    emegencyPhone.setPhone(c.getString(1));
							    emegencyPhone.setName(c.getString(2));
							    
							    emegencyPhones.add(emegencyPhone);						    
						  }
						  c.close();
						  
						  
						  heartMsg.setMsgContent(JSONArray.toJSONString(emegencyPhones));
						
						  if(!MSGUtil.session.isConnected()){
							   
							   MSGUtil.session = MSGUtil.getIOSession(thisClient);
						  }
						  
						  MSGUtil.session.write(JSON.toJSONString(heartMsg));
						  
						  handler.sendEmptyMessage(0x111);
						  
						  MSGUtil.session.getCloseFuture().awaitUninterruptibly();
					}
				}).start();
			      
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data){
		
		 super.onActivityResult(reqCode, resultCode, data);
		 
		 if(reqCode == PICK_CONTRACT){
			 
			     if(resultCode == Activity.RESULT_OK){
			    	 
			    	    Uri contactData = data.getData();
			    	    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
			    	    
			    	    if(cursor.moveToFirst()){
			    	    	 
			    	    	String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			    	    	
			    	    	final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			    	    	
			    	    	String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			    	    	
			    	    	
			    	    	if(hasPhone.equals("1")){
			    	    		
			    	    		  Cursor phones = getContentResolver().query(
			    	    				  ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			    	    				  null,
			    	    				  ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID,
			    	    				  null,
			    	    				  null
			    	    				  );
			    	    		 if(phones.moveToFirst()) {
			    	    			 
			    	    			 final String phone =
			    	    					 phones.getString(phones.getColumnIndex
			    	    							 (ContactsContract.CommonDataKinds.Phone.NUMBER));
			    	    			 
			    	    			 // 存本地数据库
			    	    			 new Thread(new Runnable() {
										
										@Override
										public void run() {
											 LocalFileUtil.SaveEmergencyInDB(name, phone,oldmanphone);
											 
											 handler.sendEmptyMessage(0x9);
											 
										}
									}).start();
			    	    			
			    	    			 
			    	    		 }
			    	    		 
			    	    	}
			    	    }
			     }
		 }
	}

	@Override
	public void removeItem(RemoveDirection direction, final int position) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				LocalFileUtil.removeEmergencyItem(map.get(position));
				handler.sendEmptyMessage(0x9);
			}
		}).start();
		
	    
	}

	private void convertCursor(Cursor cursor){
		
		   map.clear();
		   
		   int i = 0;
		   while(cursor.moveToNext()){
			      
			      map.put(i, cursor.getInt(0));
			      i++;
		   }
	}
	
}
