package com.ajaxjs.util.convert;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestJsonHelper {
    @Test
    public void testFormat() {
        // 测试空JSON字符串，期望返回空字符串
        assertEquals("", JsonHelper.format(""));

        // 测试不包含嵌套结构的JSON字符串
        String input1 = "{\"name\": \"Alice\", \"age\": 20}";
        String expected1 = "{\n\t\"name\": \"Alice\",\n\t \"age\": 20\n}";
        assertEquals(expected1, JsonHelper.format(input1));

        // 测试包含嵌套结构的JSON字符串
        String input2 = "{\"name\": \"Alice\", \"address\": {\"city\": \"Beijing\", \"country\": \"China\"}}";
        String expected2 = "{\n\t\"name\": \"Alice\",\n\t \"address\": {\n\t\t\"city\": \"Beijing\",\n\t\t \"country\": \"China\"\n\t}\n}";
        assertEquals(expected2, JsonHelper.format(input2));

        // 测试多层嵌套的JSON字符串
        String input3 = "{\"name\": \"Alice\", \"address\": {\"city\": \"Beijing\", \"country\": \"China\", \"details\": {\"postcode\": \"100001\"}}}";
        String expected3 = "{\n\t\"name\": \"Alice\",\n\t \"address\": {\n\t\t\"city\": \"Beijing\",\n\t\t \"country\": \"China\",\n\t\t \"details\": {\n\t\t\t\"postcode\": \"100001\"\n\t\t}\n\t}\n}";
        assertEquals(expected3, JsonHelper.format(input3));

        // 测试带有逗号的JSON字符串
        String input4 = "{\"name\": \"Alice\", \"age\": 20, \"city\": \"Beijing\"}";
        String expected4 = "{\n\t\"name\": \"Alice\",\n\t \"age\": 20, \"city\": \"Beijing\"\n}";
        assertEquals(expected4, JsonHelper.format(input4));
    }

    @Test
    public void testRemoveComment() {
        // 测试删除单行注释
        assertEquals("var a = 1; \nconsole.log(a);\n", JsonHelper.removeComment("var a = 1; // 定义变量a\nconsole.log(a);\n"));

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
        assertEquals(expected, JsonHelper.removeComment(input));

        // 测试删除部分注释
        String input2 = "/* This is a multi-line comment. */ var a = 1; /* This is another comment. */ console.log(a);";
        String expected2 = " var a = 1;  console.log(a);";
        assertEquals(expected2, JsonHelper.removeComment(input2));

        // 测试不包含注释的字符串，返回原字符串
        String input3 = "This string does not contain any comment.";
        assertEquals(input3, JsonHelper.removeComment(input3));

        // 测试空字符串，返回空字符串
        String input4 = "";
        assertEquals(input4, JsonHelper.removeComment(input4));

        // 测试只包含注释的字符串，返回空字符串
        String input5 = "/* This is a comment. */";
        String expected5 = "";
        assertEquals(expected5, JsonHelper.removeComment(input5));
    }

    @Test
    public void testRemoveCr() {
        String input = "Hello\r\nWorld!";
        String expectedOutput = "HelloWorld!";
        String output = JsonHelper.removeCr(input);
        assertEquals(expectedOutput, output);

        input = "Hello\r\n\r\nWorld!\r\n";
        expectedOutput = "HelloWorld!";
        output = JsonHelper.removeCr(input);
        assertEquals(expectedOutput, output);

        input = "Hello\rWorld!\n";
        expectedOutput = "HelloWorld!";
        output = JsonHelper.removeCr(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testRepeatStr() {
        String inputStr = "Hello";
        String inputDiv = "-";
        int inputRepeat = 3;

        String expectedOutput = "Hello-Hello-Hello";
        String output = JsonHelper.repeatStr(inputStr, inputDiv, inputRepeat);

        assertEquals(expectedOutput, output);

        inputStr = "World";
        inputDiv = " ";
        inputRepeat = 1;

        expectedOutput = "World";
        output = JsonHelper.repeatStr(inputStr, inputDiv, inputRepeat);

        assertEquals(expectedOutput, output);

        inputStr = "*";
        inputDiv = "";
        inputRepeat = 5;

        expectedOutput = "*****";
        output = JsonHelper.repeatStr(inputStr, inputDiv, inputRepeat);

        assertEquals(expectedOutput, output);
    }




}
