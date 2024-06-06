package com.ajaxjs.iam.jwt;

import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;

public class JwtUtils {
    /**
     * 随机字符串
     */
    private static final String STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 生成指定长度的随机字符，可能包含数字
     * 另外一个方法 <a href="https://blog.csdn.net/qq_41995919/article/details/115299461">...</a>
     *
     * @param length 户要求产生字符串的长度
     * @return 随机字符
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(STR.charAt(number));
        }

        return sb.toString();
    }

    /**
     * Sign with HMAC SHA256 (HS256)
     */
    public static String hmacSha256(String data, String secret) {
        try {
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            sha256Hmac.init(new SecretKeySpec(hash, "HmacSHA256"));
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return JwtUtils.encode(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encode(String str) {
        return encode(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    public static String encodeBase64(String str) {
//        return Base64Utils.encodeToString(Base64Utils.encode(str.getBytes()));
        return Base64Utils.encodeToString(str.getBytes());
    }

    public static String decodeBase64(String str) {
        return Base64Utils.encodeToString(str.getBytes());
//        return Base64Utils.encodeToString(Base64Utils.decodeFromString(str));
    }

    public static long now() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * 返回过期时间
     *
     * @param hours 小时数
     * @return 过期时间的时间戳
     */
    public static long setExpire(int hours) {
        return LocalDateTime.now().plus(hours, ChronoUnit.HOURS).toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * EpochSecond 时间戳转换为真实的时间
     *
     * @return 真实的时间
     */
    public static String toRealTime(long timestamp) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = java.time.Instant.ofEpochSecond(timestamp).atZone(zoneId).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = formatter.format(localDateTime);
        System.out.println("formattedTime: " + formattedTime);

        return formattedTime;
    }
}
