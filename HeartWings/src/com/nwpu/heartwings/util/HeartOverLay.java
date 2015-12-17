package com.nwpu.heartwings.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.ItemizedOverlay;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;

public class HeartOverLay extends ItemizedOverlay<OverlayItem> {

	List<OverlayItem> items = new ArrayList<OverlayItem>();
	
	public HeartOverLay(Context mContex) {
		super(mContex);
	}

	public HeartOverLay(Context context, GeoPoint p){
		   
		super(context);
		
		OverlayItem overlayItem = new OverlayItem(p, "Œª÷√", "");
		overlayItem.setDragable(false);
		items.clear();
		items.add(overlayItem);	
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int position) {
	
		
		return items.get(position);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return items.size();
	}

}
