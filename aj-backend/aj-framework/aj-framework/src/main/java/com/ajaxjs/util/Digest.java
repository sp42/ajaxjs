/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util;

import com.ajaxjs.util.io.StreamHelper;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * 字符串的编码、解密 支持 MD5、SHA-1 和 SHA-2（SHA256）摘要算法
 */
@Slf4j
public class Digest {
    /**
     * 生成字符串的 SHA1/SHA-256 哈希值
     *
     * @param hash 哈希算法，可以是 SHA1/SHA-256
     * @param str  输入的内容
     * @return 已哈希过的字符串
     */
    private static String hash(String hash, String str) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance(hash);
        } catch (NoSuchAlgorithmException e) {
            log.warn("WARN>>>>>", e);
            return null;
        }

        md.update(str.getBytes(StandardCharsets.UTF_8));

        return StreamHelper.bytesToHexStr(md.digest()).toLowerCase();
    }

    /**
     * 获取 字符串 MD5 哈希值，等价于 DigestUtils.md5DigestAsHex()
     *
     * @param str 输入的字符串
     * @return MD5 摘要，返回32位大写的字符串
     */
    public static String md5(String str) {
        return hash("MD5", str);
    }

    /**
     * 生成字符串的 SHA1 哈希值
     *
     * @param str 输入的字符串
     * @return 字符串的 SHA1 哈希值
     */
    public static String getSHA1(String str) {
        return hash("SHA1", str);
    }

    /**
     * 生成字符串的 SHA2 哈希值
     *
     * @param str 输入的字符串
     * @return 字符串的 SHA2 哈希值
     */
    public static String getSHA256(String str) {
        return hash("SHA-256", str);
    }

    /**
     * 定义加密方式 MAC 算法可选以下多种算法
     *
     * <pre>
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    private final static String KEY_MAC = "HmacMD5";

    /**
     * 获取对称加密用的 SecretKey
     *
     * @param algorithm 加密算法
     * @param secure    可选的
     * @param keySize   可选的
     * @return SecretKey
     */
    public static SecretKey getSecretKey(String algorithm, SecureRandom secure, Integer keySize) {
        KeyGenerator generator;

        try {
            generator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            log.warn("WARN>>>>>", e);
            return null;
        }

        if (secure != null) {
            if (keySize == null)
                generator.init(secure);
            else
                generator.init(keySize, secure);
        }

        return generator.generateKey();
    }

    public static String getSecretKey(String algorithm, SecureRandom secure) {
        return Base64Utils.encodeToString(Objects.requireNonNull(getSecretKey(algorithm, secure, null)).getEncoded());
    }

    /**
     * 初始化 HMAC 密钥
     *
     * @return 密钥
     */
    public static String getHMAC_Key() {
        return getSecretKey(KEY_MAC, null);
    }

    /**
     * HMAC 加密
     * 单向加密、不可逆的、类似 MD5
     *
     * @param data 需要加密的字符串
     * @param key  密钥
     * @return 加密结果
     */
    public static String getHMAC(String data, String key) {
        if (!StringUtils.hasText(data))
            return null;

        byte[] bytes = null;

        try {
            SecretKey secretKey = new SecretKeySpec(Base64Utils.decodeFromString(key), KEY_MAC);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes());
        } catch (Exception e) {
            log.warn("WARN>>>>>", e);
        }

        assert bytes != null;
        return StreamHelper.bytesToHexStr(bytes);
    }

    public static byte[] getMac(String algorithm, String key, String data) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm));

            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.warn("WARN>>>>>", e);
            return null;
        }
    }

    public static String getMacAsStr(String algorithm, String key, String data) {
        return Base64Utils.encodeToString(Objects.requireNonNull(getMac(algorithm, key, data)));
    }

    /**
     * 签名采用 HmacSHA1 算法 + Base64，编码采用 UTF-8
     */
    public static String doHmacSHA1(String key, String data) {
        return getMacAsStr("HmacSHA1", key, data);
    }

    public static String doHmacSHA256(String key, String data) {
        return getMacAsStr("HmacSHA256", key, data);
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

            while ((len = stream.read(buf)) > 0)
                digest.update(buf, 0, len);

            return StreamHelper.bytesToHexStr(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            log.warn("WARN>>>>>", e);
            return "";
        }
    }
}
