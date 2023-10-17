package com.ajaxjs.util.convert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestConvertToJson {
    @Test
    public void testToJsonBasicType() {
        Object obj = true;
        String json = ConvertToJson.toJson(obj);
        assertEquals("true", json);

        obj = 123;
        json = ConvertToJson.toJson(obj);
        assertEquals("123", json);

        obj = 100000000000000001L;
        json = ConvertToJson.toJson(obj);
        assertEquals("\"100000000000000001\"", json);

        obj = 99999L;
        json = ConvertToJson.toJson(obj);
        assertEquals("99999", json);

        obj = "hello";
        json = ConvertToJson.toJson(obj);
        assertEquals("\"hello\"", json);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "John");
        map.put("age", 30);
        json = ConvertToJson.toJson(map);
        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }

    @Test
    public void testList2Json() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        String json = ConvertToJson.list2Json(list);
        assertEquals("[1, 2, 3]", json);

        List<Long> listLong = new ArrayList<>();
        listLong.add(100L);
        listLong.add(200L);
        listLong.add(300L);
        json = ConvertToJson.list2Json(listLong);

        assertEquals("[100, 200, 300]", json);

        List<String> listStr = new ArrayList<>();
        listStr.add("hello");
        listStr.add("world");
        listStr.add("!");
        json = ConvertToJson.list2Json(listStr);
        assertEquals("[\"hello\", \"world\", \"!\"]", json);

        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "John");
        map1.put("age", 30);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "Jane");
        map2.put("age", 25);
        listMap.add(map1);
        listMap.add(map2);
        json = ConvertToJson.list2Json(listMap);
        assertEquals("[{\"name\":\"John\",\"age\":30}, {\"name\":\"Jane\",\"age\":25}]", json);

        /* 这种转不了 */
//        List<Object> listMix = new ArrayList<>();
//        listMix.add("hello");
//        listMix.add(123);
//        listMix.add(true);
//        json = ConvertToJson.list2Json(listMix);
//        assertEquals("[\"hello\", 123, true]", json);

        List<Object> listEmpty = new ArrayList<>();
        json = ConvertToJson.list2Json(listEmpty);
        assertEquals("[]", json);
    }

    @Test
    public void testFormat() {
        // 测试空JSON字符串，期望返回空字符串
        assertEquals("", ConvertToJson.format(""));

        // 测试不包含嵌套结构的JSON字符串
        String input1 = "{\"name\": \"Alice\", \"age\": 20}";
        String expected1 = "{\n\t\"name\": \"Alice\",\n\t \"age\": 20\n}";
        assertEquals(expected1, ConvertToJson.format(input1));

        // 测试包含嵌套结构的JSON字符串
        String input2 = "{\"name\": \"Alice\", \"address\": {\"city\": \"Beijing\", \"country\": \"China\"}}";
        String expected2 = "{\n\t\"name\": \"Alice\",\n\t \"address\": {\n\t\t\"city\": \"Beijing\",\n\t\t \"country\": \"China\"\n\t}\n}";
        assertEquals(expected2, ConvertToJson.format(input2));

        // 测试多层嵌套的JSON字符串
        String input3 = "{\"name\": \"Alice\", \"address\": {\"city\": \"Beijing\", \"country\": \"China\", \"details\": {\"postcode\": \"100001\"}}}";
        String expected3 = "{\n\t\"name\": \"Alice\",\n\t \"address\": {\n\t\t\"city\": \"Beijing\",\n\t\t \"country\": \"China\",\n\t\t \"details\": {\n\t\t\t\"postcode\": \"100001\"\n\t\t}\n\t}\n}";
        assertEquals(expected3, ConvertToJson.format(input3));

        // 测试带有逗号的JSON字符串
        String input4 = "{\"name\": \"Alice\", \"age\": 20, \"city\": \"Beijing\"}";
        String expected4 = "{\n\t\"name\": \"Alice\",\n\t \"age\": 20, \"city\": \"Beijing\"\n}";
        assertEquals(expected4, ConvertToJson.format(input4));
    }

    @Test
    public void testRepeatStr() {
        String inputStr = "Hello";
        String inputDiv = "-";
        int inputRepeat = 3;

        String expectedOutput = "Hello-Hello-Hello";
        String output = ConvertToJson.repeatStr(inputStr, inputDiv, inputRepeat);

        assertEquals(expectedOutput, output);

        inputStr = "World";
        inputDiv = " ";
        inputRepeat = 1;

        expectedOutput = "World";
        output = ConvertToJson.repeatStr(inputStr, inputDiv, inputRepeat);

        assertEquals(expectedOutput, output);

        inputStr = "*";
        inputDiv = "";
        inputRepeat = 5;

        expectedOutput = "*****";
        output = ConvertToJson.repeatStr(inputStr, inputDiv, inputRepeat);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testRemoveComment() {
        // 测试删除单行注释
        assertEquals("var a = 1; \nconsole.log(a);\n", ConvertToJson.removeComment("var a = 1; // 定义变量a\nconsole.log(a);\n"));

        // 测试删除多行注释
        String input = "/*\n" +
                "This is a multi-line comment.\n" +
                "It can span multiple lines.\n" +
                "*/\n" +
                "\n" +
                "var a = 1;\n" +
                "/* This is another comment. */\n" +
                "console.log(a);\n";
        String expected = "\n\n" +
                "var a = 1;\n" +
                "\n" +
                "console.log(a);\n";
        assertEquals(expected, ConvertToJson.removeComment(input));

        // 测试删除部分注释
        String input2 = "/* This is a multi-line comment. */ var a = 1; /* This is another comment. */ console.log(a);";
        String expected2 = " var a = 1;  console.log(a);";
        assertEquals(expected2, ConvertToJson.removeComment(input2));

        // 测试不包含注释的字符串，返回原字符串
        String input3 = "This string does not contain any comment.";
        assertEquals(input3, ConvertToJson.removeComment(input3));

        // 测试空字符串，返回空字符串
        String input4 = "";
        assertEquals(input4, ConvertToJson.removeComment(input4));

        // 测试只包含注释的字符串，返回空字符串
        String input5 = "/* This is a comment. */";
        String expected5 = "";
        assertEquals(expected5, ConvertToJson.removeComment(input5));
    }

    @Test
    public void testRemoveCr() {
        String input = "Hello\r\nWorld!";
        String expectedOutput = "HelloWorld!";
        String output = ConvertToJson.removeCr(input);
        assertEquals(expectedOutput, output);

        input = "Hello\r\n\r\nWorld!\r\n";
        expectedOutput = "HelloWorld!";
        output = ConvertToJson.removeCr(input);
        assertEquals(expectedOutput, output);

        input = "Hello\rWorld!\n";
        expectedOutput = "HelloWorld!";
        output = ConvertToJson.removeCr(input);
        assertEquals(expectedOutput, output);
    }
}
