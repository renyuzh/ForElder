package com.nwpu.heartwings.guide;


import com.nwpu.heartwings.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GuideFragmentAdapter extends FragmentPagerAdapter{

	protected static final int[] CONTENT = new int[] {
		R.drawable.biz_ad_new_version1_img0,
		R.drawable.biz_ad_new_version1_img1,
		R.drawable.biz_ad_new_version1_img2,
		R.drawable.biz_ad_new_version1_img3 };
	
	public GuideFragmentAdapter(FragmentManager fm) {
		super(fm);
	
	}

	@Override
	public Fragment getItem(int position) {
		
		boolean isLastPic = false;
		if (position == CONTENT.length - 1)
			isLastPic = true;
		
		return GuideFragment.newInstance(CONTENT[position], isLastPic);
	}

	@Override
	public int getCount() {
		
		return CONTENT.length;
	}

	
}
