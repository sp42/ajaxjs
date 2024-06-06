/**
 * Copyright Sp42 frank@ajaxjs.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.view;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 格式化日期的标签
 *
 * @author sp42 frank@ajaxjs.com
 */
public class DateUtilTag extends SimpleTagSupport {
    /**
     * 输入的值，可以是任意类型，要经过转换。
     */
    private Object value;

    /**
     * 格式化模版，如果为 null，则默认为 采用格式 YYYY-MM-dd HH:MM
     */
    private String format;

    @Override
    public void doTag() throws IOException {
        if (value == null)
            return;

        Date date = object2Date(value);

        if (date == null)
            return;

        String format = this.format == null ? formatDateShorter(date) : simpleDateFormatFactory(this.format).format(date);
        getJspContext().getOut().write(format);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 年月日的正则表达式，例如 2016-08-18
     */
    private final static String DATE_YEAR = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

    /**
     * 一般日期判断的正则
     */
    private final static Pattern DATE_PATTERN = Pattern.compile(DATE_YEAR + " ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");

    /**
     * 正则实例
     */
    private final static Pattern DATE_YEAR_PATTERN = Pattern.compile(DATE_YEAR);

    /**
     * 常见的日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd HH:mm";

    public static final String DATE_FORMAT_SHORTER = "yyyy-MM-dd";

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

            if (str.isEmpty())
                return null;


            try {
                if (str.indexOf("T") != 1)
                    return simpleDateFormatFactory("yyyy-MM-dd'T'HH:mm:ss").parse(str);
                
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
     * 对输入的时间进行格式化，采用格式 YYYY-MM-dd HH:MM
     *
     * @param date 输入的时间
     * @return 转换到 YYYY-MM-dd HH:MM 格式的时间
     */
    public static String formatDateShorter(Date date) {
        return simpleDateFormatFactory(DATE_FORMAT_SHORT).format(date);
    }
}