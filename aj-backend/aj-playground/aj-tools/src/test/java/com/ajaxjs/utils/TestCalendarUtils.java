package com.ajaxjs.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import static com.ajaxjs.utils.CalendarUtils.*;
import static com.ajaxjs.utils.CalendarUtils.getMonthsBetween;

public class TestCalendarUtils {
    @Test
    public void test() throws ParseException {
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
