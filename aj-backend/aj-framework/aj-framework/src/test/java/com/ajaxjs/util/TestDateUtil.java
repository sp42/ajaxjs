package com.ajaxjs.util;


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.ajaxjs.util.DateUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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


}
