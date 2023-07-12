/**
 * Copyright Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.sql.util.geo.LocationPoint;
import com.ajaxjs.sql.util.geo.MySqlGeoUtils;
import com.ajaxjs.util.map.JsonHelper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 处理值的一些相关函数
 */
public class ObjectHelper {
    /**
     * 创建一个新的 HashMap
     *
     * @param k1 键1
     * @param v1 值1
     * @return 新创建的HashMap
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);

        return map;
    }

    /**
     * 创建一个新的 HashMap
     *
     * @param k1 键1
     * @param v1 值1
     * @param k2 键2
     * @param v2 值2
     * @return 新创建的HashMap
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);
        map.put(k2, v2);

        return map;
    }

    /**
     * 创建一个新的 HashMap
     *
     * @param k1 键1
     * @param v1 值1
     * @param k2 键2
     * @param v2 值2
     * @param k3 键3
     * @param v3 值3
     * @return 新创建的 HashMap
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        return map;
    }

    /**
     * 挂起当前线程
     *
     * @param timeout  挂起的时长
     * @param timeUnit 时长单位
     * @return 被中断返回 false，否则 true
     */
    public static boolean sleep(Number timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout.longValue());
        } catch (InterruptedException e) {
            return false;
        }

        return true;
    }

    public static boolean sleep(Number timeout) {
        return sleep(timeout, TimeUnit.SECONDS);
    }

    /**
     * 把字符串还原为 Java 里面的真实值，如 "true"--true,"123"--123,"null"--null
     *
     * @param value 字符串的值
     * @return Java 里面的值
     */
    public static Object toJavaValue(String value) {
        if (value == null)
            return null;

        value = value.trim();

        if ("".equals(value))
            return "";
        if ("null".equals(value))
            return null;

        if ("true".equalsIgnoreCase(value))
            return true;
        if ("false".equalsIgnoreCase(value))
            return false;

        // try 比较耗资源，先检查一下
        if (value.charAt(0) == '-' || (value.charAt(0) >= '0' && value.charAt(0) <= '9'))
            try {
                int int_value = Integer.parseInt(value);

                if ((String.valueOf(int_value)).equals(value)) // 判断为整形
                    return int_value;
            } catch (NumberFormatException e) {// 不能转换为数字
                try {
                    long long_value = Long.parseLong(value);
                    if ((String.valueOf(long_value)).equals(value)) // 判断为整形
                        return long_value;
                } catch (NumberFormatException e1) {
                    if (value.matches("[0-9]{1,13}(\\.[0-9]*)?"))
                        return Double.parseDouble(value);
                }
            }

        return value;
    }

    /**
     * true/1、 字符串 true/1/yes/on 被视为 true 返回； false/0/null、字符串 false/0/no/off/null
     * 被视为 false 返回；
     *
     * @param value 输入值
     * @return true/false
     */
    public static boolean toBoolean(Object value) {
        if (value == null)
            return false;

        if (value.equals(true) || value.equals(1) || value.equals(1L))
            return true;

        if (value instanceof String) {
            String _value = (String) value;

            if (_value.equalsIgnoreCase("yes") || _value.equalsIgnoreCase("true") || _value.equals("1") || _value.equalsIgnoreCase("on"))
                return true;

            if (_value.equalsIgnoreCase("no") || _value.equalsIgnoreCase("false") || _value.equals("0") || _value.equalsIgnoreCase("off")
                    || _value.equalsIgnoreCase("null"))
                return false;
        }

        return false;
    }

    /**
     * 转换为 int 类型
     *
     * @param value 送入的值
     * @return int 类型的值
     */
    public static int object2int(Object value) {
        if (value == null)
            return 0;

        if (value instanceof String)
            return Integer.parseInt((String) value);
        else if (value instanceof Number)
            return ((Number) value).intValue();
        else {
            String toString = value.toString();

            if (toString.matches("-?\\d+"))
                return Integer.parseInt(toString);

            throw new IllegalArgumentException("This Object doesn't represent an int");
        }
    }

    /**
     * 转换为 long 类型
     *
     * @param value 送入的值
     * @return long 类型的值
     */
    public static long object2long(Object value) {
        if (value == null)
            return 0;

        if (value instanceof Long)
            return (long) value;
        else if (value instanceof BigInteger)
            return ((BigInteger) value).longValue();
        else if (value instanceof Number)
            return ((Number) value).longValue();
        else if (value instanceof String)
            return Long.parseLong((String) value);

        throw new IllegalArgumentException("This Object doesn't represent an long");
    }

    /**
     * 根据送入的类型作适当转换
     *
     * @param value  送入的值
     * @param target 期待的类型
     * @return 已经转换类型的值
     */
    @SuppressWarnings("unchecked")
    public static Object objectCast(Object value, Class<?> target) {
        if (value == null)
            return null;
        else if (target == boolean.class || target == Boolean.class) // 布尔型
            value = toBoolean(value);
        else if (target == int.class || target == Integer.class) { // 整形
            if (value instanceof Integer) {
//				System.out.println("::::::" + value.getClass());
//				System.out.println("::::::" + value.getClass().getName());
            } else {
                String str = value.toString();

                if (StringUtils.hasText(str)) {
                    if (str.contains(".")) { // 小数
                        value = ((Float) Float.parseFloat(str)).intValue();
                    } else
                        value = Integer.parseInt(str);
                } else
                    value = 0;
            }
        } else if (target == int[].class || target == Integer[].class) {
            // 复数
            if (value instanceof String)
                value = ListUtils.stringArr2intArr((String) value);
            else if (value instanceof List)
                value = ListUtils.intList2Arr((List<Integer>) value);
        } else if (target == long.class || target == Long.class)
            // LONG 型
            value = Long.valueOf((StringUtils.hasText(value.toString())) ? value.toString() : "0");
        else if (target == String.class) // 字符串型
            value = value.toString();
        else if (target == String[].class) {
            // 复数
            if (value instanceof ArrayList) {
                ArrayList<String> list = (ArrayList<String>) value;
                value = list.toArray(new String[0]);
            } else if (value instanceof String) {
                String str = (String) value;
                value = str.split(",");// 用于数组的分隔符
            }
//            else {
            // LOGGER.info("Bean 要求 String[] 类型，但实际传入类型：" +
            // value.getClass().getName());
//            }
        } else if (target == Date.class)
            value = DateUtil.object2Date(value);
        else if (target == BigDecimal.class) {
            if (value instanceof Integer)
                value = new BigDecimal((Integer) value);
            else if (value instanceof String) {
                BigDecimal b = new BigDecimal((String) value);
                b.setScale(2, RoundingMode.DOWN);

                value = b;
            } else if (value instanceof Double)
                value = new BigDecimal(Double.toString((Double) value));
        } else if (target == Map.class) {
            if (value instanceof String) {
                value = JsonHelper.parseMap((String) value);
            }
        } else if (target == float.class || target == Float.class) {
            if (value instanceof Double)
                value = ((Double) value).floatValue();

            if (value instanceof String)
                value = Float.parseFloat((String) value);
        } else if (target == List.class) {
            if (value instanceof String) {
                String[] arr = ((String) value).split(",");
                List<String> list = new ArrayList<>();
                Collections.addAll(list, arr);

                return list;
            }
        } else if (target == double.class || target == Double.class) {
//			if (value instanceof Float) {
//				value = ((Float) value).doubleValue();
//			}
            value = Double.parseDouble(value.toString());
        } else if (value instanceof String && IBaseModel.class.isAssignableFrom(target)) {
            // json2bean
            value = JsonHelper.parseMapAsBean((String) value, target);
        } else if (target != null && target.isEnum()) { // 枚举
            Object[] enumConstants = target.getEnumConstants();

            boolean isNumber = value instanceof Integer;
            for (Object obj : enumConstants) { // value 跟枚举类型比较
                if (isNumber) {
                    Enum<?> e = ((Enum<?>) obj);

                    if (e.ordinal() == ((Integer) value)) {
                        value = e.ordinal();
                        break;
                    }

                } else if (obj.toString().equals(value)) {
                    value = obj;
                    break;
                }
            }
        } else if (target == LocationPoint.class) { // mysql Geo 信息
            if (value instanceof byte[]) {
                double[] point = MySqlGeoUtils.bytesToOnePoint((byte[]) value);
                LocationPoint p = new LocationPoint();
                p.setLatitude(point[0]);
                p.setLongitude(point[1]);

                value = p;
            } else if (value instanceof Map) {
                LocationPoint p = new LocationPoint();
                Map<String, Object> map = (Map<String, Object>) value;
                p.setLatitude(Double.parseDouble(map.get("latitude").toString()));
                p.setLongitude(Double.parseDouble(map.get("longitude").toString()));

                value = p;
            }
        }

        return value;
    }
}
