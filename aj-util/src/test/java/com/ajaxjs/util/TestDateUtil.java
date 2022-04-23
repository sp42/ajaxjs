package com.ajaxjs.util;

import static com.ajaxjs.util.date.DateUtil.*;
import static com.ajaxjs.util.date.AutoDate.getAutoDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Date;

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
	 * 获取时间格式
	 */
	public void testDataAutoCast() throws ParseException {
		System.out.println(getAutoDate("1987-11-04"));
		System.out.println(getAutoDate("1987-11-04 12:50"));
		System.out.println(getAutoDate("1987-11-04 12:50:15"));
		System.out.println(getAutoDate("1987-11-04 12:50:15.000"));
		System.out.println(getAutoDate("19871104"));
		System.out.println(getAutoDate("04/11/1987"));
	}
}
