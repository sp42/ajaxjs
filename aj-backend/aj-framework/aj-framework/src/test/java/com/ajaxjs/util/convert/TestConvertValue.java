package com.ajaxjs.util.convert;

import org.junit.Test;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ajaxjs.util.convert.ConvertBasicValue.toBoolean;
import static com.ajaxjs.util.convert.ConvertBasicValue.object2int;
import static com.ajaxjs.util.convert.ConvertBasicValue.object2long;
import static com.ajaxjs.util.convert.ConvertBasicValue.object2double;
import static com.ajaxjs.util.convert.ConvertBasicValue.object2float;
import static org.junit.Assert.*;

public class TestConvertValue {
    @Test
    public void testToBoolean() {
        assertTrue(toBoolean(true));
        assertTrue(toBoolean("true"));
        assertTrue(toBoolean("True"));

        assertFalse(toBoolean("fAlse"));
        assertFalse(toBoolean("null"));

        assertTrue(toBoolean("on"));
        assertTrue(toBoolean("yes"));
        assertTrue(toBoolean(1));
        assertTrue(toBoolean("1"));
    }

    @Test
    public void testObject2Int() {
        // 测试传入 null 值，返回 0
        assertEquals(0, object2int(null));

        // 测试传入整型对象，返回该对象的值
        assertEquals(100, object2int(100));
        assertEquals(-200, object2int(-200));

        // 测试传入字符串表达式，返回转换后的值
        assertEquals(123, object2int("123"));
        assertEquals(-456, object2int("-456"));

        // 测试传入其他类型对象，抛出 IllegalArgumentException 异常
        try {
            object2int(new Object());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("This Object doesn't represent an int", e.getMessage());
        }
    }

    @Test
    public void testObject2Long() {
        // 测试传入 null 值，返回 0
        assertEquals(0L, object2long(null));

        // 测试传入长整型对象，返回该对象的值
        assertEquals(100L, object2long(100L));
        assertEquals(-200L, object2long(-200L));

        // 测试传入 BigInteger 对象，返回转换后的值
        assertEquals(1234567890123456789L, object2long(new BigInteger("1234567890123456789")));

        // 测试传入其他数值类型对象，返回转换后的值
        assertEquals(100L, object2long(100));
        assertEquals(-200L, object2long(-200));
        assertEquals(3L, object2long(3.14f));
        assertEquals(4L, object2long(4.56));
        assertEquals(5L, object2long((byte) 5));
        assertEquals(6L, object2long((short) 6));

        // 测试传入字符串表达式，返回转换后的值
        assertEquals(123L, object2long("123"));
        assertEquals(-456L, object2long("-456"));

        // 测试传入其他类型对象，抛出 IllegalArgumentException 异常
        try {
            object2long(new Object());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("This Object doesn't represent a long", e.getMessage());
        }
    }

    @Test
    public void testObject2Double() {
        // 测试传入 null 值，返回 0.0
        assertEquals(0.0, object2double(null), 0.0001);

        // 测试传入双精度浮点型对象，返回该对象的值
        assertEquals(3.14, object2double(3.14), 0.0001);
        assertEquals(-2.5, object2double(-2.5), 0.0001);

        // 测试传入 BigInteger 对象，返回转换后的值
        assertEquals(1234567890123456789.0, object2double(new BigInteger("1234567890123456789")), 0.0001);

        // 测试传入其他数值类型对象，返回转换后的值
        assertEquals(100.0, object2double(100), 0.0001);
        assertEquals(-200.0, object2double(-200), 0.0001);
        assertEquals(3.14, object2double(3.14f), 0.0001);
        assertEquals(4.56, object2double(4.56), 0.0001);
        assertEquals(5.0, object2double((byte) 5), 0.0001);
        assertEquals(6.0, object2double((short) 6), 0.0001);

        // 测试传入字符串表达式，返回转换后的值
        assertEquals(3.14, object2double("3.14"), 0.0001);
        assertEquals(-4.56, object2double("-4.56"), 0.0001);

        // 测试传入其他类型对象，抛出 IllegalArgumentException 异常
        try {
            object2double(new Object());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("This Object doesn't represent a double", e.getMessage());
        }
    }

    @Test
    public void testObject2Float() {
        // 测试传入 null 值，返回 0.0
        assertEquals(0.0f, object2float(null), 0.0001);

        // 测试传入浮点数类型对象，返回该对象的值
        assertEquals(3.14f, object2float(3.14f), 0.0001f);
        assertEquals(-2.5f, object2float(-2.5f), 0.0001f);

        // 测试传入 BigInteger 对象，返回转换后的值
        assertEquals(1234567890123456789.0f, object2float(new BigInteger("1234567890123456789")), 0.0001f);

        // 测试传入其他数值类型对象，返回转换后的值
        assertEquals(100.0f, object2float(100), 0.0001f);
        assertEquals(-200.0f, object2float(-200), 0.0001f);
        assertEquals(3.14f, object2float(3.14f), 0.0001f);
        assertEquals(4.56f, object2float(4.56), 0.0001f);
        assertEquals(5.0f, object2float((byte) 5), 0.0001f);
        assertEquals(6.0f, object2float((short) 6), 0.0001f);

        // 测试传入字符串表达式，返回转换后的值
        assertEquals(3.14f, object2float("3.14"), 0.0001f);
        assertEquals(-4.56f, object2float("-4.56"), 0.0001f);

        // 测试传入其他类型对象，抛出 IllegalArgumentException 异常
        try {
            object2float(new Object());
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("This Object doesn't represent a float", e.getMessage());
        }
    }

    @Test
    public void testToArray() {
        ArrayList<String> input = new ArrayList<>();
        input.add("foo");
        input.add("bar");

        String[] expected = {"foo", "bar"};

        Object result = ConvertBasicValue.toArray(input, String[].class);
        assertArrayEquals(expected, (String[]) result);

        String inputStr = "foo,bar,baz";

        String[] expected2 = {"foo", "bar", "baz"};

        result = ConvertBasicValue.toArray(inputStr, String[].class);
        assertArrayEquals(expected2, (String[]) result);

        String inputInt = "1,2,3";
        int[] expected3 = {1, 2, 3};

        result = ConvertBasicValue.toArray(inputInt, int[].class);
        assertArrayEquals(expected3, (int[]) result);

        List<Integer> inputList = new ArrayList<>();
        inputList.add(1);
        inputList.add(2);
        inputList.add(3);

        int[] expected4 = {1, 2, 3};

        result = ConvertBasicValue.toArray(inputList, int[].class);
        assertArrayEquals(expected4, (int[]) result);
    }

    @Test
    public void testBasicConvert() {
        assertTrue((boolean) ConvertBasicValue.basicConvert("true", boolean.class));
        assertEquals(1000, (int) ConvertBasicValue.basicConvert("1000", int.class));
        assertEquals(1L, (long) ConvertBasicValue.basicConvert("1", long.class));
        assertEquals("foo", ConvertBasicValue.basicConvert("foo", String.class));

        int[] arr = (int[]) ConvertBasicValue.basicConvert("1,2,3", int[].class);
        assertArrayEquals(new int[]{1, 2, 3}, arr);

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        arr = (int[]) ConvertBasicValue.basicConvert(list, int[].class);
        assertArrayEquals(new int[]{1, 2, 3}, arr);

        String[] arr2 = (String[]) ConvertBasicValue.basicConvert("1,2,3", String[].class);
        assertArrayEquals(new String[]{"1", "2", "3"}, arr2);

        List<String> list2 = new ArrayList<>();
        list2.add("1");
        list2.add("2");
        list2.add("3");

        arr2 = (String[]) ConvertBasicValue.basicConvert(list2, String[].class);
        assertArrayEquals(new String[]{"1", "2", "3"}, arr2);

        assertEquals("Tue Feb 20 00:00:00 CST 2018", (ConvertBasicValue.basicConvert("2018-2-20", Date.class)).toString());
    }

    @Test
    public void testToJavaValue() {
        String input = "null";
        Object expected = null;

        Object result = ConvertBasicValue.toJavaValue(input);
        assertEquals(expected, result);

        input = "true";
        expected = true;

        result = ConvertBasicValue.toJavaValue(input);
        assertEquals(expected, result);

        input = "123";
        expected = 123;

        result = ConvertBasicValue.toJavaValue(input);
        assertEquals(expected, result);

        input = "1234567890123";
        expected = 1234567890123L;

        result = ConvertBasicValue.toJavaValue(input);
        assertEquals(expected, result);

        input = "3.14";
        expected = 3.14;

        result = ConvertBasicValue.toJavaValue(input);
        assertEquals(expected, result);

        assertEquals(123, (int) ConvertBasicValue.toJavaValue("123"));
        assertEquals(true, ConvertBasicValue.toJavaValue("true"));
        assertEquals(false, ConvertBasicValue.toJavaValue("false"));
        assertNull(ConvertBasicValue.toJavaValue("null"));
    }
}
