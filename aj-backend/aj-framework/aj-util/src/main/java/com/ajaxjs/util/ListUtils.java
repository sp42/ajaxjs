package com.ajaxjs.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合工具类
 *
 * @author frank
 */
@Slf4j
public class ListUtils {
    /**
     * 即使 List 为空（null），也要返回一个空的 List
     *
     * @param <T>  范型，List 中元素的类别
     * @param list 给定的 List对象，可以为 null
     * @return 如果给定的 List 不为 null，则直接返回原 List 对象；如果为 null，则返回一个空的 List 对象
     */
    public static <T> List<T> getList(List<T> list) {
        if (CollectionUtils.isEmpty(list))
            list = Collections.emptyList();

        return list;
    }

    /**
     * 打印数组以便测试
     *
     * @param arr 数组对象，可以为 null
     */
    public static void printArray(Object[] arr) {
        if (arr == null)
            log.debug("数组为空，null！");

        assert arr != null;
        if (arr.length == 0)
            log.debug("数组不为空，但没有一个元素在内");

        log.debug(Arrays.toString(arr));
    }

    /**
     * 合并两个数组
     *
     * @param <T>    范型，数组中元素的类别
     * @param first  第一个数组
     * @param second 第二个数组
     * @return 合并后的数组，first数组在前，second数组在后
     */
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);

        return result;
    }

    /**
     * 将多个数组合并在一起
     * 忽略 null  的数组
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
     * @param <E>      范型，List 中元素的类别
     * @param elements 需要转换为 List 的数组
     * @return 转换后的List对象
     */
    @SafeVarargs
    public static <E> List<E> arrayList(E... elements) {
        List<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);

        return list;
    }

    /**
     * 在给定列表中查找第一个满足条件的元素
     *
     * @param <T>    范型，列表中元素的类别
     * @param list   给定列表
     * @param filter 过滤元素的条件
     * @return 第一个满足条件的元素，如果没有符合条件的元素，则返回 null
     */
    public static <T> T findOne(List<T> list, Predicate<T> filter) {
        return list.stream().filter(filter).findFirst().orElse(null);
    }

    /**
     * 在给定列表中查找满足条件的所有元素并封装成 List
     *
     * @param <T>    范型，列表中元素的类别
     * @param list   给定列表
     * @param filter 过滤元素的条件
     * @return 符合条件的元素封装的 List 集合
     */
    public static <T> List<T> findSome(List<T> list, Predicate<T> filter) {
        return list.stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * Integer[] 不能直接转 int[]，故设置一个函数专门处理之
     *
     * @param list 整形列表
     * @return 整形数组
     */
    public static int[] intList2Arr(List<Integer> list) {
        return newIntArray(list.size(), list::get);
    }

    /**
     * 当它们每一个都是数字的字符串形式，转换为整形的数组
     *
     * <pre>
     * {@code
     *   "1,2,3, ..." -- [1, 2, ...]
     * }
     * </pre>
     *
     * @param value 输入字符串
     * @return 整形数组
     */
    public static int[] stringArr2intArr(String value) {
        String[] strArr = value.split(",");

        return newIntArray(strArr.length, index -> Integer.parseInt(strArr[index].trim()));
    }

    /**
     * 创建一个 int 数列，对其执行一些逻辑
     *
     * @param length 数列最大值
     * @param fn     执行的逻辑
     * @return 数组
     */
    private static int[] newIntArray(int length, Function<Integer, Integer> fn) {
        int[] arr = new int[length];

        for (int i = 0; i < length; i++)
            arr[i] = fn.apply(i);

        return arr;
    }
}
