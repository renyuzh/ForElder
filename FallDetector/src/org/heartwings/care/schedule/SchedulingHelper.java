package org.heartwings.care.schedule;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.heartwings.care.Constants;
import org.heartwings.care.MainActivity;
import org.heartwings.care.ScheduleNoticeActivity;
import org.heartwings.care.util.FileUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;

/**
 * @author Inno520
 * 关于日程安排的相关功能
 */
public class SchedulingHelper {
	private List<SingleEvent> singleEvents;
	private List<PeriodEvent> periodEvents;
	private AlarmManager alarmManager;

	private static SchedulingHelper schedulingHelper = null;
	private Context context;

	public static SchedulingHelper getSchedulingHelper() {
		if (schedulingHelper == null) {
			schedulingHelper = new SchedulingHelper(
					MainActivity.getTheApplicationContext());
		}
		return schedulingHelper;
	}

	public void addNewEvent(SingleEvent ev) {
		singleEvents.add(ev);
		registerNewEvent(ev);
		Log.i("Schedule", "New event registered: " + ev.getContent());
	}

	public void addNewEvent(PeriodEvent ev) {
		periodEvents.add(ev);
		registerNewEvent(ev);
	}

	public void registerNewEvent(SingleEvent ev) {
		Intent intent = new Intent(context, ScheduleNoticeActivity.class);
		intent.putExtra(ScheduleConstants.NAME_CONTENTS, ev.getContent());
		intent.putExtra(ScheduleConstants.NAME_BEGIN_TIME, ev.getTimestamp());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ev.getTimestamp());
		Log.i("Schedule", "Scheduled at " + calendar.getTime().toString());
		alarmManager.set(AlarmManager.RTC, ev.getTimestamp(),
				PendingIntent.getActivity(context, 0, intent, 0));
	}

	public void registerNewEvent(PeriodEvent ev) {
		Intent intent = new Intent(context, ScheduleNoticeActivity.class);
		intent.putExtra(ScheduleConstants.NAME_CONTENTS, ev.getContent());
		intent.putExtra(ScheduleConstants.NAME_BEGIN_TIME, ev.getFromTime());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmManager.setRepeating(AlarmManager.RTC, ev.getFromTime(),
				ev.getPerioud() * 86400000,
				PendingIntent.getActivity(context, 0, intent, 0));
	}

	private SchedulingHelper(Context context) {
		this.context = context;
		alarmManager = (AlarmManager) context
				.getSystemService(Service.ALARM_SERVICE);
		try {
			FileInputStream fisSingleEvent = context
					.openFileInput(Constants.FILENAME_SINGLE_EVENT);
			String singleEventContent = FileUtil
					.readFileAsString(fisSingleEvent);
			singleEvents = JSON.parseArray(singleEventContent,
					SingleEvent.class);
			FileInputStream fisPeriodEvent = context
					.openFileInput(Constants.FILENAME_PERIOD_EVENT);
			String periodEventContent = FileUtil
					.readFileAsString(fisPeriodEvent);
			periodEvents = JSON.parseArray(periodEventContent,
					PeriodEvent.class);
			fisSingleEvent.close();
			fisPeriodEvent.close();
		} catch (FileNotFoundException e) {
			Log.i("Schedule", "File not found, it's ok.");
			singleEvents = new ArrayList<SingleEvent>();
			periodEvents = new ArrayList<PeriodEvent>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		save();
		super.finalize();
	}

	private void save() {
		try {
			FileOutputStream fosSingle = context.openFileOutput(
					Constants.FILENAME_SINGLE_EVENT, Context.MODE_PRIVATE);
			FileOutputStream fosPeriod = context.openFileOutput(
					Constants.FILENAME_PERIOD_EVENT, Context.MODE_PRIVATE);
			fosSingle.write(JSON.toJSONString(singleEvents).getBytes());
			fosPeriod.write(JSON.toJSONString(periodEvents).getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
