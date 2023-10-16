package com.ajaxjs.util;

import com.ajaxjs.util.convert.ConvertToJson;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.convert.JsonHelper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
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
     */
    public static void printJson(Object obj) {
        System.out.println("---------------");
        System.out.println(ConvertToJson.format(JsonHelper.toJson(obj)));
    }

    public static void printJson2(Object obj) {
        System.out.println((JsonHelper.toJson(obj)));
    }

    public static void printArr(Object[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    private static Boolean isRunningTest;

    /**
     * For static-way to get request in UNIT TEST
     */
    public static HttpServletRequest request;

    /**
     * 检测是否在运行单元测试
     */
    public static Boolean isRunningTest() {
        if (isRunningTest == null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            for (StackTraceElement e : stackTrace) {
                if (e.toString().lastIndexOf("junit.runners") > -1)
                    return true;
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
        jsonStr = ConvertToJson.removeCr(jsonStr);
        Map<String, Object> map = EntityConvert.json2map(jsonStr);

        return EntityConvert.map2Bean(map, beanClz, true);
    }
}
