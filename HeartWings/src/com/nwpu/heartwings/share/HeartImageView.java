package com.nwpu.heartwings.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HeartImageView extends ImageView {

	private OnMeasureListener measureListener;
	
	
	public HeartImageView(Context context) {
		super(context);
		
	}
	
	public HeartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public HeartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

    


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(measureListener != null)
		{
			measureListener.onMeasureSize(getMeasuredWidth(), getMeasuredHeight());
		}
	}

	public OnMeasureListener getMeasureListener() {
		return measureListener;
	}



	public void setMeasureListener(OnMeasureListener measureListener) {
		this.measureListener = measureListener;
	}



	public interface OnMeasureListener{
		public void onMeasureSize(int width, int height);
	}
	

}
