package com.nwpu.heartwings.agenda;

import java.util.Calendar;

import com.nwpu.heartwings.agenda.DateTimePicker.OnDateTimeChangedListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

public class DateTimePickerDialog extends AlertDialog implements OnClickListener 
{
    private Calendar mDate = Calendar.getInstance();
    private boolean mIs24HourView;
    private OnDateTimeSetListener mOnDateTimeSetListener;
    private DateTimePicker mDateTimePicker;

    public interface OnDateTimeSetListener 
    {
        void OnDateTimeSet(AlertDialog dialog, long date);
    }

    @SuppressWarnings("deprecation")
	public DateTimePickerDialog(Context context, long date) 
    {
        super(context);
        mDateTimePicker = new DateTimePicker(context);
        setView(mDateTimePicker);
        mDateTimePicker.setOnDateTimeChangedListener(new OnDateTimeChangedListener() 
        {
            public void onDateTimeChanged(DateTimePicker view, int year, int month,int dayOfMonth, int hourOfDay, int minute) 
            {
                mDate.set(Calendar.YEAR, year);
                mDate.set(Calendar.MONTH, month);
                mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDate.set(Calendar.MINUTE, minute);
                updateTitle(mDate.getTimeInMillis());
            }
        });
        mDate.setTimeInMillis(date);
        mDate.set(Calendar.SECOND, 0);
        mDateTimePicker.setCurrentDate(mDate.getTimeInMillis());
        setButton("设置", this);
        setButton2("取消", (OnClickListener)null);
        mIs24HourView=DateFormat.is24HourFormat(this.getContext());//判断是不是24小时 
        updateTitle(mDate.getTimeInMillis());
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener callBack)
    {
        mOnDateTimeSetListener = callBack;
    }

    private void updateTitle(long date) 
    {
    	    int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME;
    	 
            flag |= mIs24HourView ? DateUtils.FORMAT_24HOUR : DateUtils.FORMAT_24HOUR;
            setTitle(DateUtils.formatDateTime(this.getContext(), date, flag));
    }

    public void onClick(DialogInterface arg0, int arg1)
    {
        if (mOnDateTimeSetListener != null) 
        {
            mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis());
        }
    }

}