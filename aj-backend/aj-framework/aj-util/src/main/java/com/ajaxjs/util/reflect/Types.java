package com.ajaxjs.util.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public class Types {
    /**
     * 获取泛型类型数组。
     *
     * @param type 要获取泛型类型数组的 Type 对象
     * @return 返回泛型类型数组。如果指定的 Type 对象不是 ParameterizedType 类型，则返回 null。
     */
    public static Type[] getActualType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;

            return pt.getActualTypeArguments();
        }

        log.warn(type + " 很可能不是一个泛型");
        return null;
    }

    /**
     * 获取方法返回值里面的泛型，如 List&lt;String&gt; 里面的 String，而不是 T。
     *
     * @param method 方法
     * @return 实际类型，可能多个
     */
    public static Type[] getGenericReturnType(Method method) {
        return getActualType(method.getGenericReturnType());
    }

    /**
     * 获取方法返回值里面的泛型，如 List&lt;String&gt; 里面的 String，而不是 T。
     * 这个方法获取第一个类型，并转换为 Class
     *
     * @param method 方法
     * @return 第一个实际类型
     */
    public static Class<?> getGenericFirstReturnType(Method method) {
        Type[] type = getGenericReturnType(method);

        return type == null ? null : type2class(type[0]);
    }

    /**
     * 获取如 List&lt;String&gt; 里面的泛型类型
     *
     * @param clz 类必须先指向一个实例，参见
     *            <a href="https://stackoverflow.com/questions/8436055/how-to-get-class-of-generic-type-when-there-is-no-parameter-of-it">...</a>
     * @return 实际类型
     */
    public static Type[] getActualType(Class<?> clz) {
        return getActualType(clz.getGenericSuperclass());
    }

    /**
     * 获取实际类
     *
     * @param clz 类型
     * @return 实际类
     */
    public static Class<?> getActualClass(Class<?> clz) {
        Type[] actualType = getActualType(clz);

        return type2class(actualType[0]);
    }

    /**
     * Type 接口转换为 Class
     *
     * @param type Type 接口
     * @return Class
     */
    public static Class<?> type2class(Type type) {
        return type instanceof Class ? (Class<?>) type : null;
    }
}
