package com.nwpu.heartwings;

import java.util.Calendar;
import com.alibaba.fastjson.JSON;
import com.heart.bean.BirthDayEvent;
import com.heart.bean.HeartMsg;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.ChooseDialogUtil;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

@SuppressLint("HandlerLeak")
public class BirthdayActivity extends ActionBarActivity {

	TextView birthTimeText;
	
	MonthDayPickerDialog mDayPickerDialog;
	
	MonthPair monthPair = new MonthPair();
	
	ImageView iconImageView;
	
	EditText titleEditText;
	
	private boolean hasSelected = false;
	
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 0x102){
				
				DialogUtil.showDialog(BirthdayActivity.this, "提醒设置完成");
			}
		}
    	
    	
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthday);

		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		
		iconImageView = (ImageView)findViewById(R.id.agenda_icon);
		
		titleEditText = (EditText)findViewById(R.id.birthday_content);
		
		String whichIcon = getIntent().getStringExtra("whichicon");
		
		switch (whichIcon) {
		case "birthday":
			iconImageView.setImageResource(R.drawable.cake);
			
			break;

		case "memory":
			iconImageView.setImageResource(R.drawable.special_day);
		default:
			break;
		}
		
		monthPair.setMonth(10);
		monthPair.setDay(15);
		
		birthTimeText = (TextView)findViewById(R.id.birth_time_txt);
		
		
		birthTimeText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				setBirthday();
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.birthday, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_send_birthday) {
			
			if (!CheckNetWork.isNetAvailable(this)) {

				DialogUtil.showDialog(this, CONSTANTS.NETWORKERR);
				return true;
			}

			if (TextUtils.isEmpty(titleEditText.getText())) {
				DialogUtil.showDialog(this, "提醒事件不能为空");
				return true;
			}
			
			if(!hasSelected){		
				if (ChooseDialogUtil.showChoose(this) == null) {
					DialogUtil.showDialog(this, "请先前往设置页面绑定老人");
					return true;
				}
				hasSelected = true;
				return true;
			}
			
			final HeartMsg heartMsg = new HeartMsg();
			heartMsg.setCreateTime(System.currentTimeMillis());
			final String thisClient = LocalFileUtil.getThisClient(this);
			heartMsg.setFromUserName(thisClient);
			heartMsg.setMsgType("birthday_event");
			heartMsg.setToUserName(ChooseDialogUtil.selectdPhone);
			
			BirthDayEvent birthDayEvent = new BirthDayEvent();
			birthDayEvent.setContent(titleEditText.getText().toString());
			birthDayEvent.setMonth(monthPair.getMonth());
			birthDayEvent.setDay(monthPair.getDay());
            birthDayEvent.setId((int) (System.currentTimeMillis() % 2077373681));
			
			heartMsg.setMsgContent(JSON.toJSONString(birthDayEvent));
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (!MSGUtil.session.isConnected()) {
						MSGUtil.session = MSGUtil.getIOSession(thisClient);
					}

					MSGUtil.session.write(JSON.toJSONString(heartMsg));
					handler.sendEmptyMessage(0x102);
					MSGUtil.session.getCloseFuture().awaitUninterruptibly();

				}
			}).start();

			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private void setBirthday(){
		
		   mDayPickerDialog = new MonthDayPickerDialog(this, new OnDateSetListener(){

			@Override
			public void onDateSet(DatePicker picker, int year, int month, int day) {
			    
				monthPair.setMonth(month+1);
				monthPair.setDay(day);
				birthTimeText.setText((month + 1) + "月" + day + "日");
			}
			   
		   }, Calendar.getInstance().get(Calendar.YEAR), monthPair.getMonth() - 1, monthPair.getDay());
		   
		   mDayPickerDialog.setTitle(monthPair.getMonth() + "月" + monthPair.getDay() + "日");
		   mDayPickerDialog.show();
		   
		   DatePicker dp = findDatePicker((ViewGroup) mDayPickerDialog.getWindow().getDecorView()); 
		   if (dp != null) { 
		       ((ViewGroup) dp.getChildAt(0)).getChildAt(0).setVisibility(View.GONE); 	    
		   }  
	}
	
	private DatePicker findDatePicker(ViewGroup group) { 
	    if (group != null) { 
	        for (int i = 0, j = group.getChildCount(); i < j; i++) { 
	            View child = group.getChildAt(i); 
	            if (child instanceof DatePicker) { 
	                return (DatePicker) child; 
	            } else if (child instanceof ViewGroup) { 
	                DatePicker result = findDatePicker((ViewGroup) child); 
	                if (result != null) 
	                    return result; 
	            } 
	        } 
	    } 
	    return null; 
	 
	}  
	
	class MonthDayPickerDialog extends DatePickerDialog{

		public MonthDayPickerDialog(Context context,
				OnDateSetListener callBack, int year, int monthOfYear,
				int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
		
		}
		
		@Override
		public void onDateChanged(DatePicker view, int year,int month,int day){
			super.onDateChanged(view, year, month, day);
			setTitle((month+1)+"月"+day+"日");
		}
		
	}
	
	
	class MonthPair{
		
		int month;
		int day;
		public int getMonth() {
			return month;
		}
		public void setMonth(int month) {
			this.month = month;
		}
		public int getDay() {
			return day;
		}
		public void setDay(int day) {
			this.day = day;
		}
		
	}

}
