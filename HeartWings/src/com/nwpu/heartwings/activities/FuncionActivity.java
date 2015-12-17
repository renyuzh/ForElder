package com.nwpu.heartwings.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.dafruits.android.library.widgets.ExtendedListView;
import com.heart.bean.HeartMsg;
import com.heart.bean.HeartNews;
import com.heart.bean.RegularPosition;
import com.heart.bean.RegularShare;
import com.nwpu.heartwings.AboutActivity;
import com.nwpu.heartwings.ChangePwdActivity;
import com.nwpu.heartwings.FeedBackActivity;
import com.nwpu.heartwings.R;
import com.nwpu.heartwings.SecondSetAgendaActivity;
import com.nwpu.heartwings.activities.FunctionPaperChangeListener.MyPaper;
import com.nwpu.heartwings.app.HeartApp;
import com.nwpu.heartwings.path.AgendaAdapter;
import com.nwpu.heartwings.path.NewsAdapter;
import com.nwpu.heartwings.path.PositionAdapter;
import com.nwpu.heartwings.path.PositionMsg;
import com.nwpu.heartwings.path.SecondShareAdapter;
import com.nwpu.heartwings.path.ShareAdapter;
import com.nwpu.heartwings.share.ImageScanActivity;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.Choose2Dialog;
import com.nwpu.heartwings.util.LoadViewDataUtil;
import com.nwpu.heartwings.util.MSGUtil;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FuncionActivity extends ActionBarActivity implements MyPaper,
		OnClickListener {

	public static final int INVITE_CONTRACT = 14;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	ImageView currentAnchor;

	public static int currentP;

	ImageView position_menu;

	ImageView share_menu;
	ImageView alarm_menu;
//	ImageView setting_menu;
    ImageView home_menu;
	
	TextView positionTextView;
	TextView shareTextView;
	TextView alarmTextView;
//	TextView settingTextView;
	TextView homeTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_funcion);

		SharedPreferences preferences = getSharedPreferences(
				CONSTANTS.PREFERENCE, MODE_PRIVATE);
		final String name = preferences.getString("login_phone", "");

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (MSGUtil.session == null || !MSGUtil.session.isConnected()) {
					MSGUtil.session = MSGUtil.getIOSession(name);
					MSGUtil.session.getCloseFuture().awaitUninterruptibly();
					// MSGUtil.connector.dispose();
				}

			}
		}).start();

		position_menu = (ImageView) findViewById(R.id.menu_action_position);
		position_menu.setOnClickListener(new TabClickListener(1));
		share_menu = (ImageView) findViewById(R.id.menu_action_share);
		 share_menu.setOnClickListener(new TabClickListener(2));
		alarm_menu = (ImageView) findViewById(R.id.menu_action_alarm);
		  alarm_menu.setOnClickListener(new TabClickListener(3));
//		setting_menu = (ImageView) findViewById(R.id.menu_action_setting);
//       setting_menu.setOnClickListener(new TabClickListener(4));
       home_menu = (ImageView)findViewById(R.id.menu_action_home);
       home_menu.setOnClickListener(new TabClickListener(0));
       
       positionTextView = (TextView)findViewById(R.id.txt_position);
       shareTextView = (TextView)findViewById(R.id.txt_share);
       alarmTextView = (TextView)findViewById(R.id.txt_alarm);
//       settingTextView = (TextView)findViewById(R.id.txt_setting);
       homeTextView = (TextView)findViewById(R.id.txt_home);
       
       
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		// currentAnchor = (ImageView)findViewById(R.id.img_tab_now);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new FunctionPaperChangeListener(this));

		System.out.println(savedInstanceState == null);
		if(null != savedInstanceState)
		{
			if(savedInstanceState.containsKey("currentPage"))
			{
				Log.d("czp", "in the onCreate ， read from savedInstanceState");
				mViewPager.setCurrentItem(savedInstanceState.getInt("currentPage"));
			}
			   
		}
		
		else
		{
			Log.d("czp", "savedInstanceState is null!!!!!");
		}
		
		Log.d("czp", "HeartApp.getInstance().currentPage = "+ HeartApp.getInstance().currentPage);
		mViewPager.setCurrentItem(HeartApp.getInstance().currentPage);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * // Inflate the menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.funcion, menu); return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_postion:
			System.out.println("click the action position..");

			Choose2Dialog.showDialog(this, 1);
			/*
			 * intent = new Intent(FuncionActivity.this, ShowMapActivity.class);
			 * startActivity(intent);
			 */
			break;
		case R.id.action_share:
			intent = new Intent(FuncionActivity.this, ImageScanActivity.class);
			startActivity(intent);
			break;
		case R.id.action_alarm:
			intent = new Intent(FuncionActivity.this, SecondSetAgendaActivity.class);
		//	intent = new Intent(FuncionActivity.this, SetAgendaActivity.class);
			startActivity(intent);
			break;
//		case R.id.action_setting:
//			intent = new Intent(FuncionActivity.this, SearchingActivity.class);
//			startActivity(intent);
//			break;
		case R.id.menu_setting:
			intent = new Intent(FuncionActivity.this,Menu_Setting.class);
			startActivity(intent);
		break;
		case R.id.action_home_refresh:
			Log.d("czp", "R.id.action_home_refresh");
			return false;

		}
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			return "";
		}
	}

	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		ExtendedListView mListView;

		public ListView newsListView;
		
		public ListView shareListView;
		
		List<PositionMsg> mainPositionMsgs;
		@SuppressLint("HandlerLeak")
		Handler handler = new Handler() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {

				if (msg.what == 0x40) {

					if (!msg.obj.equals(CONSTANTS.LOGIN_ERR_TAG)) {

						System.out.println("msg is 0x40...");
						loadPositionMsg((List<RegularPosition>) msg.obj);
					}
				} else if (msg.what == 0x41) {

				List<RegularShare> caoList =	new ArrayList<RegularShare>();
					
						for(int i = 0; i < 5; i++)
						{
							caoList.add(new RegularShare());
						}
					loadShareMsg(caoList);
			/*		if (!msg.obj.equals(CONSTANTS.LOGIN_ERR_TAG)) {
						System.out.println("msg is 0x41...");
					loadShareMsg((List<RegularShare>) msg.obj);

						
					}*/
				}

				else if (msg.what == 0x42) {

					if (!msg.obj.equals(CONSTANTS.LOGIN_ERR_TAG)) {

						loadAgendaMsg((List<HeartMsg>) msg.obj);
					}
				}
				
				else if(msg.what == 0x43){
					
					loadNewsMsg((List<HeartNews>) msg.obj);
				}
			}

		};

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
		
			return fragment;
		}

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			setHasOptionsMenu(true);

			int p = getArguments().getInt(ARG_SECTION_NUMBER);

			System.out.println(currentP + " now is page...");
			View rootView = null;

			if(p == 1){
				
				rootView = inflater.inflate(R.layout.news_list, null);
				
				newsListView = (ListView) rootView.findViewById(R.id.news_listview);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						List<HeartNews> heartNews = new ArrayList<>();
						
						try {
							heartNews = LoadViewDataUtil.loadHeartNews();
						} catch (IOException e) {
							
							Log.d("czp", "load news in the function activity error");
							e.printStackTrace();
						}
						
						
						Message message = new Message();
						message.what = 0x43;
						message.obj = heartNews;
						handler.sendMessage(message);
						
					}
				}).start();
				
			}
			
			
			if(p == 3)
			{
				rootView = inflater.inflate(R.layout.share_list, container,false);
				
				shareListView = (ListView)rootView.findViewById(R.id.share_list_view);
				
				new Thread(new Runnable() {
					@Override
					public void run() {

					/*	List<RegularShare> result = new ArrayList<>();

						result.add(new RegularShare());
						if (HeartApp.getInstance().oldmanHashMap.size() > 0) {
							List<RegularShare> regularShares = null;

							try {
								regularShares = LoadViewDataUtil
										.loadShare();
							} catch (IOException e) {
								Log.d("czp",
										"in function activity read share err");
								e.printStackTrace();
							}

							result.addAll(regularShares);

						}
*/
					/*	Message message = new Message();
						message.what = 0x41;
					message.obj = result;
						handler.sendMessage(message);*/
						
						handler.sendEmptyMessage(0x41);

					}
				}).start();
				
			}
			
			if (p != 5 && p != 1 && p!=3) {
				rootView = inflater.inflate(R.layout.main, container, false);

				mListView = (ExtendedListView) rootView
						.findViewById(android.R.id.list);

				switch (p) {

				case 2:

					new Thread(new Runnable() {

						@Override
						public void run() {

							List<RegularPosition> result = new ArrayList<RegularPosition>();

							result.add(new RegularPosition());

							if (HeartApp.getInstance().oldmanHashMap.size() > 0) {
								List<RegularPosition> regularPositions = null;
								try {
									regularPositions = LoadViewDataUtil
											.loadPosition();

									result.addAll(regularPositions);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							Message message = new Message();
							message.what = 0x40;
							message.obj = result;
							handler.sendMessage(message);

						}
					}).start();
					break;

				case 4:

					new Thread(new Runnable() {

						@Override
						public void run() {

							List<HeartMsg> result = new ArrayList<>();
							result.add(new HeartMsg());

							if (HeartApp.getInstance().oldmanHashMap.size() > 0) {

								List<HeartMsg> agendas = null;

								try {
									agendas = LoadViewDataUtil.loadAgenda();
								} catch (IOException e) {

									e.printStackTrace();
								}

								result.addAll(agendas);

							}

							Message message = new Message();
							message.what = 0x42;
							message.obj = result;
							handler.sendMessage(message);

						}
					}).start();
					break;
				default:

					/*
					 * PositionAdapter positionAdapter2 = new
					 * PositionAdapter(getActivity(), positionMsgs);
					 * mListView.setAdapter(positionAdapter2);
					 */
					break;
				}

				mListView.setCacheColorHint(Color.TRANSPARENT);
			}

	/*		if(p == 5) {

				rootView = inflater.inflate(R.layout.setting, container, false);

				Button change_pwd = (Button) rootView
						.findViewById(R.id.change_pwd_btn);
				change_pwd.setOnClickListener((OnClickListener) getActivity());

				rootView.findViewById(R.id.fall_record_btn).setOnClickListener(
						(OnClickListener) getActivity());

				rootView.findViewById(R.id.set_emergency_btn)
						.setOnClickListener((OnClickListener) getActivity());

				rootView.findViewById(R.id.feedback_btn).setOnClickListener(
						(OnClickListener) getActivity());

				rootView.findViewById(R.id.check_new_version)
						.setOnClickListener((OnClickListener) getActivity());

				rootView.findViewById(R.id.about_us_btn).setOnClickListener(
						(OnClickListener) getActivity());

				rootView.findViewById(R.id.invite_other2).setOnClickListener(
						(OnClickListener) getActivity());
			}*/

			return rootView;
		}

		private void loadPositionMsg(List<RegularPosition> positionMsgs) {

			PositionAdapter positionAdapter = new PositionAdapter(
					getActivity(), positionMsgs);

			mListView.setAdapter(positionAdapter);

		}

		private void loadShareMsg(List<RegularShare> regularShares) {

			/*ShareAdapter shareAdapter = new ShareAdapter(getActivity(),
					regularShares);
			mListView.setAdapter(shareAdapter);*/
			
			SecondShareAdapter secondShareAdapter = new SecondShareAdapter(getActivity(), regularShares);
			
			
			shareListView.setAdapter(secondShareAdapter);
		}

		private void loadAgendaMsg(List<HeartMsg> msgs) {

			AgendaAdapter agendaAdapter = new AgendaAdapter(getActivity(), msgs);

			mListView.setAdapter(agendaAdapter);
		}

		private void loadNewsMsg(List<HeartNews> newsList){
			
			NewsAdapter newsAdapter = new NewsAdapter(newsList, getActivity());
			
			newsListView.setAdapter(newsAdapter);
		}
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);

			switch (FuncionActivity.currentP) {
			case 0:
				inflater.inflate(R.menu.home_menu, menu);
				break;
			case 1:
				inflater.inflate(R.menu.funcion, menu);
				break;
			case 2:
				inflater.inflate(R.menu.function2, menu);
				break;
			case 3:
				inflater.inflate(R.menu.function3, menu);
				break;
//			case 4:
//				inflater.inflate(R.menu.function4, menu);
//				break;
			}

		}

		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		         if(item.getItemId() == R.id.action_home_refresh){
		             
		        	 List<HeartNews> newsList = new ArrayList<>();
					try {
						newsList = LoadViewDataUtil.refreshHeartNews();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	 NewsAdapter newsAdapter = new NewsAdapter(newsList, getActivity());
		        //	newsAdapter.notifyDataSetChanged();
		        	 newsListView.setAdapter(newsAdapter);
		        	 return true;
		                 }
		         
		      return    super.onOptionsItemSelected(item);
		}
		

	}

	@Override
	public int getCurrentPosition(int position) {

		currentP = position;

		switch (currentP) {
		case 0:
			
			home_menu.setImageResource(R.drawable.home_red);
			homeTextView.setTextColor(Color.rgb(233, 19, 19));
			
			position_menu
			.setImageResource(R.drawable.ic_action_place);
    positionTextView.setTextColor(Color.rgb(17, 17, 17));
	
	share_menu.setImageResource(R.drawable.ic_action_share);

	shareTextView.setTextColor(Color.rgb(17, 17, 17));
	alarm_menu.setImageResource(R.drawable.ic_action_alarms);
    alarmTextView.setTextColor(Color.rgb(17, 17, 17));
//	setting_menu.setImageResource(R.drawable.ic_action_settings);
//    settingTextView.setTextColor(Color.rgb(17, 17, 17));
			break;
		case 1:

			home_menu.setImageResource(R.drawable.home);
			homeTextView.setTextColor(Color.rgb(17,17,17));
			
			position_menu
					.setImageResource(R.drawable.ic_action_place_after_press3);
            positionTextView.setTextColor(Color.rgb(233, 19, 19));
			
			share_menu.setImageResource(R.drawable.ic_action_share);

			shareTextView.setTextColor(Color.rgb(17, 17, 17));
			alarm_menu.setImageResource(R.drawable.ic_action_alarms);
            alarmTextView.setTextColor(Color.rgb(17, 17, 17));
//			setting_menu.setImageResource(R.drawable.ic_action_settings);
//            settingTextView.setTextColor(Color.rgb(17, 17, 17));
			break;
		case 2:

			home_menu.setImageResource(R.drawable.home);
			homeTextView.setTextColor(Color.rgb(17,17,17));
			
			position_menu.setImageResource(R.drawable.ic_action_place);

			positionTextView.setTextColor(Color.rgb(17, 17, 17));
			share_menu
					.setImageResource(R.drawable.ic_action_share_after_press3);

			shareTextView.setTextColor(Color.rgb(233, 19, 19));
			
			alarm_menu.setImageResource(R.drawable.ic_action_alarms);
			alarmTextView.setTextColor(Color.rgb(17, 17, 17));
//			setting_menu.setImageResource(R.drawable.ic_action_settings);
//			 settingTextView.setTextColor(Color.rgb(17, 17, 17));
			 
			break;
		case 3:

			home_menu.setImageResource(R.drawable.home);
			homeTextView.setTextColor(Color.rgb(17,17,17));
			
			position_menu.setImageResource(R.drawable.ic_action_place);

			positionTextView.setTextColor(Color.rgb(17, 17, 17));
			
			share_menu.setImageResource(R.drawable.ic_action_share);

			shareTextView.setTextColor(Color.rgb(17, 17, 17));
			
			alarm_menu
					.setImageResource(R.drawable.ic_action_alarms_after_press3);

			alarmTextView.setTextColor(Color.rgb(233, 19, 19));
			
//			setting_menu.setImageResource(R.drawable.ic_action_settings);
			break;
		case 4:
		
			home_menu.setImageResource(R.drawable.home);
			homeTextView.setTextColor(Color.rgb(17,17,17));
			
			position_menu.setImageResource(R.drawable.ic_action_place);
			positionTextView.setTextColor(Color.rgb(17, 17, 17));
			
			share_menu.setImageResource(R.drawable.ic_action_share);
			shareTextView.setTextColor(Color.rgb(17, 17, 17));
            
			alarm_menu.setImageResource(R.drawable.ic_action_alarms);
			alarmTextView.setTextColor(Color.rgb(17, 17, 17));
            
//			setting_menu
//					.setImageResource(R.drawable.ic_action_settings_after_press3);
//			
//			settingTextView.setTextColor(Color.rgb(233, 19, 19));
			
			break;
		default:
			break;
		}
		return 0;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.set_emergency_btn:

			Choose2Dialog.showDialog(this, R.id.set_emergency_btn);

			break;
		case R.id.check_new_version:
			Toast.makeText(this, "目前是最新版本", Toast.LENGTH_LONG).show();
			break;
		case R.id.about_us_btn:
			startActivity(new Intent(FuncionActivity.this, AboutActivity.class));
			break;
		case R.id.feedback_btn:
			startActivity(new Intent(FuncionActivity.this,
					FeedBackActivity.class));
			break;
		case R.id.invite_other2:
			Log.d("czp", "run the invite");
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			startActivityForResult(intent, INVITE_CONTRACT);
         break;
		case R.id.change_pwd_btn:
			startActivity(new Intent(FuncionActivity.this,
					ChangePwdActivity.class));
			break;
	
		default:
			break;
		}

	}



	
	
	public class TabClickListener implements View.OnClickListener{
		 
		  private int index = 0;

		public TabClickListener(int index) {
			super();
			this.index = index;
		}

		@Override
		public void onClick(View view) {
		  
			mViewPager.setCurrentItem(index);
			
		}
		  
		  
	}
	
	@Override
	public void onPause(){
		
		super.onPause();
		
		
	}
	
	@Override
	public void onStop(){
		
		super.onStop();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		
		HeartApp.getInstance().currentPage = currentP;
		
		outState.putInt("currentPage", currentP);
		
		Log.d("czp", "current page :  onSaveInstanceState...." + outState.getInt("currentPage"));
		
		super.onSaveInstanceState(outState);
		
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
          
		Log.d("czp", "current page :  onRestoreInstanceState....");
		  super.onRestoreInstanceState(savedInstanceState);
		 }
}
