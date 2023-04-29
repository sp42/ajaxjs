package com.ajaxjs.fast_doc;

import com.ajaxjs.util.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Util {
    /**
     * 对一个数组进行迭代，返回一个 list
     *
     * @param <T>
     * @param arr
     * @param fn
     * @return
     */
    public static <T, K> List<T> makeListByArray(K[] arr, Function<K, T> fn) {
        List<T> values = new ArrayList<>();

        for (K obj : arr) {
            T v = fn.apply(obj);

            if (v != null)
                values.add(v);
        }

        return values;
    }

    public static boolean isSimpleValueType(Class<?> type) {
        return type.isPrimitive() || type == Boolean.class || type == Integer.class || type == Long.class || type == String.class
                || type == Double.class || type == Float.class;
    }

    /**
     * 类全称转为 .java 磁盘路径
     *
     * @param clzName
     * @return
     */
    public static String className2JavaFileName(String clzName) {
        return clzName.replaceAll("\\.", "\\\\") + ".java";
    }

    public static String className2JavaFileName(Class<?> clz) {
        return className2JavaFileName(clz.getName());
    }

    public static String getClzPath(String dir) {
        File file = new File(dir); // 获取其file对象
        File[] fs = file.listFiles(); // 遍历path下的文件和目录，放在File数组中

        List<String> list = new ArrayList<>();

        if (fs != null)
            for (File f : fs) { // 遍历File[]数组
                if (!f.isDirectory() && f.getName().contains("jar")) {
                    list.add(dir + "//" + f.getName());
                }
            }

        return StrUtil.join(list, ";");
    }
}
