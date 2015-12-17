package com.nwpu.heartwings.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.heart.bean.HeartMsg;
import com.heart.bean.SingleEvent;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.agenda.DateTimePickerDialog;
import com.nwpu.heartwings.agenda.DateTimePickerDialog.OnDateTimeSetListener;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.ChooseDialogUtil;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class SetAgendaActivity extends ActionBarActivity{

	TextView date1;//左边的日期
	TextView date2;//右边的日期
	TextView time1;//左边的时间
	TextView time2;//右边的时间
	ImageView imageview1;//左边的设置点
	ImageView imageview2;//右边的设置点
	
	EditText edittext=null;//输入通知老人的内容
	Button backbutton;// 返回主界面
	Button setbutton;//设置时间
	
	int whichtime=0;//判断先设置哪个设置点的时间
	
	private boolean hasSelected = false;
	
	private static long selectedDate;
	
	private ProgressDialog progressDialog;
	
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 0x48)
			{
				progressDialog.dismiss();
				
				DialogUtil.showDialog(SetAgendaActivity.this, "提醒设置完成");
			}
		}
		
		
	};
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setagenda);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(null);
		progressDialog.setMessage("正在同步日程提醒...");
		
		
		edittext=(EditText) findViewById(R.id.editText1);
		edittext.setBackgroundColor(Color.BLUE);  
		date1=(TextView)findViewById(R.id.textView1);
		date2=(TextView)findViewById(R.id.textView2);
		time1=(TextView)findViewById(R.id.textView3);
		time2=(TextView)findViewById(R.id.textView4);
		imageview1=(ImageView)findViewById(R.id.fromImageView);
		imageview2=(ImageView)findViewById(R.id.toImageView);
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd",Locale.CHINA);  
		String date=sdf.format(new java.util.Date()); 
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		t.setToNow(); // 取得系统时间。  
		
		int hour = t.hour; // 0-23  
		int minute = t.minute;  
		
		String[] sourceStrArray=date.split("-");
		String sore=sourceStrArray[0];
		if(sourceStrArray[0].substring(0, 1).equals("0"))
			sore=sourceStrArray[0].substring(1, 2)+"月";
		else
			sore=sourceStrArray[0].substring(0, 2)+"月";
		sore=sore+sourceStrArray[1];
	
		date1.setText(sore);
		date2.setText(sore);
		if(minute>=0&&minute<=9)
		{
			time1.setText(hour+":0"+minute);
			time2.setText(hour+":0"+minute);
		}
		else
		{
			time1.setText(hour+":"+minute);
			time2.setText(hour+":"+minute);
		}
		
		
		imageview1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				whichtime=0;
		        setReminder();
				
				
			}
		});
		imageview2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				whichtime=1;
				setReminder();
				
			}
		});
		
		
		
	}
	 private void setReminder()
	 {
		  DateTimePickerDialog d = new DateTimePickerDialog(this, System.currentTimeMillis());
		  Window window = d.getWindow();  
		  window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置  
		  window.setWindowAnimations(R.style.agendastyle);  //添加动画  
	      d.setOnDateTimeSetListener(new OnDateTimeSetListener()
	      {
			@Override
			public void OnDateTimeSet(AlertDialog dialog, long date)
			{
				  
					if(whichtime==0)
					{
						selectedDate = date;
						
						String string = getStringDate(date);
						String[] sourcestrArray=string.split("-");
						String sor=sourcestrArray[0];
						if(sourcestrArray[0].substring(0, 1).equals("0"))
							sor=sourcestrArray[0].substring(1, 2)+"月";
						else
							sor=sourcestrArray[0].substring(0, 2)+"月";
						sor=sor+sourcestrArray[1];
						date1.setText(sor);
						date2.setText(sor);
						String string1 = getStringDate1(date);
						time1.setText(string1);
						time2.setText(string1);
							
					}
					if(whichtime==1)
					{
						String string = getStringDate(date);
						String[] sourcestrArray=string.split("-");
						String sor=sourcestrArray[0];
						if(sourcestrArray[0].substring(0, 1).equals("0"))
							sor=sourcestrArray[0].substring(1, 2)+"月";
						else
							sor=sourcestrArray[0].substring(0, 2)+"月";
						sor=sor+sourcestrArray[1];
						
						date2.setText(sor);
						String string1 = getStringDate1(date);
						
						time2.setText(string1);
					}
				
			}
		});
	        d.show();
	    }
	/**
	* 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	*
	*/
	public static String getStringDate(Long date) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd",Locale.CHINA);
		String dateString = formatter.format(date);
		
		return dateString;
	}
	public static String getStringDate1(Long date) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm",Locale.CHINA);
		String dateString = formatter.format(date);
		
		return dateString;
	}
	
		@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			
		getMenuInflater().inflate(R.menu.agenda_menu, menu);
		return true;
		
	}
	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		     
			Log.d("home", item.getItemId() + item.toString() + "  " +R.id.home);
			
			if(item.getItemId() == R.id.action_save_agenda){
				         
				  if(!CheckNetWork.isNetAvailable(this)){
					     
					   DialogUtil.showDialog(this, CONSTANTS.NETWORKERR);
					   return true;
				  }
				  
				  if(TextUtils.isEmpty(edittext.getText())){
					    
					  DialogUtil.showDialog(this, "提醒事件不能为空");
					  return true;
				  }
				  
				  if(!hasSelected){
					  
					    if(ChooseDialogUtil.showChoose(this) == null)
					    {
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
			     heartMsg.setMsgType("schedule_single_event");
			     heartMsg.setToUserName(ChooseDialogUtil.selectdPhone);
			     
			     SingleEvent singleEvent = new SingleEvent();
			     singleEvent.setContent(edittext.getText().toString());
			     singleEvent.setTimestamp(selectedDate);
			     
			     heartMsg.setMsgContent(JSON.toJSONString(singleEvent));
			     
			     progressDialog.show();
			     
			     new Thread(new Runnable() {
					
					@Override
					public void run() {
						 if(!MSGUtil.session.isConnected())
					     {
					    	 MSGUtil.session = MSGUtil.getIOSession(thisClient);
					     }
						  
					     MSGUtil.session.write(JSON.toJSONString(heartMsg));
					     handler.sendEmptyMessage(0x48);
					     MSGUtil.session.getCloseFuture().awaitUninterruptibly();			
					     					     
					}
				}).start();
			         
			}
			
			if(item.getItemId() == 16908332)
			{
         
				 NavUtils.navigateUpFromSameTask(this);
				 return true;
			}
			return true;
		}
	
		
}
