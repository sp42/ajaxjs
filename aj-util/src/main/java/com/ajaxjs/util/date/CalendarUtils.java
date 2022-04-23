package com.ajaxjs.util.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Calendar 工具类
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class CalendarUtils {
	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

	public static boolean isAfter(Calendar date) {
		return Calendar.getInstance().after(date);
	}
}
