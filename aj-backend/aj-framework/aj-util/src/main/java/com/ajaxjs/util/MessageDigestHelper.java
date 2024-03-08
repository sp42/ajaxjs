package com.ajaxjs.util;

import com.ajaxjs.util.io.StreamHelper;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串摘要（哈希）工具类
 */
@Data
@Slf4j
@Accessors(chain = true)
public class MessageDigestHelper {
    /**
     * 定义摘要的算法，可选以下多种算法
     *
     * <pre>
     * MD5
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    private String algorithmName;

    /**
     * 密钥，用于 HMAC 摘要，否则可选的
     */
    private String key;

    /**
     * 被摘要的字符串
     */
    private String value;

    /**
     * true 返回 Hex 转换为字符串，false = Base64 编码字符串
     */
    private Boolean isHexStr = true;

    /**
     * 获取指定算法的 MessageDigest 对象
     *
     * @param algorithmName 算法名称
     * @param str           需要摘要的字符串
     * @return 摘要后的字节数组
     */
    private static byte[] getMessageDigest(String algorithmName, String str) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            log.warn("WARN>>>>>", e);
            throw new RuntimeException(e);
        }

        return md.digest(StrUtil.getUTF8_Bytes(str));
    }

    /**
     * 返回摘要的结果
     *
     * @return 摘要的结果
     */
    public String getResult() {
        byte[] result;

        if (StringUtils.hasText(key)) result = getMac(algorithmName, key, value);
        else result = getMessageDigest(algorithmName, value);

        if (isHexStr) return StreamHelper.bytesToHexStr(result).toLowerCase();
        else return Base64Utils.encodeToString(result);
    }

    /**
     * 生成字符串的 MD5 哈希值，等价于 DigestUtils.md5DigestAsHex()
     *
     * @param str 输入的字符串
     * @return 字符串的 MD5 哈希值，返回32位小写的字符串
     */
    public static String getMd5(String str) {
        return new MessageDigestHelper().setAlgorithmName("MD5").setValue(str).getResult();
    }

    /**
     * 生成字符串的 SHA1 哈希值
     *
     * @param str 输入的字符串
     * @return 字符串的 SHA1 哈希值
     */
    public static String getSHA1(String str) {
        return new MessageDigestHelper().setAlgorithmName("SHA1").setValue(str).getResult();
    }

    /**
     * 生成字符串的 SHA256 哈希值
     *
     * @param str 输入的字符串
     * @return 字符串的 SHA256 哈希值
     */
    public static String getSHA256(String str) {
        return new MessageDigestHelper().setAlgorithmName("SHA-256").setValue(str).getResult();
    }

    /**
     * 生成字符串的 MD5 哈希值，Base64 编码
     *
     * @param str 输入的字符串
     * @return 字符串的 MD5 哈希值，Base64 编码
     */
    public static String getMd5AsBase64(String str) {
        return new MessageDigestHelper().setAlgorithmName("MD5").setValue(str).setIsHexStr(false).getResult();
    }

    // ----------------------KEY--------------------------

    /**
     * 获取指定算法的 MAC 值（可设密钥）
     *
     * @param algorithmName 算法名称
     * @param key           用于生成 MAC 值的密钥
     * @param data          要进行 MAC 计算的数据
     * @return 生成的 MAC 值
     */
    public static byte[] getMac(String algorithmName, String key, String data) {
        SecretKey sk;

        try {
            if (key == null) sk = KeyGenerator.getInstance(algorithmName).generateKey();
            else sk = new SecretKeySpec(StrUtil.getUTF8_Bytes(key)/*Base64Utils.decodeFromString(key)*/, algorithmName);
        } catch (NoSuchAlgorithmException e) {
            log.warn("WARN>>>>>", e);
            throw new RuntimeException(e);
        }

        try {
            Mac mac = Mac.getInstance(algorithmName);// 获取指定算法的 Mac 对象
            mac.init(sk); // 使用指定算法初始化 Mac 对象

            return mac.doFinal(StrUtil.getUTF8_Bytes(data)); // 对指定数据进行MAC计算
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.warn("WARN>>>>>", e); // 捕获算法不存在和密钥无效异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取字符串的 MD5 值
     *
     * @param key 生成 MD5 值的密钥
     * @param str 要生成 MD5 值的字符串
     * @return 生成的 MD5 值
     */
    public static String getMd5(String key, String str) {
        return new MessageDigestHelper().setAlgorithmName("HmacMD5").setKey(key).setValue(str).getResult();
    }

    /**
     * 获取字符串的 MD5 值并转换为 Base64 编码
     *
     * @param key 生成 MD5 的密钥
     * @param str 需要生成 MD5 的字符串
     * @return MD5 值的 Base64 编码
     */
    public static String getMd5AsBase64(String key, String str) {
        return new MessageDigestHelper().setAlgorithmName("HmacMD5").setKey(key).setValue(str).setIsHexStr(false).getResult();
    }

    public static String getHmacSHA1AsBase64(String key, String str) {
        return new MessageDigestHelper().setAlgorithmName("HmacSHA1").setKey(key).setValue(str).setIsHexStr(false).getResult();
    }

    public static String getHmacSHA256AsBase64(String key, String str) {
        return new MessageDigestHelper().setAlgorithmName("HmacSHA256").setKey(key).setValue(str).setIsHexStr(false).getResult();
    }

    /**
     * 计算文件 MD5
     *
     * @param file  文件对象。该参数与 bytes 二选一
     * @param bytes 文件字节。该参数与 file 二选一
     * @return 返回文件的 md5 值，如果计算过程中任务的状态变为取消或暂停，返回 null， 如果有其他异常，返回空字符串
     */
    public static String calcFileMD5(File file, byte[] bytes) {
        try (InputStream stream = file != null ? Files.newInputStream(file.toPath(), StandardOpenOption.READ) : new ByteArrayInputStream(bytes)) {
            byte[] buf = new byte[8192];
            int len;
            MessageDigest digest = MessageDigest.getInstance("MD5");

            while ((len = stream.read(buf)) > 0) digest.update(buf, 0, len);

            return StreamHelper.bytesToHexStr(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            log.warn("WARN>>>>>", e);
            return "";
        }
    }
}
