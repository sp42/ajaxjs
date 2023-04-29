package com.ajaxjs.framework;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * 单元测试的辅助工具类
 *
 * @author xinzhang
 */
public class TestHelper {
    public static void json(Object obj) {
        System.out.println("---------------");
        System.out.println(JsonHelper.toJson(obj));
    }

    /**
     * 打印 JSON 方便了解对象结构
     *
     * @param obj
     */
    public static void printJson(Object obj) {
        System.out.println("---------------");
        System.out.println(JsonHelper.format(JsonHelper.toJson(obj)));
    }

    public static void printJson2(Object obj) {
        System.out.println((JsonHelper.toJson(obj)));
    }

    private static Boolean isRunningTest;

    /**
     * For static-way to get request in UNIT TEST
     */
    public static HttpServletRequest request;

    /**
     * 检测是否在运行单元测试
     *
     * @return
     */
    public static Boolean isRunningTest() {
        if (isRunningTest == null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            for (StackTraceElement e : stackTrace) {
                if (e.toString().lastIndexOf("junit.runners") > -1) {
                    isRunningTest = true;
                    return isRunningTest;
                }
            }

            isRunningTest = false;
        }

        return isRunningTest;
    }

    /**
     * 打印 bean，方便调试
     *
     * @param bean Bean
     */
    public static void print(Object bean) {
        Class<?> c = bean.getClass();
        StringBuilder sb = new StringBuilder(c.getName() + ".toString：\n");
        Field[] fields = c.getDeclaredFields();
        Field.setAccessible(fields, true);

        try {
            for (Field field : fields) {
                if (null != field.get(bean)) {
//					String fieldType = String.valueOf(field.getType());
                    String tmp = field.getName() + "=" + field.get(bean) + "\n";
                    sb.append(tmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(sb);
    }

    public static <T> T jsonStr2Bean(String jsonStr, Class<T> beanClz) {
        Map<String, Object> map = JsonHelper.parseMapClean(jsonStr);

        return MapTool.map2Bean(map, beanClz, true);
    }

    private static enum Level {
        INFO, WARN, ERROR
    }

    private static final String INFO_TPL = "%s %s \033[32;4mINFO\033[0m \033[36;4m%s\033[0m : %s";
    private static final String WARN_TPL = "%s %s \033[33;4mWARN\033[0m \033[36;4m%s\033[0m : %s";
    private static final String ERROR_TPL = "%s %s \033[31;4mERROR\033[0m \033[36;4m%s\033[0m : %s";

    /**
     * @param level
     * @param x
     */
    private static void print(Level level, Object x) {
        String tpl;

        if (level == Level.INFO)
            tpl = INFO_TPL;
        else if (level == Level.WARN)
            tpl = WARN_TPL;
        else
            tpl = ERROR_TPL;

        StackTraceElement e = new Exception().getStackTrace()[1];
        String className = e.getClassName();
        String methodName = e.getMethodName();
        String str = String.format(tpl, LocalDate.now(), LocalTime.now(), className + "#" + methodName, x);

        if (level == Level.INFO || level == Level.WARN)
            System.out.println(str);
        else
            System.err.println(str);
    }

    /**
     * 控制台打印 WARN 日志
     *
     * @param x 待打印
     */
    public static void info(Object x) {
        print(Level.INFO, x);
    }

    /**
     * 控制台打印 ERROR 日志
     *
     * @param x 待打印
     */
    public static void warn(Object x) {
        print(Level.ERROR, x);
    }

    /**
     * 控制台打印 INFO 日志
     *
     * @param x 待打印
     */
    public static void error(Object x) {
        print(Level.ERROR, x);
    }
}
