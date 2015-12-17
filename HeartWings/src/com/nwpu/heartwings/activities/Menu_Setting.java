package com.nwpu.heartwings.activities;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.nwpu.heartwings.AboutActivity;
import com.nwpu.heartwings.ChangePwdActivity;
import com.nwpu.heartwings.FeedBackActivity;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.util.Choose2Dialog;

public class Menu_Setting extends ActionBarActivity {

	public static final int INVITE_CONTRACT = 14;

	Button btn0;
	Button btn1;
	Button btn2;
	Button btn3;
	Button btn4;
	Button btn5;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		getSupportActionBar().setHomeButtonEnabled(true);

		btn0 = (Button) findViewById(R.id.change_pwd_btn);
		btn1 = (Button) findViewById(R.id.set_emergency_btn);
		btn2 = (Button) findViewById(R.id.invite_other2);

		btn3 = (Button) findViewById(R.id.feedback_btn);
		btn4 = (Button) findViewById(R.id.check_new_version);
		btn5 = (Button) findViewById(R.id.about_us_btn);

		onClickButton();
	}

	public void onClickButton() {

		btn0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Menu_Setting.this,
						ChangePwdActivity.class));
			}
		});

		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Choose2Dialog.showDialog(Menu_Setting.this, R.id.set_emergency_btn);
			}
		});

		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("czp", "run the invite");
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
				startActivityForResult(intent, INVITE_CONTRACT);
			}
		});

		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Menu_Setting.this,
						FeedBackActivity.class));
			}
		});

		btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "目前是最新版本", Toast.LENGTH_LONG).show();
			}
		});

		btn5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Menu_Setting.this, AboutActivity.class));
			}
		});

	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.function4, menu); 
		
		return true; 
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if (item.getItemId() == R.id.action_setting) {
			
			Log.d("czp","item.getItemId() == R.id.action_setting");
			Intent intent = new Intent();
			intent.setClass(Menu_Setting.this, SearchingActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {

		super.onActivityResult(reqCode, resultCode, data);

		if (reqCode == INVITE_CONTRACT) {
			if (resultCode == Activity.RESULT_OK) {

				Uri contactData = data.getData();
				Cursor cursor = getContentResolver().query(contactData, null,
						null, null, null);

				if (cursor.moveToFirst()) {

					String contactID = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));

					String hasPhone = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equals("1")) {

						Cursor phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ "=" + contactID, null, null);

						if (phones.moveToFirst()) {

							final String phone = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

							Uri uri = Uri.parse("smsto:" + phone);
							Intent intent = new Intent(Intent.ACTION_SENDTO,
									uri);
							intent.putExtra("sms_body",
									"快去下载 牵挂 吧 ： http://114.215.122.96/VerySimpleFallDetector.apk");
							startActivity(intent);

						}
					}
				}
			}
		}
	}

}
