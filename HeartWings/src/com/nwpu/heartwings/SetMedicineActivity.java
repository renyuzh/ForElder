package com.nwpu.heartwings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.heart.bean.HeartMsg;
import com.heart.bean.TakePillsEvent;
import com.nwpu.heartwings.agenda.MedicineItemAdapter;
import com.nwpu.heartwings.bean.TimePair;
import com.nwpu.heartwings.util.CONSTANTS;
import com.nwpu.heartwings.util.CheckNetWork;
import com.nwpu.heartwings.util.ChooseDialogUtil;
import com.nwpu.heartwings.util.DialogUtil;
import com.nwpu.heartwings.util.LocalFileUtil;
import com.nwpu.heartwings.util.MSGUtil;
import com.nwpu.heartwings.util.TimeUtil;
import com.tencent.tencentmap.streetviewsdk.ad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Build;

@SuppressLint("HandlerLeak")
public class SetMedicineActivity extends ActionBarActivity {

	MedicineItemAdapter adapter = null;

	EditText titlEditText;

	List<TimePair> timePairs = new ArrayList<>();

	private boolean hasSelected = false;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 0x100) {

				adapter.notifyDataSetChanged();
			}
			else if(msg.what == 0x101){
				
				DialogUtil.showDialog(SetMedicineActivity.this, "提醒设置完成");
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_medicine);

		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		final String whichIcon = getIntent().getStringExtra("whichicon");

		ListView listView = (ListView) findViewById(R.id.agenda_list);

		titlEditText = (EditText) findViewById(R.id.medicine_title);

		switch (whichIcon) {
		case "medicine":

			timePairs.add(new TimePair(8, 30));
			timePairs.add(new TimePair(13, 0));
			timePairs.add(new TimePair(18, 30));

			adapter = new MedicineItemAdapter(timePairs, this,"medicine");

			listView.setAdapter(adapter);

			break;

		case "drink":

			
			timePairs.add(new TimePair(8, 30));
			timePairs.add(new TimePair(10, 10));
			timePairs.add(new TimePair(12, 25));
			timePairs.add(new TimePair(18, 30));

			adapter = new MedicineItemAdapter(timePairs, this,"drink");

			listView.setAdapter(adapter);
			
			break;
		case "other":
			timePairs.add(new TimePair(6, 30));
			timePairs.add(new TimePair(13, 30));
			timePairs.add(new TimePair(20, 30));

			adapter = new MedicineItemAdapter(timePairs, this,"other");

			listView.setAdapter(adapter);
			
			break;
		default:
			break;
		}

		ImageView addImageView = (ImageView) findViewById(R.id.add_one);
		addImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				switch (whichIcon) {
				case "medicine":
					if (timePairs.size() > 5)
						return;
					break;
				case "drink":
					if(timePairs.size() > 10)
					{
						return;
					}
				break;
				case "other":
					if(timePairs.size() > 20){
						return;
					}
				break;
				default:
					break;
				}
				timePairs.add(new TimePair(12, 12));

				handler.sendEmptyMessage(0x100);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.set_medicine, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.send_agenda_medicine) {

			if (!CheckNetWork.isNetAvailable(this)) {

				DialogUtil.showDialog(this, CONSTANTS.NETWORKERR);
				return true;
			}

			if (TextUtils.isEmpty(titlEditText.getText())) {

				DialogUtil.showDialog(this, "提醒事件不能为空");
				return true;
			}

			if (timePairs.size() == 0) {
				DialogUtil.showDialog(this, "至少需设置一个提醒时间");
				return true;
			}

			if (!hasSelected) {
				if (ChooseDialogUtil.showChoose(this) == null) {
					DialogUtil.showDialog(this, "请先前往设置页面绑定老人");
					return true;
				}

				hasSelected = true;

				return true;
			}

			final HeartMsg heartMsg = new HeartMsg();
			heartMsg.setCreateTime(System.currentTimeMillis());
			final String thisClient = LocalFileUtil.getThisClient(this);
			heartMsg.setFromUserName(thisClient);
			heartMsg.setMsgType("take_pills");
			heartMsg.setToUserName(ChooseDialogUtil.selectdPhone);

			TakePillsEvent takePillsEvent = convertToTakePillsEvent(
					titlEditText.getText().toString(), timePairs);
			heartMsg.setMsgContent(JSON.toJSONString(takePillsEvent));
			
			System.out.println(JSON.toJSONString(takePillsEvent));
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (!MSGUtil.session.isConnected()) {
						MSGUtil.session = MSGUtil.getIOSession(thisClient);
					}

					MSGUtil.session.write(JSON.toJSONString(heartMsg));
					handler.sendEmptyMessage(0x101);
					MSGUtil.session.getCloseFuture().awaitUninterruptibly();

				}
			}).start();

			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private TakePillsEvent convertToTakePillsEvent(String content,
			List<TimePair> timePairs) {

		TakePillsEvent takePillsEvent = new TakePillsEvent();

		takePillsEvent.setContent(content);

		List<Integer> hours = new ArrayList<>();
		List<Integer> minutes = new ArrayList<>();
		for (TimePair timePair : timePairs) {

			hours.add(timePair.getHour());
			minutes.add(timePair.getMinute());
		}

		takePillsEvent.setTheTimes_hour(hours);
		takePillsEvent.setTheTimes_minute(minutes);
        takePillsEvent.setId((int) (System.currentTimeMillis() % 2077373681));
        
		return takePillsEvent;

	}

}
