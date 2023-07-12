package com.ajaxjs.cryptography;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {
    public static String base64EncodeToString(String str) {
        byte[] byteData = str.getBytes();

        return base64EncodeToString(byteData);
    }

    public static String base64EncodeToString(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeFromString(String base64EncodedString) {
        return Base64.getDecoder().decode(base64EncodedString);
    }

    /**
     * 字节转编码为 字符串（ UTF-8 编码）
     *
     * @param bytes 输入的字节数组
     * @return 字符串
     */
    public static String byte2String(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    /**
     * byte[] 转化为 16 进制字符串输出
     *
     * @param bytes 字节数组
     * @return 16 进制字符串
     */
    public static String bytesToHexStr(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;

            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
