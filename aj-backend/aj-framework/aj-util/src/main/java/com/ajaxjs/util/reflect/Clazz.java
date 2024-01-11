package com.ajaxjs.util.reflect;

import com.ajaxjs.framework.IgnoreDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 类相关的反射
 */
@Slf4j
public class Clazz {

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
            log.warn("找不到这个类： " + clzName, e);
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
     * 这个 Java 函数将一个可变参数列表转换为一个类对象列表。它接受一个可变参数 args，返回一个 Class 类型的数组 clazz，
     * 数组长度与参数列表的长度相同，并且每个元素的类型与对应参数的类型相同。
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

    /**
     * 遍历 Java Bean 对象的所有字段，并对每个字段执行指定的操作。
     * 注意 static 成员无效
     *
     * @param bean 要遍历的 Java Bean 对象。
     * @param fn   对每个字段要执行的操作，类型为 BiConsumer，其中第一个参数为字段名，第二个参数为字段值。
     */
    public static void eachFields(Object bean, BiConsumer<String, Object> fn) {
        eachFields2(bean.getClass(), (name, field) -> {
            try {
                Object value = field.get(bean);
                fn.accept(name, value);
            } catch (IllegalAccessException e) {
                log.warn("ERROR>>", e);
            }
        });
    }

    /**
     * 遍历给定类的所有非静态字段，并对每个字段执行给定的操作。
     *
     * @param clz 要遍历的类
     * @param fn  对于每个字段执行的操作
     */
    public static void eachFields2(Class<?> clz, BiConsumer<String, Field> fn) {
        Field[] fields = clz.getFields();

        if (ObjectUtils.isEmpty(fields)) return;

        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()))// 如果是静态的字段，则跳过
                    continue;

                fn.accept(field.getName(), field);// 对于当前字段执行给定的操作
            }
        } catch (IllegalArgumentException e) {
            log.warn("ERROR>>", e);
        }
    }

    @FunctionalInterface
    public interface EachFieldArg {
        void item(String key, Object value, PropertyDescriptor property);
    }

    /**
     * 遍历一个 Java Bean
     *
     * @param bean Java Bean
     * @param fn   执行的任务，参数有 key, value, property
     */
    public static void eachField(Object bean, EachFieldArg fn) {
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
            eachField(bean, props, fn);
        } catch (IntrospectionException e) {
            log.warn("ERROR>>", e);
        }
    }

    /**
     * 遍历一个 Java Bean
     *
     * @param bean Java Bean
     * @param fn   执行的任务，参数有 key, value, property
     */
    public static void eachField(Object bean, PropertyDescriptor[] props, EachFieldArg fn) {
        try {
            if (ObjectUtils.isEmpty(props))
                return;

            for (PropertyDescriptor property : props) {
                String key = property.getName();
                Method getter = property.getReadMethod();// 得到 property 对应的 getter 方法

                if (getter.getAnnotation(IgnoreDB.class) != null)
                    continue;

                Object value = getter.invoke(bean); // 原始默认值，不过通常是没有指定的

                if (value != null && value.equals("class"))  // 过滤 class 属性
                    continue;

                fn.item(key, value, property);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.warn("ERROR>>", e);
        }
    }
}
