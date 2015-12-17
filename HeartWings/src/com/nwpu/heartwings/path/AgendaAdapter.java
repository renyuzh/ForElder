package com.nwpu.heartwings.path;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.alibaba.fastjson.JSON;
import com.heart.bean.BirthDayEvent;
import com.heart.bean.HeartMsg;
import com.heart.bean.SingleEvent;
import com.heart.bean.TakePillsEvent;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.path.PositionAdapter.ViewHolder;
import com.nwpu.heartwings.util.FreshDialogUtil;
import com.nwpu.heartwings.util.HttpUtil;
import com.nwpu.heartwings.util.LoadViewDataUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.tencent.tencentmap.mapsdk.a.b;
import com.tencent.tencentmap.streetviewsdk.ag;
import com.tencent.tencentmap.streetviewsdk.v;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class AgendaAdapter extends BaseAdapter{

	private Context context;
	private List<HeartMsg> heartMsgs;
	
	public static final String EMPTY = "        ";
	
	public AgendaAdapter(Context context, List<HeartMsg> heartMsgs) {
		super();
		this.context = context;
		this.heartMsgs = heartMsgs;
	}

	@Override
	public int getCount() {
		
		return heartMsgs.size();
	}

	@Override
	public Object getItem(int position) {
	   
		return heartMsgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final HeartMsg msg = heartMsgs.get(position);
		AgendaHolder agendaHolder;
		
		if(convertView == null
				|| (agendaHolder = (AgendaHolder)convertView.getTag()).flag != position)
		{
			agendaHolder = new AgendaHolder();
			
			
			if(position == 0)
			{
				agendaHolder.flag = position;
				
				convertView = LayoutInflater.from(context).inflate(R.layout.position_cover, null);
				
				ImageView bg = (ImageView)convertView.findViewById(R.id.cover_image);
				bg.setImageResource(R.drawable.cover);
				
				final ImageView fresh = (ImageView)convertView.findViewById(R.id.cover_refresh);
				fresh.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						try {
							heartMsgs = LoadViewDataUtil.refreshAgenda();
							
							notifyDataSetChanged();
							
					    //    FreshDialogUtil.showDialog(context);
							
							final Animation anim = AnimationUtils.loadAnimation(context, R.anim.refresh);
							fresh.startAnimation(anim);
							
						} catch (InterruptedException | ExecutionException e) {
						
							e.printStackTrace();
						}
						
						
						
					}
				});
			}
			
			else {
				
				agendaHolder.flag = position;
				
				convertView = LayoutInflater.from(context).inflate
						(R.layout.position_activity_item, null);
				
				ImageView userImageView = (ImageView)convertView.findViewById(R.id.user_photo);
				userImageView.setImageResource(R.drawable.person);
				
				TextView userName = (TextView)convertView.findViewById(R.id.username_position);
				userName.setText(msg.getToUserName());
				
				ImageView big = (ImageView)convertView.findViewById(R.id.moment_bigdot);
				ImageView sma = (ImageView)convertView.findViewById(R.id.moment_smalldot);
				
	           LinearLayout contentBody  = (LinearLayout)convertView.findViewById(R.id.feed_post_body);
				
				big.setVisibility(View.GONE);
				sma.setVisibility(View.VISIBLE);
				
				final View view = LayoutInflater.from(context).inflate(R.layout.agenda_list_item, null);
				
				TextView textView = (TextView)view.findViewById(R.id.event_time);
	
				TextView detail = (TextView)view.findViewById(R.id.event_detail);
				
				System.out.println(msg.getMsgType() +"   msg.getMsgType()");
				
				Log.d("msg.getMsgType()", msg.getMsgType());
				
				switch (msg.getMsgType()) {
				case "schedule_single_event":
					SingleEvent singleEvent = JSON.parseObject(msg.getMsgContent(), SingleEvent.class);
					textView.setText(singleEvent.getContent());
					detail.setText(EMPTY + longConvertToString(singleEvent.getTimestamp()));
					break;
				case "take_pills":
					TakePillsEvent takePillsEvent = JSON.parseObject(msg.getMsgContent(), TakePillsEvent.class);
					textView.setText(takePillsEvent.getContent());
					detail.setText(EMPTY + timeListToString(takePillsEvent.getTheTimes_hour(),takePillsEvent.getTheTimes_minute()));
					break;
				case "birthday_event":
					BirthDayEvent birthDayEvent = JSON.parseObject(msg.getMsgContent(), BirthDayEvent.class);
					textView.setText(birthDayEvent.getContent());
					detail.setText(EMPTY + birthDayEvent.getMonth() + "月" + birthDayEvent.getDay() + "日");
					break;
				default:
					break;
				}
				
				final Handler handler = new Handler(){

					@Override
					public void handleMessage(Message msg) {
						
						if(msg.what == 0x105){
							
							heartMsgs.remove(position);
							notifyDataSetChanged();
						}
					}
					
					
				};
				
				ImageView deleteImageView = (ImageView) view.findViewById(R.id.delete_agenda);
				
				deleteImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						// 删数据库，发送mina消息，删除本地
						
		               if(msg.getMsgType().equals("take_pills") || msg.getMsgType().equals("birthday_event"))
		               {
		            	   
		            	
		            	   new Thread(new Runnable() {
								
								@Override
								public void run() {
								
									   String clientMsgID = null;
					            	   
					            	   if(msg.getMsgType().equals("take_pills"))
					            	   {
					            		     clientMsgID = String.valueOf(JSON.parseObject(msg.getMsgContent(), TakePillsEvent.class).getId());
					            	   }
					            	   else {
										
					            		   clientMsgID = String.valueOf(JSON.parseObject(msg.getMsgContent(), BirthDayEvent.class).getId());
					            		   
									}
					            	   
									HashMap<String, String> rawParams = new HashMap<>();
									rawParams.put("oldmanphone", msg.getToUserName());
									rawParams.put("clientMsgID", clientMsgID);
									rawParams.put("_id", msg.getMsgID());
									
									HttpUtil.postRequest(HttpUtil.BASEURL + "deleteAgenda", rawParams);
									
									
									handler.sendEmptyMessage(0x105);
								}
							}).start();
		               }
					   
		               else {
						    
		            	   new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								  HashMap<String, String> rawParams = new HashMap<>();
								  rawParams.put("_id", msg.getMsgID());
								  Log.d("msg.getMsgID()", msg.getMsgID());
								  
								  HttpUtil.postRequest(HttpUtil.BASEURL +"removeSingleEvent" , rawParams);
								  
								  handler.sendEmptyMessage(0x105);
								
							}
						}).start();
		            	   
					}
					}
				});
				
				contentBody.addView(view);
				
			}
			convertView.setTag(agendaHolder);
		}
		
		return convertView;
	}

	
	static class AgendaHolder{
		
		int flag = -1;
	}
	
	private String longConvertToString(long date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(calendar.get(Calendar.YEAR)+ "年" + (calendar.get(Calendar.MONTH) + 1) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日" +calendar.get(Calendar.HOUR_OF_DAY) + "时"
				+ calendar.get(Calendar.MINUTE) + "分");
		
		return sb.toString();
	}
	
	private String timeListToString(List<Integer> hours,List<Integer> minutes){
		
		  StringBuilder sb = new StringBuilder();
		  
		  for(int i = 0; i < hours.size(); i++){
			  
			  sb.append(hours.get(i)+"时" + minutes.get(i) + "分;  ");
		  }
		  
		  return sb.toString();
	}
}
