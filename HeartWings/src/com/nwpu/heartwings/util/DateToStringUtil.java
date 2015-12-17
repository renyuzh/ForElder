package com.nwpu.heartwings.util;

import java.util.Calendar;
import java.util.Date;


public class DateToStringUtil {

	public static String ConvertToString(Date date){
		
		StringBuilder sb = new StringBuilder();
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		sb.append(calendar.get(Calendar.YEAR) + "年");
		
		int month = calendar.get(Calendar.MONTH) + 1;
		
		sb.append(month+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日     ");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY) + "时" + calendar.get(Calendar.MINUTE) + "分");
		
		return sb.toString();
		
	}
}
