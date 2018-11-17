package com.ajaxjs.user.sign_perday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUtil {
	/**
	 * 返回今天是当年的第几周
	 * 
	 * @return 周数
	 */
	public static int getWEEK_OF_YEAR() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 指定日期的当年的第几周
	 * 
	 * @return 周数
	 */
	public static int getWEEK_OF_YEAR(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取过去第几天的日期
	 * 
	 * @param daysAgo
	 *            过去第几天
	 * @return
	 */
	public static Date getDayAgo(int daysAgo) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - daysAgo);

		return calendar.getTime();
	}

	/**
	 * 获取过去第几天到今天的所有日期
	 * 
	 * @param daysAgo
	 *            过去第几天
	 * @return
	 */
	public static Date[] getDaysAgo(int daysAgo) {
		List<Date> pastDaysList = new ArrayList<>();
		for (int i = 0; i < daysAgo; i++) {
			pastDaysList.add(getDayAgo(i));
		}

		Collections.reverse(pastDaysList); // 翻转一下
		return pastDaysList.toArray(new Date[pastDaysList.size()]);
	}

	/**
	 * 获取过去第几天所在的周数
	 * 
	 * @param daysAgo
	 *            过去第几天
	 * @return
	 */
	public static Integer[] getWeeksByDate(int daysAgo) {
		Date[] dates = getDaysAgo(daysAgo);
		int[] _weeks = new int[dates.length];

		for (int i = 0; i < dates.length; i++)
			_weeks[i] = getWEEK_OF_YEAR(dates[i]);

		return unique(_weeks);
	}

	/**
	 * 去重
	 * 
	 * @param array
	 */
	static Integer[] unique(int[] array) {
		List<Integer> list = new ArrayList<>();

		Map<Integer, Boolean> map = new HashMap<>();
		for (int i : array) {
			if (!map.containsKey(i)) {
				map.put(i, true);
				list.add(i);
			}
		}

		return list.toArray(new Integer[list.size()]);
	}

	/**
	 * 默认 7 日之前
	 * 
	 * @return
	 */
	public static Date[] getDaysAgo() {
		return getDaysAgo(7);
	}
}
