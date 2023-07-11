package com.ajaxjs.utils;

import java.math.BigDecimal;

/**
 * 信用卡有效期格式转换为 MMYY、YYMM
 * <a href="https://blog.csdn.net/qq_40083897/article/details/99674687">...</a>
 */
public class BankCard {
    // 将信用卡有效期格式转换为 MMYY
    public String expiredTimeToMMYY(String expiredTime) {
        if (expiredTime.contains("/") || expiredTime.contains("-")) {
            expiredTime = expiredTime.replace("/", "");
            expiredTime = expiredTime.replace("-", "");
        }

        String before = expiredTime.substring(0, 2);
        String after = expiredTime.substring(2, 4);

        BigDecimal big = new BigDecimal(before);
        BigDecimal times = new BigDecimal("12");

        int compareTo = big.compareTo(times);
        // 如果前两位大于12，,代表是年/月的格式
        if (compareTo == 1)
            expiredTime = after + before;
        else
            expiredTime = before + after;

        return expiredTime;
    }

    // 将信用卡有效期格式转换为 YYMM
    public String expiredTimeToYYMM(String expiredTime) {
        if (expiredTime.contains("/") || expiredTime.contains("-")) {
            expiredTime = expiredTime.replace("/", "");
            expiredTime = expiredTime.replace("-", "");
        }

        String before = expiredTime.substring(0, 2);
        String after = expiredTime.substring(2, 4);
        BigDecimal big = new BigDecimal(before);
        BigDecimal times = new BigDecimal("12");
        int compareTo = big.compareTo(times);

        // 如果前两位大于12，,代表是年/月的格式
        if (compareTo == 1)
            expiredTime = before + after;
        else
            expiredTime = after + before;

        return expiredTime;
    }
}
