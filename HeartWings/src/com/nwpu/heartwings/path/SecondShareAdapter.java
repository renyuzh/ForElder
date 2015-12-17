package com.nwpu.heartwings.path;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.heart.bean.RegularShare;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.util.LoadViewDataUtil;
import com.nwpu.heartwings.util.ReadBitMapUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondShareAdapter extends BaseAdapter {

	private Context context;
	private List<RegularShare> regularShares;
	
	
	public SecondShareAdapter(Context context, List<RegularShare> regularShares) {
		super();
		this.context = context;
		this.regularShares = regularShares;
	}

	@Override
	public int getCount() {
		
		return regularShares.size();
	}

	@Override
	public Object getItem(int position) {
	
		return regularShares.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		RegularShare regularShare = regularShares.get(position);
		
		if(position == 0){
			
			convertView = LayoutInflater.from(context).inflate(
					R.layout.position_cover, null);
			ImageView bg = (ImageView) convertView
					.findViewById(R.id.cover_image);
			bg.setImageResource(R.drawable.cover);
			
			final ImageView fresh = (ImageView)convertView.findViewById(R.id.cover_refresh);
			fresh.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Log.d("czp", "click the fresh position...");
					
					try {
						
						regularShares = LoadViewDataUtil.refreshShare();
						notifyDataSetChanged();
						
						final Animation anim = AnimationUtils.loadAnimation(context, R.anim.refresh);
						fresh.startAnimation(anim);
					} catch (InterruptedException | ExecutionException e) {
						
						e.printStackTrace();
					}
					
				}
			});

		}
		
		else {
			
			convertView = LayoutInflater.from(context).inflate(
					R.layout.second_shareitem, null);
			
			ImageView sharePageImageView = (ImageView)convertView.findViewById(R.id.share_page);
			
			try {
			//	sharePageImageView.setImageBitmap( ReadBitMapUtil.getShareBitmap(regularShare.getPicUrls().get(0)));
				
				if(position == 1)
				{
				sharePageImageView.setImageResource(R.drawable.a);	
				}
				else if(position == 2)
				{
					sharePageImageView.setImageResource(R.drawable.b);	
				}
				else if(position == 3)
				{
					sharePageImageView.setImageResource(R.drawable.c);	
				}
				else if(position == 4)
				{
					sharePageImageView.setImageResource(R.drawable.d);	
				}
			} catch (Exception e) {
			   sharePageImageView.setImageResource(R.drawable.missingfile);
				e.printStackTrace();
			}
			
			
		 TextView shareTime = (TextView)convertView.findViewById(R.id.share_time);
		 
		 shareTime.setText("");
		 
		 TextView howmanyTextView = (TextView)convertView.findViewById(R.id.share_howmany);
		 howmanyTextView.setText("");
		 
		 
		 
		}
		return convertView;
	}

}
