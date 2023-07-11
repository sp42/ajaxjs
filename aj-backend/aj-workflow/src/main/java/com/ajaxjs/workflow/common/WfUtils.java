package com.ajaxjs.workflow.common;

import com.ajaxjs.util.DateUtil;
import com.ajaxjs.workflow.model.ProcessModel;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 工作流工具类
 *
 * @author sp42@qq.com
 */
public class WfUtils {
    public static Long[] cast(Object[] arr) {
        Long[] ids = new Long[arr.length];
        int i = 0;

        for (Object s : arr)
            ids[i++] = Long.parseLong(s.toString());

        return ids;

    }

    public static String join(Object[] arr) {
        StringBuilder sb = new StringBuilder();
        for (Object o : arr)
            sb.append(o).append(",");

        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }

    /**
     * 默认的流程实例编号生成器 编号生成规则为:yyyyMMdd-HH:mm:ss-SSS-random
     *
     * @param model 模型对象
     * @return 流程实例编号
     */
    public static String generate(ProcessModel model) {
        return DateUtil.now("yyyyMMdd-HH:mm:ss-SSS") + "-" + new Random().nextInt(1000);
    }

    private static final Pattern pattern = Pattern.compile("[0-9]*");

    public static boolean isNumeric(String str) {
        return pattern.matcher(str).matches();
    }

    /**
     * ArrayUtils.add
     *
     * <p>
     * Inserts the specified element at the specified position in the array. Shifts
     * the element currently at that position (if any) and any subsequent elements
     * to the right (adds one to their indices).
     * </p>
     *
     * <p>
     * This method returns a new array with the same elements of the input array
     * plus the given element on the specified position. The component type of the
     * returned array is always the same as that of the input array.
     * </p>
     *
     * <p>
     * If the input array is <code>null</code>, a new one element array is returned
     * whose component type is the same as the element.
     * </p>
     *
     * <pre>
     * ArrayUtils.add(null, 0, null)      = [null]
     * ArrayUtils.add(null, 0, "a")       = ["a"]
     * ArrayUtils.add(["a"], 1, null)     = ["a", null]
     * ArrayUtils.add(["a"], 1, "b")      = ["a", "b"]
     * ArrayUtils.add(["a", "b"], 3, "c") = ["a", "b", "c"]
     * </pre>
     *
     * @param array   the array to add the element to, may be <code>null</code>
     * @param index   the position of the new object
     * @param element the object to add
     * @return A new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 ||
     *                                   index > array.length).
     */
    public static Object[] add(Object[] array, int index, Object element) {
        Class<?> clz;

        if (array != null)
            clz = array.getClass().getComponentType();
        else if (element != null)
            clz = element.getClass();
        else
            return new Object[]{null};

        return (Object[]) add(array, index, element, clz);
    }

    /**
     * Underlying implementation of add(array, index, element) methods. The last
     * parameter is the class, which may not equal element.getClass for primitives.
     *
     * @param array   the array to add the element to, may be <code>null</code>
     * @param index   the position of the new object
     * @param element the object to add
     * @param clz    the type of the element being added
     * @return A new array containing the existing elements and the new element
     */
    private static Object add(Object array, int index, Object element, Class<?> clz) {
        if (array == null) {
            if (index != 0)
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");

            Object joinedArray = Array.newInstance(clz, 1);
            Array.set(joinedArray, 0, element);

            return joinedArray;
        }

        int length = Array.getLength(array);
        if (index > length || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);

        Object result = Array.newInstance(clz, length + 1);
        System.arraycopy(array, 0, result, 0, index);
        Array.set(result, index, element);

        if (index < length)
            System.arraycopy(array, index, result, index + 1, length - index);

        return result;
    }

    /**
     * 根据 class 类型、methodName 方法名称，返回 Method 对象。 注意：这里不检查参数类型，所以自定义的 java 类应该避免使用重载方法
     *
     * @param clazz      类
     * @param methodName 方法名称
     * @return Method 对象
     */
    public static Method findMethod(Class<?> clazz, String methodName) {
        Method[] candidates = clazz.getDeclaredMethods();

        for (Method candidate : candidates) {
            if (candidate.getName().equals(methodName))
                return candidate;
        }

        if (clazz.getSuperclass() != null)
            return findMethod(clazz.getSuperclass(), methodName);

        return null;
    }

//    public static byte[] readBytes(InputStream in) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte[] buffer = new byte[BUFFER_SIZE];
//
//        for (int count; (count = in.read(buffer)) != -1; ) {
//            out.write(buffer, 0, count);
//        }
//
//        return out.toByteArray();
//    }
//
//    public static final int BUFFER_SIZE = 4096;

}
