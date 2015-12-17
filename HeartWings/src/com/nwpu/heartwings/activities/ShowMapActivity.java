package com.nwpu.heartwings.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.heart.bean.HeartMsg;
import com.heart.bean.OldmanBean;
import com.heart.bean.RegularPosition;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.R.id;
import com.nwpu.heartwings.R.layout;
import com.nwpu.heartwings.R.menu;
import com.nwpu.heartwings.app.HeartApp;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.HeartOverLay;
import com.nwpu.heartwings.util.HttpUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;
import com.tencent.tencentmap.mapsdk.a.l;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewShow;
import com.tencent.tencentmap.streetviewsdk.overlay.ItemizedOverlay;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

@SuppressLint("HandlerLeak")
public class ShowMapActivity extends ActionBarActivity implements StreetViewListener{

	MapView mapview=null;
	
	HeartOverLay overLay = null;
	
	private ViewGroup viewContainer = null;
	
	private View mStreetView = null;
	
	
	private String oldmanPhone;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
		  
			 if(msg.what == 0x36){
				    
				   if(msg.obj.equals(CONSTANTS.LOGIN_ERR_TAG))
				   {
					   Toast.makeText(ShowMapActivity.this, "获取地理信息失败", Toast.LENGTH_LONG).show();
					   return;
				   }
				   
				   RegularPosition position = JSON.parseObject(msg.obj.toString(), RegularPosition.class);
				   
				   double latitude = position.getLatitude() * 1E6;
				   double longtitude = position.getLongtitude() * 1E6;
				   
				   GeoPoint center = new GeoPoint((int)latitude, (int)longtitude);
				   mapview.getController().setCenter(center);
				   mapview.getController().setZoom(17);
				   
				   overLay = new HeartOverLay(ShowMapActivity.this,center);
				   
				   
				   mapview.addOverlay(overLay);
				   
				   com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint
				      streetPoint = new com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint((int)latitude,(int) longtitude);
				   
				   StreetViewShow.getInstance().showStreetView(ShowMapActivity.this, 
						   streetPoint , 300,ShowMapActivity.this, -170,0);
				   
				   
				  
			 }
		}
		
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_map);
        
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		
		
	    oldmanPhone =	getIntent().getStringExtra("oldmanphone");
	    
		mapview = (MapView)findViewById(R.id.mapview);
		
		viewContainer = (ViewGroup)findViewById(R.id.streetLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

			
		getMenuInflater().inflate(R.menu.show_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		int id = item.getItemId();
		
		if (id == R.id.action_refresh_map) {
			
		     if(!CheckNetWork.isNetAvailable(ShowMapActivity.this))
		     {
		    	 DialogUtil.showDialog(ShowMapActivity.this, CONSTANTS.NETWORKERR);
		    	 return true;
		     }
		     
			
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
                        
					    Map<String, String> rawParams = new HashMap<>();
					    rawParams.put("oldmanPhone", oldmanPhone);
					    
                         String result = HttpUtil.postRequest(HttpUtil.BASEURL + "fetchLatestPosition", rawParams);
                       
                         Message message = new Message();
                         message.what = 0x36;
                         message.obj = result;
                         
                         handler.sendMessage(message);
				}
			}).start();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	 @Override
	    protected void onDestroy() {
	        mapview.onDestroy();
	        StreetViewShow.getInstance().destory();
	        super.onDestroy();
	    }
	 
	    @Override
	    protected void onPause() {
	        mapview.onPause();
	        
	        super.onPause();
	    }
	 
	    @Override
	    protected void onResume() {
	        mapview.onResume();
	        super.onResume();
	    }
	 
	    @Override
	    protected void onStop() {
	        mapview.onStop();
	        super.onStop();
	    }

		@Override
		public ItemizedOverlay getOverlay() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onAuthFail() {
			
		}

		@Override
		public void onDataError() {
			
		}

		@Override
		public void onLoaded() {
			
			int latitude = StreetViewShow.getInstance().getStreetStatus().latitudeE6;
			int longtitude = StreetViewShow.getInstance().getStreetStatus().longitudeE6;
			
			GeoPoint newCenter = new GeoPoint(latitude, longtitude);
			
			
			mapview.getController().setCenter(newCenter);
			
			
		}

		@Override
		public void onNetError() {
			
		}

		@Override
		public void onViewReturn(final View view) {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					mStreetView = view;
					
					viewContainer.addView(mStreetView);
				}
			});
		}

	    
}
