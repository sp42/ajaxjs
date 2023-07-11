package com.ajaxjs.utils;

import org.junit.Test;

import java.util.Arrays;

import static com.ajaxjs.utils.BytesUtils.bytesToLong;
import static com.ajaxjs.utils.BytesUtils.longToBytes;

public class TestBytesUtils {
    @Test
    public void test() {
        long l = 1291286722214105090L;
        byte[] content = longToBytes(l);
        System.out.println("long类型转byte===========" + Arrays.toString(longToBytes(l)));
        System.out.println("byte类型转long===========" + bytesToLong(content, 0));
    }
}
