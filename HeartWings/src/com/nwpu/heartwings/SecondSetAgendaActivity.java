package com.nwpu.heartwings;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.heart.bean.HeartMsg;
import com.heart.bean.SingleEvent;
import com.nwpu.heartwings.agenda.DateTimePickerDialog;
import com.nwpu.heartwings.agenda.DateTimePickerDialog.OnDateTimeSetListener;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.ChooseDialogUtil;
import com.nwpu.heartwings.util.DateToStringUtil;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;
import com.tencent.tencentmap.mapsdk.a.i;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

@SuppressLint("HandlerLeak")
public class SecondSetAgendaActivity extends ActionBarActivity implements OnClickListener{

	
	private long selectedDate = System.currentTimeMillis();
	
	private EditText singleEventContentEditText;
	
	private TextView singleEventTimeEditTextView;
	
	private Button save_sendButton;
	
	private boolean hasSelected = false;
	
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
		
			if(msg.what == 0x103){
				
				DialogUtil.showDialog(SecondSetAgendaActivity.this, "提醒设置完成");
			}
		}
		
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_set_agenda);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.second_set_agenda_bg);
	
	    layout.getBackground().setAlpha(100);
	
		
		singleEventContentEditText = (EditText)findViewById(R.id.single_event_content);
		singleEventTimeEditTextView = (TextView)findViewById(R.id.single_event_time);
		save_sendButton = (Button)findViewById(R.id.save_and_send_agendaBtn);
		
		
		
		singleEventTimeEditTextView.setText(DateToStringUtil.ConvertToString(new Date()));
		
		singleEventTimeEditTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				setReminder();
			}
		});
		
		
		findViewById(R.id.medicine_icon).setOnClickListener(this);
		findViewById(R.id.drink_icon).setOnClickListener(this);
		findViewById(R.id.other_icon).setOnClickListener(this);
		findViewById(R.id.birthday_icon).setOnClickListener(this);
		findViewById(R.id.memory_icon).setOnClickListener(this);
		save_sendButton.setOnClickListener(this);
	}




	private void setReminder(){
		
		DateTimePickerDialog d = new DateTimePickerDialog(this, System.currentTimeMillis());
		
		 Window window = d.getWindow();  
		 
		  window.setGravity(Gravity.BOTTOM);  
		  window.setWindowAnimations(R.style.agendastyle); 
		  
		  d.setOnDateTimeSetListener(new OnDateTimeSetListener() {
			
			@Override
			public void OnDateTimeSet(AlertDialog dialog, long date) {
				
				selectedDate = date;
				
				singleEventTimeEditTextView.setText(DateToStringUtil.ConvertToString(new Date(date)));
			}
		});
		  
		  d.show();
	}

	@Override
	public void onClick(View view) {
		
		Intent intent;
		switch(view.getId()){
		case R.id.medicine_icon:
			intent = new Intent(SecondSetAgendaActivity.this,SetMedicineActivity.class);
			intent.putExtra("whichicon", "medicine");
			startActivity(intent);
			break;
		case R.id.drink_icon:
			
			intent = new Intent(SecondSetAgendaActivity.this,SetMedicineActivity.class);
			intent.putExtra("whichicon", "drink");
			startActivity(intent);
			break;
		case R.id.other_icon:
			intent = new Intent(SecondSetAgendaActivity.this,SetMedicineActivity.class);
			intent.putExtra("whichicon", "other");
			startActivity(intent);
		   break;
		case R.id.birthday_icon:
			intent = new Intent(SecondSetAgendaActivity.this,BirthdayActivity.class);
			intent.putExtra("whichicon", "birthday");
			startActivity(intent);
			break;
		case R.id.memory_icon:
			intent = new Intent(SecondSetAgendaActivity.this,BirthdayActivity.class);
			intent.putExtra("whichicon", "memory");
			startActivity(intent);
			break;
		case R.id.save_and_send_agendaBtn:
			
			if (!CheckNetWork.isNetAvailable(this)) {

				DialogUtil.showDialog(this, CONSTANTS.NETWORKERR);
				return;
			}
			
			if(TextUtils.isEmpty(singleEventContentEditText.getText()))
			{
				DialogUtil.showDialog(this, "提醒事件不能为空");
				return;
			}
			
			if(!hasSelected)
			{
				if (ChooseDialogUtil.showChoose(this) == null) {
					DialogUtil.showDialog(this, "请先前往设置页面绑定老人");
					return;
				}
				hasSelected = true;
				
				save_sendButton.setText("同步");
				return;
			}
			
			else {
				
				  final HeartMsg heartMsg = new HeartMsg();
				     heartMsg.setCreateTime(System.currentTimeMillis());
				     final String thisClient = LocalFileUtil.getThisClient(this);
				     heartMsg.setFromUserName(thisClient);
				     heartMsg.setMsgType("schedule_single_event");
				     heartMsg.setToUserName(ChooseDialogUtil.selectdPhone);
				     
				     SingleEvent singleEvent = new SingleEvent();
				     singleEvent.setContent(singleEventContentEditText.getText().toString());
				     singleEvent.setTimestamp(selectedDate);
				     heartMsg.setMsgContent(JSON.toJSONString(singleEvent));
				     
				     new Thread(new Runnable() {
							
							@Override
							public void run() {
								 if(!MSGUtil.session.isConnected())
							     {
							    	 MSGUtil.session = MSGUtil.getIOSession(thisClient);
							     }
								  
							     MSGUtil.session.write(JSON.toJSONString(heartMsg));
							     handler.sendEmptyMessage(0x103);
							     MSGUtil.session.getCloseFuture().awaitUninterruptibly();			
							     					     
							}
						}).start();
				     
			}
			break;
		}
	}

}
