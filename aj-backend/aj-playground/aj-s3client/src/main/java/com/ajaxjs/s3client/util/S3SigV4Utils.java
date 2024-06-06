package com.ajaxjs.s3client.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class S3SigV4Utils {
    /**
     * 获取当前时间的字符串表示，格式为"yyyyMMdd'T'HHmmss'Z'"，使用 UTC 时区。
     *
     * @return 返回当前时间的字符串表示，格式化后的时间字符串。
     */
    public static String now() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
    }

    public static byte[] hmacSha256(byte[] key, String value) {
        String algorithm = "HmacSHA256";

        try {
            Mac mac = Mac.getInstance(algorithm);
            SecretKeySpec signingKey = new SecretKeySpec(key, algorithm);
            mac.init(signingKey);

            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error signing request", e);
        }
    }

    public static String calcFileSHA256(byte[] bytes) {
        try {
            // 创建SHA-256消息摘要对象
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 更新消息摘要以处理字节数组
            digest.update(bytes);

            // 计算哈希值
            byte[] hash = digest.digest();

            // 将哈希值转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            String sha256Hash = hexString.toString();
            System.out.println("SHA-256哈希值: " + sha256Hash);

            return sha256Hash;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256算法不可用");
            return null;
        }
    }

    private static final char[] ENC_TAB = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String base16Encode(String data) {
        return Throwables.returnableInstance(() -> base16Encode(data.getBytes(StandardCharsets.UTF_8)), RuntimeException::new);
    }

    public static String base16Encode(byte[] data) {
        int length = data.length;
        StringBuilder stringBuilder = new StringBuilder(length * 2);
        int i = 0;

        while (i < length) {
            stringBuilder.append(ENC_TAB[(data[i] & 0xF0) >> 4]);
            stringBuilder.append(ENC_TAB[data[i] & 0x0F]);
            i++;
        }

        return stringBuilder.toString();
    }
}
