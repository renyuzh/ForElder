package com.nwpu.heartwings.share;

import java.util.List;






import com.nwpu.heartwings.R;
import com.nwpu.heartwings.share.HeartImageView.OnMeasureListener;
import com.nwpu.heartwings.share.NativeImageLoader.NativeImageCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {

	private List<ImageBean> imageBeans;
	private Point point = new Point(0,0);
	
	private GridView gridView;
	private LayoutInflater layoutInflater;
	
	@Override
	public int getCount() {
		
		return imageBeans.size();
	}

	@Override
	public Object getItem(int position) {
		
		return imageBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder viewHolder;
		ImageBean imageBean = imageBeans.get(position);
		String topImagePath = imageBean.getTopImagePath();
		
		if(convertView ==  null)
		{
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.grid_group_item, null);
			viewHolder.imageView = (HeartImageView) convertView.findViewById(R.id.group_image);
			viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.group_title);
			viewHolder.textViewCount = (TextView) convertView.findViewById(R.id.group_count);
			
			viewHolder.imageView.setMeasureListener(new OnMeasureListener() {
				
				@Override
				public void onMeasureSize(int width, int height) {
					
					point.set(width,height);
					
				}
			});
			
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.friends_sends_pictures_no);
			
		}
		
		viewHolder.textViewTitle.setText(imageBean.getFolderName());
		viewHolder.textViewCount.setText(String.valueOf(imageBean.getImageCounts()));
		//给ImageView设置路径Tag,这是异步加载图片的小技巧
		viewHolder.imageView.setTag(topImagePath);
		
		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(topImagePath,
				point,
				new NativeImageCallBack(){

					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						ImageView imageView = (ImageView) gridView.findViewWithTag(path);
						
						if(bitmap != null && imageView != null)
						{
							imageView.setImageBitmap(bitmap);
						}
						
					}
			
		});
		
		if(bitmap != null)
		{
			viewHolder.imageView.setImageBitmap(bitmap);
			
		}else {
			viewHolder.imageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		return convertView;
	}
	
	
	public GroupAdapter(Context context, List<ImageBean> list,GridView gridView)
	{
		this.imageBeans = list;
		this.gridView = gridView;
		this.layoutInflater = LayoutInflater.from(context);
		
	}
	public static class ViewHolder{
		
		public HeartImageView imageView;
		public TextView textViewTitle;
		public TextView textViewCount;
	}

}
