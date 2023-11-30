package com.ajaxjs.util;


import com.ajaxjs.util.io.StreamHelper;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * <a href="https://raw.githubusercontent.com/535404515/MYSQL-TOMCAT-MONITOR/master/nlpms-task-monitor/src/main/java/com/nuoli/mysqlprotect/util/EncryptUtil.java">...</a>
 *
 * @author Caiyuhui
 */
public class EncryptUtil {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String HmacMD5 = "HmacMD5";
    public static final String HmacSHA1 = "HmacSHA1";
    public static final String DES = "DES";
    public static final String AES = "AES";

    /**
     * 编码格式；默认使用uft-8
     */
    public String charset = "utf-8";

    /**
     * DES
     */
    public int keySizeDES = 0;

    /**
     * AES
     */
    public int keySizeAES = 128;

    public static EncryptUtil me;

    private EncryptUtil() {
        //单例
    }

    // 双重锁
    public static EncryptUtil getInstance() {
        if (me == null) {
            synchronized (EncryptUtil.class) {
                if (me == null) me = new EncryptUtil();
            }
        }

        return me;
    }

    /**
     * 使用MessageDigest进行单向加密（无密码）
     *
     * @param res       被加密的文本
     * @param algorithm 加密算法名称
     */
    private String messageDigest(String res, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);

            return Base64Utils.encodeToString(md.digest(resBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用KeyGenerator进行单向/双向加密（可设密码）
     *
     * @param res       被加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密使用的秘钥
     */
    private String keyGeneratorMac(String res, String algorithm, String key) {
        try {
            SecretKey sk;

            if (key == null) {
                KeyGenerator kg = KeyGenerator.getInstance(algorithm);
                sk = kg.generateKey();
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                sk = new SecretKeySpec(keyBytes, algorithm);
            }

            Mac mac = Mac.getInstance(algorithm);
            mac.init(sk);

            return Base64Utils.encodeToString(mac.doFinal(res.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
     *
     * @param res       加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密的秘钥
     */
    private String keyGeneratorES(String res, String algorithm, String key, int keySize, boolean isEncode) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);

            if (keySize == 0) {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(new SecureRandom(keyBytes));
            } else if (key == null) {
                kg.init(keySize);
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(keySize, new SecureRandom(keyBytes));
            }

            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);

            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);

                return StreamHelper.bytesToHexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);

                return new String(cipher.doFinal(Objects.requireNonNull(parseHexStr2Byte(res))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];

        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }

        return result;
    }

    /**
     * MD5 加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     */
    public String MD5(String res) {
        return messageDigest(res, MD5);
    }

    /**
     * md5加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     */
    public String MD5(String res, String key) {
        return keyGeneratorMac(res, HmacMD5, key);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     */
    public String SHA1(String res) {
        return messageDigest(res, SHA1);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     */
    public String SHA1(String res, String key) {
        return keyGeneratorMac(res, HmacSHA1, key);
    }

    /**
     * 使用DES加密算法进行加密（可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     */
    public String DES_encode(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, true);
    }

    /**
     * 对使用DES加密算法的密文进行解密（可逆）
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     */
    public String DES_decode(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, false);
    }

    /**
     * 使用AES加密算法经行加密（可逆）
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     */
    public String AES_encode(String res, String key) {
        return keyGeneratorES(res, AES, key, keySizeAES, true);
    }

    /**
     * 对使用AES加密算法的密文进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     */
    public String AES_decode(String res, String key) {
        return keyGeneratorES(res, AES, key, keySizeAES, false);
    }

    /**
     * 使用异或进行加密
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     */
    public String XOR_encode(String res, String key) {
        byte[] bs = res.getBytes();

        for (int i = 0; i < bs.length; i++)
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());

        return StreamHelper.bytesToHexStr(bs);
    }

    /**
     * 使用异或进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     */
    public String XOR_decode(String res, String key) {
        byte[] bs = parseHexStr2Byte(res);

        for (int i = 0; i < Objects.requireNonNull(bs).length; i++)
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());

        return new String(bs);
    }

    /**
     * 直接使用异或（第一调用加密，第二次调用解密）
     *
     * @param res 密文
     * @param key 秘钥
     */
    public int XOR(int res, String key) {
        return res ^ key.hashCode();
    }
}