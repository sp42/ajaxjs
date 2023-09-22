package com.ajaxjs.tools;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Data
public class IdCardService {
    /**
     * 身份证第18位校检码
     */
    private static final String[] REF_NUMBER = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    /**
     * 身份证前17位每位加权因子
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 18位（二代）身份证正则表达式
     */
    private static final Pattern PATTERN = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}([\\dxX])$");

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final Map<String, String> PROVINCE_CODE_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 4593620948714292923L;

        {
            put("11", "北京");
            put("12", "天津");
            put("13", "河北");
            put("14", "山西");
            put("15", "内蒙古");
            put("21", "辽宁");
            put("22", "吉林");
            put("23", "黑龙江");
            put("31", "上海");
            put("32", "江苏");
            put("33", "浙江");
            put("34", "安徽");
            put("35", "福建");
            put("36", "江西");
            put("37", "山东");
            put("41", "河南");
            put("42", "湖北");
            put("43", "湖南");
            put("44", "广东");
            put("45", "广西");
            put("46", "海南");
            put("50", "重庆");
            put("51", "四川");
            put("52", "贵州");
            put("53", "云南");
            put("54", "西藏");
            put("61", "陕西");
            put("62", "甘肃");
            put("63", "青海");
            put("64", "宁夏");
            put("65", "新疆");
            put("71", "台湾");
            put("81", "香港");
            put("82", "澳门");
            put("91", "国外");
        }
    };

    // 省份
    private String province;
    // 城市
    private String city;
    // 区县
    private String region;
    // 年份
    private int year;
    // 月份
    private int month;
    // 日期
    private int day;
    // 性别
    private String gender;
    // 出生日期
    private LocalDate birthday;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 创建一个 IdCardService 实例
     *
     * @param idNo 身份证号
     */
    public IdCardService(String idNo) {
        this.idNo = idNo;
    }

    /**
     * 检查身份证号码有效性校验
     *
     * @return true 表示为合法的身份证号码
     */
    public boolean check() {
        return check(idNo);
    }

    /**
     * 身份证信息提取
     */
    public IdCardService extractor() {
        province = PROVINCE_CODE_MAP.get(idNo.substring(0, 2)); // 获取省份
        gender = Integer.parseInt(idNo.substring(16, 17)) % 2 != 0 ? "男" : "女";// 获取性别
        birthday = LocalDate.parse(idNo.substring(6, 14), FORMATTER); // 获取出生日期
        year = birthday.getYear();
        month = birthday.getMonthValue();
        day = birthday.getDayOfMonth();

        return this;
    }

    /**
     * 严格检查
     * 二代身份证号码有效性校验
     *
     * @return true 表示为合法的身份证号码
     */
    public static boolean check(String idNo) {
        if (!StringUtils.hasText(idNo) || idNo.length() != 18 || !PATTERN.matcher(idNo).matches() || !PROVINCE_CODE_MAP.containsKey(idNo.substring(0, 2)) // 检查身份证的省份信息是否正确
        ) return false;

        try {
            LocalDate.parse(idNo.substring(6, 14), FORMATTER); // 判断日期是否有效
        } catch (DateTimeParseException e) {
            return false;
        }

        return checkIdNoLastNum(idNo);
    }

    /**
     * 校验身份证第18位是否正确
     */
    private static boolean checkIdNoLastNum(String idNo) {
        char[] tmp = idNo.toCharArray();
        int[] cardIdArray = new int[tmp.length - 1];

        for (int i = 0; i < tmp.length - 1; i++)
            cardIdArray[i] = Integer.parseInt(String.valueOf(tmp[i]));

        int sum17 = 0;

        for (int i = 0; i < POWER.length; i++)
            sum17 += POWER[i] * cardIdArray[i];

        return idNo.substring(17, 18).equalsIgnoreCase(REF_NUMBER[(sum17 % 11)]); // 将和值与11取模得到余数进行校验码判断
    }
}
