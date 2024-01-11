package com.ajaxjs.util.regexp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author xinzhang
 */
public class RegExpUtils {
    public boolean isMatch(Pattern pattern, String str) {
        return pattern.matcher(str).find();
    }

    /**
     * 返回 Matcher
     *
     * @param regexp 正则
     * @param str    测试的字符串
     * @return Matcher
     */
    private static Matcher getMatcher(String regexp, String str) {
        return Pattern.compile(regexp).matcher(str);
    }

    /**
     * 使用正则的快捷方式。可指定分组
     *
     * @param regexp     正则
     * @param str        测试的字符串
     * @param groupIndex 分组 id，若为 -1 则取最后一个分组
     * @return 匹配结果
     */
    public static String regMatch(String regexp, String str, int groupIndex) {
        Matcher m = getMatcher(regexp, str);

        if (groupIndex == -1)
            groupIndex = m.groupCount();

        return m.find() ? m.group(groupIndex) : null;
    }

    /**
     * 使用正则的快捷方式
     *
     * @param regexp 正则
     * @param str    测试的字符串
     * @return 匹配结果，只有匹配第一个
     */
    public static String regMatch(String regexp, String str) {
        return regMatch(regexp, str, 0);
    }

    /**
     * 返回所有匹配项
     *
     * @param regexp 正则
     * @param str    测试的字符串
     * @return 匹配结果
     */
    public static String[] regMatchAll(String regexp, String str) {
        Matcher m = getMatcher(regexp, str);
        List<String> list = new ArrayList<>();

        while (m.find())
            list.add(m.group());

        return list.toArray(new String[0]);
    }

    /**
     * 判断输入字符串是否为科学计数法
     */
    static Pattern kexuejisshu_pattern;

    /**
     * 过滤输入字符串的科学计数法为真实数字
     *
     * @param json JSON
     * @return 真实数字
     */
    public static String kexuejishu(String json) {
        // init
        if (kexuejisshu_pattern == null)
            kexuejisshu_pattern = Pattern.compile("(-?\\d+\\.?\\d*)[Ee]{1}[+-]?[0-9]*");

        return RegExpUtils.replaceAll(json, kexuejisshu_pattern, (text, index, matcher) -> new BigDecimal(Double.toString(Double.parseDouble(text))).toPlainString());
    }

    /**
     * 将 String 中的所有 regex 匹配的字串全部替换掉
     *
     * @param string      代替换的字符串
     * @param regex       替换查找的正则表达式
     * @param replacement 替换函数
     * @return 结果
     */
    public static String replaceAll(String string, String regex, ReplaceCallBack replacement) {
        return replaceAll(string, Pattern.compile(regex), replacement);
    }

    /**
     * 将 String 中的所有 pattern 匹配的字串替换掉
     *
     * @param string      代替换的字符串
     * @param pattern     替换查找的正则表达式对象
     * @param replacement 替换函数
     * @return 结果
     */
    public static String replaceAll(String string, Pattern pattern, ReplaceCallBack replacement) {
        if (string == null)
            return null;

        Matcher m = pattern.matcher(string);

        if (m.find()) {
            StringBuffer sb = new StringBuffer();
            int index = 0;

            do {
                m.appendReplacement(sb, replacement.replace(m.group(0), index++, m));
            } while (m.find());

            m.appendTail(sb);

            return sb.toString();
        }

        return string;
    }

    /**
     * 将 String 中的 regex 第一次匹配的字串替换掉
     *
     * @param string      代替换的字符串
     * @param regex       替换查找的正则表达式
     * @param replacement 替换函数
     * @return 结果
     */
    public static String replaceFirst(String string, String regex, ReplaceCallBack replacement) {
        return replaceFirst(string, Pattern.compile(regex), replacement);
    }

    /**
     * 将 String 中的 pattern 第一次匹配的字串替换掉
     *
     * @param string      代替换的字符串
     * @param pattern     替换查找的正则表达式对象
     * @param replacement 替换函数
     * @return 结果
     */
    public static String replaceFirst(String string, Pattern pattern, ReplaceCallBack replacement) {
        if (string == null)
            return null;

        Matcher m = pattern.matcher(string);
        StringBuffer sb = new StringBuffer();

        if (m.find())
            m.appendReplacement(sb, replacement.replace(m.group(0), 0, m));

        m.appendTail(sb);

        return sb.toString();
    }
}
