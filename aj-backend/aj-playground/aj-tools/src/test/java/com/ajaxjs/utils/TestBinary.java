package com.ajaxjs.utils;

import org.junit.Test;

import static com.ajaxjs.utils.BinaryUtil.binaryToDecimal;
import static com.ajaxjs.utils.BinaryUtil.decimalToBinary;

public class TestBinary {
    @Test
    public void testBinaryUtil() {
        int num = 3;
        String string = decimalToBinary(num, new StringBuilder());
        System.out.println(num + "的二进制结果：" + string);

        Integer binaryToDecimal = binaryToDecimal(string);
        System.out.println(string + "的十进制结果：" + binaryToDecimal);
    }
}
