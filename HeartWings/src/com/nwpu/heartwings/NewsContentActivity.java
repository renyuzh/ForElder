package com.nwpu.heartwings;

import java.util.concurrent.ExecutionException;

import com.heart.bean.HeartNews;
import com.nwpu.heartwings.util.ReadBitMapUtil;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class NewsContentActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_news_content);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		
		HeartNews heartNews =
	                     (HeartNews) getIntent().getSerializableExtra("mainNews");
		
		TextView title = (TextView)findViewById(R.id.main_news_title);
		
		title.setText(heartNews.getTitle());
		
		ImageView imageView = (ImageView)findViewById(R.id.main_news_img);
		
		try {
			imageView.setImageBitmap(ReadBitMapUtil.getShareBitmap(heartNews.getNewsImg()));
		} catch (InterruptedException | ExecutionException e) {
			imageView.setImageResource(R.drawable.missingfile);
			e.printStackTrace();
		}
	
		TextView content = (TextView)findViewById(R.id.main_news_content);
		
		String newsContent = heartNews.getNewsContent();
		
		Spanned spanned = Html.fromHtml(newsContent);
		content.setText(spanned);
		
	
	}



}
