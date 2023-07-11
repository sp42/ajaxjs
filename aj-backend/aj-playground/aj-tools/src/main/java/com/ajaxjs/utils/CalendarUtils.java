package com.ajaxjs.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Calendar 工具类
 * 获取每个月有几周，并且每周的开始日期和结束日期 国内都是以周一开始，注：即使第一天是周末，也算一周
 *
 * @author 谢辉 <a href="https://blog.csdn.net/qq_41995919/article/details/115756233">...</a>
 */
public class CalendarUtils {
    /**
     * Date 转换为 Calendar
     *
     * @param date Date
     * @return Calendar
     */
    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    /**
     * @param date
     * @return
     */
    public static boolean isAfter(Calendar date) {
        return Calendar.getInstance().after(date);
    }

    /**
     * 过去了多少分钟
     *
     * @param date 比较的日期
     * @return 过去了多少分钟
     */
    public static int passedMins(Date date) {
        long diff = new Date().getTime() - date.getTime();
        return new Long((diff / 1000) / 60).intValue();
    }

    /**
     * 获取指定月份有几周，每周的开始日期和结束日期
     *
     * @param yearMonth
     * @return
     */
    public static Map<Integer, WeekData> weeks(YearMonth yearMonth) {
        // 获取指定月份的第一天
        LocalDate start = LocalDate.now().with(yearMonth).with(TemporalAdjusters.firstDayOfMonth());
        // 获取指定月份的最后一天
        LocalDate end = LocalDate.now().with(yearMonth).with(TemporalAdjusters.lastDayOfMonth());

        return Stream.iterate(start, localDate -> localDate.plusDays(1l)).limit(ChronoUnit.DAYS.between(start, end) + 1)
                // DayOfWeek.MONDAY 这里是指定周一（MONDAY）为一周的开始
                .collect(Collectors.groupingBy(localDate -> localDate.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()),
                        Collectors.collectingAndThen(Collectors.toList(), WeekData::new)));
    }

    /**
     * 获取指定时期的后面月份的第一天开始时间
     *
     * @param startDate 开始时间
     * @param month     第几个月
     * @return
     */
    public static Date getMonthDate(Date startDate, int month) {
        LocalDateTime time = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusMonths(month);

        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static class WeekData {
        // 一周的开始时间
        private final LocalDate start;
        // 一周的结束时间
        private final LocalDate end;

        public WeekData(List<LocalDate> localDates) {
            this.start = localDates.get(0);
            this.end = localDates.get(localDates.size() - 1);
        }

        @Override
        public String toString() {
            return this.start + "~" + this.end;
        }
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DATE);
    }

    /**
     * 返回日期的月份，1-12,即yyyy-MM-dd中的MM * * @param date * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回日期的年,即yyyy-MM-dd中的yyyy * * @param date * Date * @return int
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int calDiffMonth(String startDate, String endDate) {
        int result = 0;

        try {
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM");
            Date start = sfd.parse(startDate);
            Date end = sfd.parse(endDate);
            int startYear = getYear(start);
            int startMonth = getMonth(start);
            int startDay = getDay(start);
            int endYear = getYear(end);
            int endMonth = getMonth(end);
            int endDay = getDay(end);

            if (startDay > endDay) { // 1月17 大于 2月28
                if (endDay == getDaysOfMonth(getYear(new Date()), 2)) { // 也满足一月
                    result = (endYear - startYear) * 12 + endMonth - startMonth;
                } else
                    result = (endYear - startYear) * 12 + endMonth - startMonth - 1;
            } else
                result = (endYear - startYear) * 12 + endMonth - startMonth;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<String> getBetweenDateByMonth(String startDate, String endDate) {
        List<String> results = new ArrayList<>();
        // 判断两个月份的间隔数量
        int count = calDiffMonth(startDate, endDate);

        // 如果两个日期相等
        if (count == 0) {
            results.add(startDate);
            results.add(endDate);
            return results;
        }

        if (count > 0) {
            YearMonth now = YearMonth.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM"));
            results.add(startDate);

            for (int i = 1; i <= count; i++) {
                // 参数-1 是获取下个月的月份
                YearMonth nextMonths = now.minusMonths(-i);
                results.add(nextMonths.toString());
            }

            return results;
        }

        if (count < 0) {
            YearMonth now = YearMonth.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM"));
            results.add(startDate);
            count = Math.abs(count);

            for (int i = 1; i <= count; i++) {
                // 参数1 是获取上个月的月份
                YearMonth nextMonths = now.minusMonths(i);
                results.add(nextMonths.toString());
            }

            Collections.reverse(results);

            return results;
            // return results;
        }

        return results;
    }

    /**
     * 根据指定日期开始获取指定数量的下几个月 注：包含入参的月份
     *
     * @param startDate 开始日期（yyyy-MM）
     * @param num       指定数量
     * @return 下几个月
     */
    public static List<String> getNextMonthByNum(String startDate, int num) {
        return getNeighborMonthByNum(startDate, num, true);
    }

    /**
     * 根据指定日期开始获取指定数量的前几个月 注：包含入参的月份
     *
     * @param startDate 开始日期（yyyy-MM）
     * @param num       指定数量
     * @return 前几个月
     */
    public static List<String> getBeforeMonthByNum(String startDate, int num) {
        return getNeighborMonthByNum(startDate, num, false);
    }

    private static List<String> getNeighborMonthByNum(String startDate, int num, boolean isNext) {
        YearMonth now = YearMonth.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM"));
        List<String> results = new ArrayList<>();
        results.add(now.toString());

        if (num <= 0)
            return results;

        for (int i = 1; i <= num; i++) {
            YearMonth nextMonths;

            if (isNext) {
                nextMonths = now.minusMonths(-i);
                results.add(nextMonths.toString());
            } else {
                nextMonths = now.plusMonths(-i);
                results.add(0, nextMonths.toString());
            }
        }

        return results;
    }

    /**
     * 获取两个日期之间的所有日期 (年月日)
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getBetweenDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 声明保存日期集合
        List<String> list = new ArrayList<>();

        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            // 用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();

            while (startDate.getTime() <= endDate.getTime()) {
                list.add(sdf.format(startDate));// 把日期添加到集合// 把日期添加到集合
                calendar.setTime(startDate);// 设置日期
                calendar.add(Calendar.DATE, 1);// 把日期增加一天
                startDate = calendar.getTime();// 获取增加后的日期
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取指定月份
     *
     * @param months
     * @return
     */
    public static List<String> getMonthsBetween(int months) {
        List<String> listMonth = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();

        for (int i = 1; i <= months; i++) {
            c.setTime(new Date());
            c.add(Calendar.MONTH, -i);
            Date m = c.getTime();
            listMonth.add(sdf.format(m));
        }

        Collections.reverse(listMonth);

        return listMonth;
    }

    /**
     * 获取前几天的日期
     *
     * @param days
     * @return
     */
    public static List<String> getDaysBetwwen(int days) {
        Calendar calendar = Calendar.getInstance();
        List<String> listDays = new ArrayList<>();
        long oneDay = 1000 * 60 * 60 * 24l; // 1天

        for (int i = 0; i < days; i++) {
            Date d = new Date(calendar.getTimeInMillis() - oneDay * i);
            DateFormat df = new SimpleDateFormat("MM月dd日");
            // System.out.println(df.format(d));
            listDays.add(df.format(d));
        }

        Collections.reverse(listDays); // 倒序排列

        return listDays;
    }

    public static String getLast12Months(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();

        return sdf.format(m);
    }
}
