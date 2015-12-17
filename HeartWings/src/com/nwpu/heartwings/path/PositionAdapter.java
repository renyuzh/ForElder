package com.nwpu.heartwings.path;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.heart.bean.RegularPosition;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.app.HeartApp;
import com.nwpu.heartwings.util.LoadViewDataUtil;
import com.nwpu.heartwings.util.ReadBitMapUtil;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PositionAdapter extends BaseAdapter {

	private Context context;
	
	private List<RegularPosition> msgs;
	
	public static final String STRINGFORMAT = "дк<font color='#1479ad'>%s</font>";
	
	public static final String PMFORMAT = "PM2.5 : <font color='#F86565'>%s</font>";
	
	public PositionAdapter(Context context, List<RegularPosition> msgs) {
		super();
		
		this.context = context;
		this.msgs = msgs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msgs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return msgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		RegularPosition positionMsg = msgs.get(position);
		ViewHolder viewHolder;
		if(convertView == null
				|| (viewHolder = (ViewHolder)convertView.getTag()).flag != position)
		{
			viewHolder = new ViewHolder();
			
			
			if(position == 0)
			{
				viewHolder.flag = position;
				
				convertView = LayoutInflater.from(context).inflate(R.layout.position_cover, null);
				
				ImageView bg = (ImageView)convertView.findViewById(R.id.cover_image);
				bg.setImageResource(R.drawable.cover);
				
				final ImageView fresh = (ImageView)convertView.findViewById(R.id.cover_refresh);
				
				fresh.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						Log.d("czp", "click the fresh...");
						
						try {
							msgs = LoadViewDataUtil.refreshPosition();
							notifyDataSetChanged();
							
							final Animation anim = AnimationUtils.loadAnimation(context, R.anim.refresh);
							fresh.startAnimation(anim);
							
						} catch (InterruptedException | ExecutionException e) {
							
							Log.d("czp", "refresh position err");
							
							e.printStackTrace();
						}
						
					}
				});
				
			}
			
			else {
				
				viewHolder.flag = position;
				
				
				convertView = LayoutInflater.from(context).inflate
						(R.layout.position_activity_item, null);
				
				ImageView userImageView = (ImageView)convertView.findViewById(R.id.user_photo);
				userImageView.setImageResource(R.drawable.person);
				
				TextView userName = (TextView)convertView.findViewById(R.id.username_position);
				userName.setText(HeartApp.getInstance().oldmanHashMap.get(positionMsg.getOldmanPhone()));
				
				ImageView big = (ImageView)convertView.findViewById(R.id.moment_bigdot);
				ImageView sma = (ImageView)convertView.findViewById(R.id.moment_smalldot);
				
				/*ImageView imgType = (ImageView)convertView.findViewById(R.id.moment_people_photo);*/
			//	ImageView feedType = (ImageView)convertView.findViewById(R.id.feed_post_type);
				
				LinearLayout contentBody  = (LinearLayout)convertView.findViewById(R.id.feed_post_body);
				
				big.setVisibility(View.GONE);
				sma.setVisibility(View.VISIBLE);
				
				View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
				
				TextView textView = (TextView)view.findViewById(R.id.showcontent);
				
				String txt = 
						String.format(STRINGFORMAT, positionMsg.getArea());
				

				Spanned spanned = Html.fromHtml(txt);
				
				textView.setText(spanned);
				
				
				ImageView baidu = (ImageView)view.findViewById(R.id.baidupic);
				
			  try {
				
			//	System.out.println();
				baidu.setImageBitmap(ReadBitMapUtil.returnBitmap(positionMsg.getImgUrl()));
			} catch (InterruptedException | ExecutionException e) {
				 baidu.setImageResource(R.drawable.duoyun);
				
			}
			   
				TextView tianqiTextView = (TextView)view.findViewById(R.id.baidutianqi);
				tianqiTextView.setText(positionMsg.getTianqi());
				
				TextView wenduTextView = (TextView)view.findViewById(R.id.baiduwendu);
				wenduTextView.setText(positionMsg.getDegree());
				
				TextView pmTextView = (TextView)view.findViewById(R.id.baidupm);
				String pmTxt = String.format(PMFORMAT, positionMsg.getPm25());
				Spanned pmSpanned = Html.fromHtml(pmTxt);
				pmTextView.setText(pmSpanned);
				
				contentBody.addView(view);
			}
			
			
			convertView.setTag(viewHolder);
			
		}
		
		
		return convertView;
	}
	
	static class ViewHolder{
		
		TextView text;
		TextView time;
		TextView staus;
		int flag = -1;
	}

}
