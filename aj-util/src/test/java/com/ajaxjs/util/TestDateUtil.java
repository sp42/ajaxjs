package com.ajaxjs.util;

import static com.ajaxjs.util.date.CalendarUtils.getBetweenDate;
import static com.ajaxjs.util.date.CalendarUtils.getBetweenDateByMonth;
import static com.ajaxjs.util.date.CalendarUtils.getDaysBetwwen;
import static com.ajaxjs.util.date.CalendarUtils.getMonthDate;
import static com.ajaxjs.util.date.CalendarUtils.getMonthsBetween;
import static com.ajaxjs.util.date.CalendarUtils.weeks;
import static com.ajaxjs.util.date.DateUtil.DATE_FORMAT;
import static com.ajaxjs.util.date.DateUtil.formatDate;
import static com.ajaxjs.util.date.DateUtil.formatDateShorter;
import static com.ajaxjs.util.date.DateUtil.now;
import static com.ajaxjs.util.date.DateUtil.object2Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

public class TestDateUtil {
	@Test
	public void testNow() {
		assertNotNull(now()); // 返回当前时间的 YYYY-MM-dd HH:MM:ss 字符串类型
		assertNotNull(now(DATE_FORMAT)); // 返回当前时间，并对当前时间进行格式化
	}

	@Test
	public void testFormat() {
		Date date = object2Date("2017-07-25 11:16:09");

		assertEquals("Tue Jul 25 11:16:09 GMT+08:00 2017", date.toString());
		assertEquals("2017-07-25 11:16:09", formatDate(date));
		assertEquals("2017-07-25 11:16", formatDateShorter(date));

		assertEquals(date.getTime(), object2Date(date).getTime());
		assertEquals(date.getTime(), object2Date(date.getTime()).getTime());
		assertEquals(date.getTime(), object2Date("2017-07-25 11:16:09").getTime()); // 转换字符串类型的日期到 Date 类型
	}

	/**
	 * 获取昨天的日期
	 */
	public static void getYesterday() {
		SimpleDateFormat smdate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);

		String date = smdate.format(calendar.getTime());
		System.out.println(date);
	}

	/**
	 * 获取明天的日期
	 */
	public void getTomorrow() {
		SimpleDateFormat smdate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		String date = smdate.format(calendar.getTime());
		System.out.println(date);
	}

	public void main() throws Exception {
		System.out.println(weeks(YearMonth.of(2021, 4)));

		String targetStr = YearMonth.now().toString() + "-01";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = getMonthDate(sdf.parse(targetStr), 2);
		System.out.println("后2个月的时间:" + sdf.format(date));

		List<String> betweenDateByMonth = getBetweenDateByMonth("2020-04", "2020-08");
		System.out.println(betweenDateByMonth.toString());

		String startStr = "2022-02-26";
		String endStr = "2022-03-09";
		List<String> list = getBetweenDate(startStr, endStr);
		System.out.println(list);

		List<String> daysBetwwen = getDaysBetwwen(7);
		System.out.println(daysBetwwen.toString());
		List<String> monthsBetween = getMonthsBetween(7);
		// Collections.sort(monthsBetween); // 顺序排列
		// Collections.shuffle(monthsBetween); // 混乱的意思
		// Collections.binarySearch(monthsBetween, " a5 ");
		// Collections.reverse(monthsBetween);// 倒序排列
		System.out.println(monthsBetween.toString());
	}
}
