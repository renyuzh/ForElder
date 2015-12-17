package com.nwpu.heartwings.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.heart.bean.HeartMsg;
import com.heart.bean.RegularShare;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.activities.ImageViewActivity;
import com.nwpu.heartwings.app.HeartApp;
import com.nwpu.heartwings.share.ImageInnerGridViewAdapter;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.HttpUtil;
import com.nwpu.heartwings.util.LoadViewDataUtil;
import com.nwpu.heartwings.util.ReadBitMapUtil;
import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;
import com.tencent.tencentmap.streetviewsdk.v;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class ShareAdapter extends BaseAdapter {

	private Context context;
	private List<RegularShare> RegularShares;

	public static final String SHARE_FORMAT = 
			"给 <font color='#EE2049'>%s</font>分享了<font color='#222'>%s</font>张照片";
	
	public ShareAdapter(Context context, List<RegularShare> regularShares) {
		super();
		this.context = context;
		this.RegularShares = regularShares;
	}

	@Override
	public int getCount() {

		return RegularShares.size();
	}

	@Override
	public Object getItem(int position) {

		return RegularShares.get(position);

	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RegularShare regularShare = RegularShares.get(position);

		ViewHolderInShare viewHolderInShare;

		if (convertView == null
				|| (viewHolderInShare = (ViewHolderInShare) convertView
						.getTag()).flag != position) {

			viewHolderInShare = new ViewHolderInShare();

			if (position == 0) {
				viewHolderInShare.flag = position;

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
							RegularShares = LoadViewDataUtil.refreshShare();
							notifyDataSetChanged();
							
							final Animation anim = AnimationUtils.loadAnimation(context, R.anim.refresh);
							fresh.startAnimation(anim);
							
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});

			} else {

				viewHolderInShare.flag = position;
				convertView = LayoutInflater.from(context).inflate(
						R.layout.position_activity_item, null);

				ImageView userImageView = (ImageView) convertView
						.findViewById(R.id.user_photo);
				userImageView.setImageResource(R.drawable.person);

				TextView userName = (TextView) convertView
						.findViewById(R.id.username_position);
				userName.setText(regularShare.getOldmanName());

				ImageView big = (ImageView) convertView
						.findViewById(R.id.moment_bigdot);
				ImageView sma = (ImageView) convertView
						.findViewById(R.id.moment_smalldot);

				LinearLayout contentBody = (LinearLayout) convertView
						.findViewById(R.id.feed_post_body);

				big.setVisibility(View.GONE);
				sma.setVisibility(View.VISIBLE);

				View view = LayoutInflater.from(context).inflate(
						R.layout.share_list_view, null);


				/*final ViewAnimator viewAnimator = (ViewAnimator) view
						.findViewById(R.id.share_viewFlipper);*/
				
				GridView gridView = (GridView)view.findViewById(R.id.share_gridview);
				
				
				ArrayList<String> share_path = regularShare.getPicUrls();
                 
				ArrayList<Integer> fack_img = new ArrayList<>();
				fack_img.add(R.drawable.a);
				fack_img.add(R.drawable.b);
				fack_img.add(R.drawable.c);
				fack_img.add(R.drawable.d);
		//		ImageInnerGridViewAdapter adapter = new ImageInnerGridViewAdapter(context, share_path);
				ImageInnerGridViewAdapter adapter = new ImageInnerGridViewAdapter(context, fack_img);
				gridView.setAdapter(adapter);
				
			/*	List<Map<String, Bitmap>> listItems =
						new ArrayList<>();*/
						
						
/*				
				for(String path : share_path){
					
					Map<String, Bitmap> item =
							 new HashMap<>();
							 
					try {
						item.put("share_pic", ReadBitMapUtil.getShareBitmap(path));
					} catch (InterruptedException | ExecutionException e) {
						
					//	item.put("share_pic", BitmapFactory.decodeResource(context.getResources(), R.drawable.missingfile));
						Log.d("img", "load image err ");
						e.printStackTrace();
					}
					
					listItems.add(item);
				}
				
				SimpleAdapter simpleAdapter =
						 new SimpleAdapter(context, listItems, R.layout.share_img,
								 new String[]{"share_pic"}, new int[]{R.id.one_share_view});
				
				gridView.setAdapter(simpleAdapter);
				

				
				simpleAdapter.setViewBinder(new ViewBinder() {
					
					@Override
					public boolean setViewValue(View _view, Object data, String textRe) {
						if(_view instanceof ImageView && data instanceof Bitmap)
						{
							ImageView iv = (ImageView)_view;
							iv.setImageBitmap((Bitmap)data);
							return true;
						}
						return false;
					}
				});*/


				TextView shareContent = (TextView) view
						.findViewById(R.id.share_content);
				String txt = String.format(SHARE_FORMAT, HeartApp.getInstance().oldmanHashMap.get(regularShare.getOldmanPhone()),
						regularShare.getPicUrls().size());
				
				Spanned spanned = Html.fromHtml(txt);
				shareContent.setText(spanned);

				contentBody.addView(view);
			}

			convertView.setTag(viewHolderInShare);
		}
		return convertView;
	}

	static class ViewHolderInShare {

		int flag = -1;
	}
	
	
	
}
