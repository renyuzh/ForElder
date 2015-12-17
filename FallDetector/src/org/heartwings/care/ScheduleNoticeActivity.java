package org.heartwings.care;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.heartwings.care.schedule.ScheduleConstants;
import org.heartwings.care.schedule.SchedulingHelper;
import org.heartwings.care.schedule.SingleEvent;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScheduleNoticeActivity extends Activity {
	private TextView contentTextView;
	private TextView timeTextView;
	private Button ackButton;
	private Button delayButton;
	private Button nakButton;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bg));
		actionBar.setTitle("日程管理");

		setContentView(R.layout.activity_schedule_notice);
		contentTextView = (TextView) findViewById(R.id.scheduleContent);
		timeTextView = (TextView) findViewById(R.id.scheduleTime);
		ackButton = (Button) findViewById(R.id.scheduleAckButton);
		delayButton = (Button) findViewById(R.id.scheduleDelayButton);
		nakButton = (Button) findViewById(R.id.scheduleNakButton);
		Intent startIntent = getIntent();
		final String contents = startIntent
				.getStringExtra(ScheduleConstants.NAME_CONTENTS);
		final long dateTime = startIntent.getLongExtra(
				ScheduleConstants.NAME_BEGIN_TIME, -1);
		String timeInString = simpleDateFormat.format(new Date(dateTime));
		;
		contentTextView.setText(contents);
		timeTextView.setText(timeInString);

		ackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		delayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SchedulingHelper.getSchedulingHelper().registerNewEvent(
						new SingleEvent(contents, dateTime + 600000));
				finish();
			}
		});
		nakButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.schedule_notice, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
