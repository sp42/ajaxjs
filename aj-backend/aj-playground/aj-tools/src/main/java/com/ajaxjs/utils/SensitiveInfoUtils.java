package com.ajaxjs.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 敏感信息处理工具类
 */
public class SensitiveInfoUtils {
    /**
     * 名字脱敏处理
     *
     * @param name 名字
     * @return 脱敏后的名字
     */
    public static String name(String name) {
        if (!StringUtils.hasText(name))
            return "*";

        char[] chars = name.toCharArray();
        for (int i = 1; i < chars.length; i++)
            chars[i] = '*';

        return new String(chars);
    }

    /**
     * 用户姓名脱敏
     *
     * @param name 姓名
     * @return 脱敏后的姓名
     */
    public static String username(String name) {
        if (StringUtils.isEmpty(name) || name.length() == 1)
            return "*";

        char[] chars = name.toCharArray();
        chars[1] = '*';

        for (int i = 2; i < chars.length - 1; i++)
            chars[i] = '*';

        return new String(chars);
    }

    /**
     * 银行卡脱敏 （截取后4位）
     *
     * @param cardNo 卡号
     * @return 脱敏后的卡号
     */
    public static String bankCard(String cardNo) {
        if (!StringUtils.hasText(cardNo))
            return "";

        return cardNo.substring(cardNo.length() - 4);
    }

    /**
     * 手机号脱敏 （中间四位隐藏）
     *
     * @param mobile 手机号
     * @return 脱敏后的手机号
     */
    public static String mobile(String mobile) {
        if (!StringUtils.hasText(mobile))
            return "";

        char[] chars = mobile.toCharArray();
        int i = 3;

        chars[i++] = '*';
        chars[i++] = '*';
        chars[i++] = '*';
        chars[i] = '*';

        return new String(chars);
    }

    /**
     * 将银行卡号内的空格去除
     *
     * @param cardNo 卡号
     * @return 卡号
     */
    public static String trimCardNo(String cardNo) {
        char[] chars = cardNo.toCharArray();
        char[] card = new char[chars.length];
        int i = 0;

        for (char c : chars) {
            if (Character.isDigit(c))
                card[i++] = c;
        }

        return new String(card, 0, i);
    }

    /**
     * 判断字符串是否是数字
     *
     * @param value 字符串
     * @return 为数字则返回<code>true</code>
     */
    public static boolean isNumber(String value) {
        if (value == null || value.isEmpty())
            return false;

        int len = value.length();

        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(value.charAt(i)))
                return false;

        }

        return true;
    }

    /**
     * 1. 0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。
     * 2.传入的参数等于0，则直接返回字符串"0.00"
     * 3.大于1的小数，直接格式化返回字符串
     *
     * @param obj 传入的小数
     */
    public static String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");

        if (obj.compareTo(BigDecimal.ZERO) == 0)
            return "0.00";
        else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0)
            return "0" + df.format(obj);
        else
            return df.format(obj);
    }

}
