package com.ajaxjs.utils;

import org.junit.Test;

import static com.ajaxjs.utils.ChineseWord.*;

public class TestChineseWord {
    @Test
    public void test() {
        String[] strArr = new String[]{"www.micmiu.com", "!@#$%^&*()_+{}[]|\"'?/:;<>,.", "！￥……（）——：；“”‘'《》，。？、", "不要啊", "やめて", "韩佳人", "???"};
        for (String str : strArr) {
            System.out.println("===========> 测试字符串：" + str);
            System.out.println("正则判断结果：" + isChineseByREG(str) + " -- " + isChineseByName(str));
            System.out.println("Unicode判断结果 ：" + isChinese(str));
            System.out.println("详细判断列表：");
            char[] ch = str.toCharArray();

            for (char c : ch) {
                System.out.println(c + " --> " + (isChinese(String.valueOf(c)) ? "是" : "否"));
            }
        }
    }
}
