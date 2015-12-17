package com.nwpu.heartwings.guide;


import com.nwpu.heartwings.R;
import com.nwpu.heartwings.activities.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class GuideFragment extends Fragment {

	private static final String KEY_CONTENT = "GuideFragment:Content";
	private static final String KEY_ISLASTPIC = "GuideFragment:IsLastPic";
	private int mContent;
	private boolean mIsLastPic;
	
	public static GuideFragment newInstance(int content, boolean isLastPic){
		
		GuideFragment guideFragment = new GuideFragment();
		guideFragment.mContent = content;
		guideFragment.mIsLastPic = isLastPic;
		return guideFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getInt(KEY_CONTENT);
			mIsLastPic = savedInstanceState.getBoolean(KEY_ISLASTPIC);
		}
	
		View root = inflater
				.inflate(R.layout.fragment_guide, container, false);
		ImageView iv = (ImageView) root.findViewById(R.id.iv);
		iv.setImageResource(mContent);
		Button btn = (Button) root.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 if(mIsLastPic)
				 {
					 Intent intent = new Intent(getActivity().getBaseContext(),LoginActivity.class);
					 startActivity(intent);
					 getActivity().finish();
				 }
				
			}
		});
		if (mIsLastPic)
			btn.setVisibility(View.VISIBLE);
		else
			btn.setVisibility(View.GONE);
		return root;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CONTENT, mContent);
		outState.putBoolean(KEY_ISLASTPIC, mIsLastPic);
	}
	
}
