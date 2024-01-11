/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * 日期格式化
 * SimpleDateFormat 不是线程安全的，Java 8 之后尽量使用 java.time.DateTimeFormatter
 *
 * @author sp42 frank@ajaxjs.com
 */
public class DateUtil {
    /**
     * 常见的日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd HH:mm";

    public static final String DATE_FORMAT_SHORTER = "yyyy-MM-dd";

    /**
     * 对输入的时间进行格式化，采用格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 输入的时间
     * @return 转换到 yyyy-MM-dd HH:mm:ss 格式的时间
     */
    public static String formatDate(Date date) {
        return simpleDateFormatFactory(DATE_FORMAT).format(date);
    }

    /**
     * 对输入的时间进行格式化，采用格式 YYYY-MM-dd HH:mm
     *
     * @param date 输入的时间
     * @return 转换到 YYYY-MM-dd HH:MM 格式的时间
     */
    public static String formatDateShorter(Date date) {
        return simpleDateFormatFactory(DATE_FORMAT_SHORT).format(date);
    }

    /**
     * 返回当前时间，并对当前时间进行格式化
     *
     * @param format 期望的格式
     * @return 转换到期望格式的当前时间
     */
    public static String now(String format) {
        return simpleDateFormatFactory(format).format(new Date());
    }

    /**
     * 返回当前时间的 YYYY-MM-dd HH:MM 字符串类型
     *
     * @return 当前时间
     */
    public static String now() {
        return now(DATE_FORMAT_SHORT);
    }

    /**
     * 返回当前时间的 YYYY-MM-dd HH:MM:ss 字符串类型
     *
     * @return 当前时间
     */
    public static String now2() {
        return now(DATE_FORMAT);
    }

    /**
     * 年月日的正则表达式，例如 2016-08-18
     */
    private final static String DATE_YEAR = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

    /**
     * 正则实例
     */
    private final static Pattern DATE_YEAR_PATTERN = Pattern.compile(DATE_YEAR);

    /**
     * 一般日期判断的正则
     */
    private final static Pattern DATE_PATTERN = Pattern.compile(DATE_YEAR + " ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");

    /**
     * 支持任意对象转换为日期类型
     *
     * @param obj 任意对象
     * @return 日期类型对象，返回 null 表示为转换失败
     */
    public static Date object2Date(Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof Date)
            return (Date) obj;
        else if (obj instanceof Long)
            return new Date((Long) obj);
        else if (obj instanceof Integer)
            return object2Date(Long.parseLong(obj + "000")); /* 10 位长 int，后面补充三个零为13位 long 时间戳 */
        else if (obj instanceof String) {
            String str = obj.toString();

            if (!StringUtils.hasText(str))
                return null;

            try {
                if (DATE_PATTERN.matcher(str).matches())
                    return simpleDateFormatFactory(DATE_FORMAT).parse(str);
                else if (DATE_YEAR_PATTERN.matcher(str).matches())
                    return simpleDateFormatFactory(DATE_FORMAT_SHORTER).parse(str);
                else
                    return simpleDateFormatFactory(DATE_FORMAT_SHORT).parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // 输入日期不合法，不能转为日期类型。请重新输入日期字符串格式类型，或考虑其他方法。
        }

        return null;
    }

    /**
     * SimpleDateFormat caches
     */
    private final static Map<String, SimpleDateFormat> FORMATTERS = new ConcurrentHashMap<>();

    /**
     * 对输入的时间进行格式化 有 SimpleDateFormat 缓存 格式化的另外一种方法
     *
     * <pre>
     * {@code
     *   new SimpleDateFormat(format).format(System.currentTimeMillis());
     *  }
     * </pre>
     * <p>
     * 返回 SimpleDateFormat 的工厂函数
     *
     * @param format 日期格式
     * @return 格式日期的对象
     */
    public static SimpleDateFormat simpleDateFormatFactory(String format) {
        if (!FORMATTERS.containsKey(format))
            FORMATTERS.put(format, new SimpleDateFormat(format));

        return FORMATTERS.get(format);
    }

    /**
     * 请求的时间戳，格式必须符合 RFC1123 的日期格式
     *
     * @return 当前日期
     */
    public static String getGMTDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        return format.format(new Date());
    }

    /**
     * 将字符串转换为LocalDateTime对象
     *
     * @param str    字符串日期
     * @param format 格式化字符串
     * @return 转换后的LocalDateTime对象，如果转换失败则返回null
     */
    public static LocalDateTime string2localDate(String str, String format) {
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(format));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将字符串转换为LocalDateTime类型的日期对象（短格式）
     *
     * @param str 待转换的字符串
     * @return 转换后的LocalDateTime类型的日期对象
     */
    public static LocalDateTime string2localDateShort(String str) {
        return string2localDate(str, DATE_FORMAT_SHORT);
    }

    public static LocalDateTime string2localDate(String str) {
        return string2localDate(str, DATE_FORMAT);
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDate2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static ZonedDateTime getZone(Date date) {
        Instant instant;
        /*
            java.sql.Date仅支持日期组件（日期、月份、年份）。它不支持时间组件（小时、分钟、秒、毫秒）。
            toInstant需要 Date 和 Time 组件，
            因此 java.sql.Date 实例上的 toInstant 会引发 UnsupportedOperationException 异常
        */
        if (date instanceof java.sql.Date)
            instant = Instant.ofEpochMilli(date.getTime());
        else
            instant = date.toInstant();

        return instant.atZone(ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(Date date) {
        return getZone(date).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return getZone(date).toLocalDateTime();
    }

    public static Date string2dateShort(String str) {
        return localDateTime2Date(string2localDateShort(str));
    }

}
