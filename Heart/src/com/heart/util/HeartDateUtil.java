package com.heart.util;

import java.util.Calendar;
import java.util.Date;

public class HeartDateUtil {

	public static Date getStartDateOfToday(){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		return calendar.getTime();
	}
}
