package com.nwpu.heartwings.share;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.nwpu.heartwings.R;
import com.nwpu.heartwings.activities.ImageViewActivity;
import com.nwpu.heartwings.util.ReadBitMapUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageInnerGridViewAdapter extends BaseAdapter {

	private Context context;
	//private List<String> path;
	
	private List<Integer> path;
	
	public ImageInnerGridViewAdapter(Context context, List<Integer> path) {
		super();
		this.context = context;
		this.path = path;
	}

	@Override
	public int getCount() {
		
		return path.size();
	}

	@Override
	public Object getItem(int position) {
		
		return path.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ImageView view = (ImageView)LayoutInflater.from(context).inflate(R.layout.share_img, null);
		try {
		//	Bitmap bitmap = ReadBitMapUtil.getShareBitmap(path.get(position));
			
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), path.get(position));
			view.setImageBitmap(bitmap);
			
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
				
					
					Intent intent = new Intent(context,ImageViewActivity.class);
					intent.putExtra("image_path", path.get(position));
					
			
					context.startActivity(intent);
					
				}
			});
			
		} catch (Exception e) {
			
			view.setImageResource(R.drawable.missingfile);
			
			e.printStackTrace();
		}
		
		return view;
	}

}
