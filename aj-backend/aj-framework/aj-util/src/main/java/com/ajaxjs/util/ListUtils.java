package com.ajaxjs.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 集合工具类
 *
 * @author frank
 */
public class ListUtils {
    private static final LogHelper LOGGER = LogHelper.getLog(ListUtils.class);

    /**
     * 即使 List 为空（null），也要返回一个空的 List
     *
     * @param <T>
     * @param list
     * @return
     */
    public static <T> List<T> getList(List<T> list) {
        if (CollectionUtils.isEmpty(list))
            list = Collections.emptyList();

        return list;
    }

    /**
     * 打印数组以便测试
     *
     * @param arr
     */
    public static void printArray(Object[] arr) {
        if (arr == null)
            LOGGER.debug("数组为空，null！");

        assert arr != null;
        if (arr.length == 0)
            LOGGER.debug("数组不为空，但没有一个元素在内！");

        LOGGER.debug(Arrays.toString(arr));
    }

    /**
     * 合并两个数组
     *
     * @param <T>
     * @param first
     * @param second
     * @return
     */
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);

        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param <T>    数组元素类型
     * @param arrays 数组集合
     * @return 合并后的数组
     */
    @SafeVarargs
    public static <T> T[] addAll(T[]... arrays) {
        if (arrays.length == 1)
            return arrays[0];

        int length = 0;
        for (T[] array : arrays) {
            if (null != array)
                length += array.length;
        }

        T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);

        length = 0;
        for (T[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    /**
     * 新建一个空数组
     *
     * @param <T>           数组元素类型
     * @param componentType 元素类型
     * @param newSize       大小
     * @return 空数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<?> componentType, int newSize) {
        return (T[]) Array.newInstance(componentType, newSize);
    }

    /**
     * 数组转换为 List
     *
     * @param <E>
     * @param elements
     * @return
     */
    @SafeVarargs
    public static <E> List<E> arrayList(E... elements) {
        List<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);

        return list;
    }

    /**
     * Create a new {@link HashMap}
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);

        return map;
    }

    /**
     * Create a new {@link HashMap}
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);
        map.put(k2, v2);

        return map;
    }

    /**
     * Create a new {@link HashMap}
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        return map;
    }

    public static <T> T findOne(List<T> list, Predicate<T> filter) {
        return list.stream().filter(filter).findFirst().orElse(null);
    }

    public static <T> List<T> findSome(List<T> list, Predicate<T> filter) {
        return list.stream().filter(filter).collect(Collectors.toList());
    }
}
