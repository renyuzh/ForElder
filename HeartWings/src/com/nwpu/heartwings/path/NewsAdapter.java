package com.nwpu.heartwings.path;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.heart.bean.HeartNews;
import com.nwpu.heartwings.NewsContentActivity;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.share.HeartImageView;
import com.nwpu.heartwings.util.ReadBitMapUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {

	List<HeartNews> heartNewsList;
	Context context;
	
	public NewsAdapter(List<HeartNews> heartNewsList, Context context) {
		super();
		this.heartNewsList = heartNewsList;
		this.context = context;
	}

	@Override
	public int getCount() {
		
		return heartNewsList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return heartNewsList.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final HeartNews heartNews = heartNewsList.get(position);

		if(position == 0){
			
			convertView = LayoutInflater.from(context).inflate(R.layout.news_headline, null);
			
			ImageView headImageView = (ImageView)convertView.findViewById(R.id.headline_img);
			
			TextView textView = (TextView)convertView.findViewById(R.id.headline_title);
			
			try {
				headImageView.setImageBitmap(ReadBitMapUtil.getShareBitmap(heartNews.getNewsImg()));
		        textView.setText(heartNews.getTitle());
		        
			} catch (InterruptedException | ExecutionException e) {
				
				headImageView.setImageResource(R.drawable.missingfile);
				
				e.printStackTrace();
			}

		}
		
		else {
			
			convertView = LayoutInflater.from(context).inflate(R.layout.news_items, null);
			ImageView newsImage = (ImageView)convertView.findViewById(R.id.news_img);
			
			TextView newsTitle = (TextView)convertView.findViewById(R.id.news_list_title);
			
			TextView newsAbstract = (TextView)convertView.findViewById(R.id.news_sub_title);
			try {
				newsImage.setImageBitmap(ReadBitMapUtil.getShareBitmap(heartNews.getNewsImg()));
				newsTitle.setText(heartNews.getTitle());
				newsAbstract.setText(heartNews.getNewsAbstract());
				
			} catch (InterruptedException | ExecutionException e) {
				newsImage.setImageResource(R.drawable.missingfile);
				e.printStackTrace();
			}
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			    
				Intent intent = new Intent();
				intent.setClass(context, NewsContentActivity.class);
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("mainNews", heartNews);
				intent.putExtras(bundle);

				context.startActivity(intent);
				
			}
		});
		
		return convertView;
	}

	public interface FreshNewsInterface{
	    
		public void  freshNews();
		  
	}
}
