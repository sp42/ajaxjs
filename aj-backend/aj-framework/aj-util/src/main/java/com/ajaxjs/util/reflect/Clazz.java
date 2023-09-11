package com.ajaxjs.util.reflect;

import com.ajaxjs.util.logger.LogHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Clazz {
    private static final LogHelper LOGGER = LogHelper.getLog(Clazz.class);

    /**
     * 根据类名字符串获取类对象
     *
     * @param clzName 类全称。如果是内部类请注意用法
     * @return 类对象
     */
    public static Class<?> getClassByName(String clzName) {
        try {
            return Class.forName(clzName);
        } catch (ClassNotFoundException e) {
            LOGGER.warning(e, "找不到这个类：{0}。", clzName);
        }

        return null;
    }

    /**
     * 根据类名字符串获取类对象，可强类型转换类型
     *
     * @param clzName 类全称
     * @param clz     要转换的目标类型
     * @return 类对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassByName(String clzName, Class<T> clz) {
        Class<?> c = getClassByName(clzName);
        return c == null ? null : (Class<T>) c;
    }

    /**
     * 把参数转换为类对象列表
     *
     * @param args 可变参数列表
     * @return 类对象列表
     */
    public static Class<?>[] args2class(Object[] args) {
        Class<?>[] clazz = new Class[args.length];

        for (int i = 0; i < args.length; i++)
            clazz[i] = args[i].getClass();

        return clazz;
    }

    /**
     * 已知接口类型，获取它的 class
     *
     * @param type 接口类型
     * @return 接口的类对象
     */
    public static Class<?> getClassByInterface(Type type) {
        String className = type.toString();
        className = className.replaceAll("<.*>$", "").replaceAll("(class|interface)\\s", ""); // 不要泛型的字符

        return getClassByName(className);
    }

    /**
     * 获取某个类的所有接口
     *
     * @param clz 目标类
     * @return 类的所有接口
     */
    public static Class<?>[] getDeclaredInterface(Class<?> clz) {
        List<Class<?>> fields = new ArrayList<>();

        for (; clz != Object.class; clz = clz.getSuperclass()) {
            Class<?>[] currentInterfaces = clz.getInterfaces();
            fields.addAll(Arrays.asList(currentInterfaces));
        }

        return fields.toArray(new Class[0]);
    }

}
