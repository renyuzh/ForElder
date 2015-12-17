package com.nwpu.heartwings.agenda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nwpu.heartwings.R;
import com.nwpu.heartwings.bean.TimePair;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class MedicineItemAdapter extends BaseAdapter {

	private List<TimePair> colckList;
	private Context context;
	
	private String tag;
	
	public static Map<Integer,String> numMap = new HashMap<>();
	
	static{
		numMap.put(1, "第一次");numMap.put(2, "第二次");numMap.put(3, "第三次");numMap.put(4, "第四次");
		numMap.put(5, "第五次");numMap.put(6, "第六次");numMap.put(7, "第七次");numMap.put(8, "第八次");
		numMap.put(9, "第九次");numMap.put(10, "第十次");numMap.put(11, "第11次");numMap.put(12, "第12次");
		numMap.put(13, "第13次");numMap.put(14, "第14次");numMap.put(15, "第15次");numMap.put(16, "第16次");
		numMap.put(17, "第17次");numMap.put(18, "第18次");numMap.put(19, "第19次");numMap.put(20, "第20次");
		numMap.put(21, "第21次");numMap.put(22, "第22次");
	}
	public MedicineItemAdapter(List<TimePair> colckList, Context context,String tag) {
		super();
		this.colckList = colckList;
		this.context = context;
		this.tag = tag;
	}

	@Override
	public int getCount() {
		
		return colckList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return colckList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final TimePair timePair = colckList.get(position);
		
		convertView = LayoutInflater.from(context).inflate(R.layout.medicine_each_item, null);
		
		TextView the_th = (TextView)convertView.findViewById(R.id.agenda_the_th);
		the_th.setText(numMap.get(position+1) );
		if(tag.equals("medicine"))
		{
			the_th.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pill, 0, 0, 0);
		}
		else if(tag.equals("drink"))
		{
			the_th.setCompoundDrawablesWithIntrinsicBounds(R.drawable.water, 0, 0, 0);
		}
		
		else if(tag.equals("other")){
			the_th.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom, 0, 0, 0);
		}
		TextView agenda_time_clock = (TextView)convertView.findViewById(R.id.agenda_time_clock);
		agenda_time_clock.setText(timePair.getHour()+"时"+timePair.getMinute()+"分");
		
		agenda_time_clock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				  new TimePickerDialog(context, 
						  new OnTimeSetListener() {
							
							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								
								timePair.setHour(hourOfDay);
								timePair.setMinute(minute);
								
								notifyDataSetChanged();
							}
						}, timePair.getHour(), timePair.getMinute(), true).show();
			}
		});
		
		
		ImageView deleteImageView = (ImageView)convertView.findViewById(R.id.delete_icon);
		
		deleteImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				colckList.remove(position);
				notifyDataSetChanged();
				
			}
		});
		return convertView;
	}

}
