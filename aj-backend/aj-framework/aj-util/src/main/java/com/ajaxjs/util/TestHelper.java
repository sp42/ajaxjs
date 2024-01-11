package com.ajaxjs.util;

import com.ajaxjs.util.convert.ConvertToJson;
import com.ajaxjs.util.convert.EntityConvert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * 单元测试的辅助工具类
 */
public class TestHelper {
    public static void json(Object obj) {
        System.out.println("---------------");
        System.out.println(ConvertToJson.toJson(obj));
    }

    /**
     * 打印 JSON 方便了解对象结构
     */
    public static void printJson(Object obj) {
        System.out.println("---------------");
        System.out.println(ConvertToJson.format(ConvertToJson.toJson(obj)));
    }

    /**
     * 将传入的对象转换为JSON字符串并打印
     *
     * @param obj 要转换为JSON的对象
     */
    public static void printJson2(Object obj) {
        System.out.println(ConvertToJson.toJson(obj));
    }

    /**
     * 打印数组中的元素
     *
     * @param arr 要打印的数组
     */
    public static void printArr(Object[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    private static Boolean isRunningTest;

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

    /**
     * 将JSON字符串转换为指定类型的Java对象
     *
     * @param jsonStr JSON字符串
     * @param beanClz Java对象的类型
     * @param <T>     Java对象的类型
     * @return 转换后的Java对象
     */
    public static <T> T jsonStr2Bean(String jsonStr, Class<T> beanClz) {
        jsonStr = ConvertToJson.removeCr(jsonStr);
        Map<String, Object> map = EntityConvert.json2map(jsonStr);

        return EntityConvert.map2Bean(map, beanClz, true);
    }

}
