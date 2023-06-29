package com.ajaxjs.util;

import java.math.BigDecimal;

public class TestDecimalUtils {
    public void test() {
        // 定义两个变量，进行运算
        BigDecimal a = new BigDecimal("100");
        BigDecimal b = new BigDecimal("3.14");

        // 加 a+b
        BigDecimal c1 = DecimalUtils.add(a, b);
        // 减 a-b
        BigDecimal c2 = DecimalUtils.subtract(a, b);
        // 乘 a*b
        BigDecimal c3 = DecimalUtils.multiply(a, b);
        // 除 a/b
        BigDecimal c4 = DecimalUtils.divide(a, b);

        // 累加 sum = a + sum
        BigDecimal sum = null;
        sum = DecimalUtils.accumulate(a, sum);

        // 将变量a结果保留2位小数
        BigDecimal c5 = DecimalUtils.scale(a, 2);

        // 变量a的1000倍运算   a*1000
        BigDecimal c6 = DecimalUtils.multiple(a, 1000);

        //Integer、Long、Float、Double、String、Object转为BigDecimal。
        Object obj = 123;
        BigDecimal c7 = DecimalUtils.toBigDecimal(obj);

        // BigDecimal转为字符串
        String str = DecimalUtils.toPlainString(a);

        // 不同数据类型之间混合运算（如：Double类型除以Long类型）
        BigDecimal mixCalc = DecimalUtils.divide(DecimalUtils.toBigDecimal(1.23D), DecimalUtils.toBigDecimal(1234567L));
    }
}
