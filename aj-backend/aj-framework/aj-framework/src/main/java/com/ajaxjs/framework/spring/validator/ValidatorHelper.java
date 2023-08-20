package com.ajaxjs.framework.spring.validator;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class ValidatorHelper {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";

    /**
     * 正则表达式：验证密码 (?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{8,}
     * 1、密码必须由数字、字符、特殊字符三种中的两种组成
     * 2、密码长度不能少于8个字符
     */
    public static final String REGEX_PASSWORD = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$)(?=[^\\s]+$).{8,16}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((16[0-9])|(17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9])|(19[0-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)|(^\\d{17}(\\d|X|x)$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    private static int[] IdWeights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

    /**
     * 校验用户名
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        if (!StringUtils.hasText(mobile))
            return false;

        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        if (!StringUtils.hasText(idCard)) return false;

        if (!Pattern.matches(REGEX_ID_CARD, idCard))
            return false;

        if (idCard.length() == 15)
            return true;

        int sum = 0;

        for (int i = 0; i < idCard.length(); i++) {
            String bit = String.valueOf(idCard.charAt(i));

            if (bit.equalsIgnoreCase("x"))
                sum += IdWeights[i] * 10;
            else
                sum += IdWeights[i] * Integer.parseInt(bit);
        }

        return sum % 11 == 1;
    }

    /**
     * 校验URL
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
}