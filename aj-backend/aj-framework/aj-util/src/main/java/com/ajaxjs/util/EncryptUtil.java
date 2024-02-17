package com.ajaxjs.util;


import com.ajaxjs.util.io.StreamHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * <a href="https://raw.githubusercontent.com/535404515/MYSQL-TOMCAT-MONITOR/master/nlpms-task-monitor/src/main/java/com/nuoli/mysqlprotect/util/EncryptUtil.java">...</a>
 *
 * @author Caiyuhui
 */
@Slf4j
public class EncryptUtil {
    public static final String DES = "DES";
    public static final String AES = "AES";

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
        // 单例
    }

    // 双重锁
    public static EncryptUtil getInstance() {
        if (me == null)
            synchronized (EncryptUtil.class) {
                if (me == null) me = new EncryptUtil();
            }

        return me;
    }

    /**
     * 获取对称加密用的 SecretKey
     *
     * @param algorithmName 加密算法
     * @param secure        可选的
     * @param keySize       可选的
     * @return SecretKey
     */
    public static SecretKey getSecretKey(String algorithmName, SecureRandom secure, int keySize) {
        KeyGenerator generator;

        try {
            generator = KeyGenerator.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            log.warn("WARN>>>>>", e);
            throw new RuntimeException(e);
        }

        if (secure != null) {
            if (keySize == 0) generator.init(secure);
            else generator.init(keySize, secure);
        }

        return generator.generateKey();
    }

    /**
     * 根据指定算法和安全随机数生成一个秘密密钥，并将其以 Base64 编码的字符串形式返回
     *
     * @param algorithm 算法名称
     * @param secure    安全随机数
     * @return Base64 编码后的秘密密钥字符串
     */
    public static String getSecretKey(String algorithm, SecureRandom secure) {
        return Base64Utils.encodeToString(getSecretKey(algorithm, secure, 0).getEncoded());
    }

    /**
     * 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
     *
     * @param res           加密的原文
     * @param algorithmName 加密使用的算法名称
     * @param key           加密的秘钥
     */
    private static String keyGeneratorES(String res, String algorithmName, String key, int keySize, boolean isEncode) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithmName);

            if (keySize == 0)
                kg.init(new SecureRandom(StrUtil.getUTF8_Bytes(key)));
            else if (key == null)
                kg.init(keySize);
            else
                kg.init(keySize, new SecureRandom(StrUtil.getUTF8_Bytes(key)));

            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithmName);
            Cipher cipher = Cipher.getInstance(algorithmName);

            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);

                return StreamHelper.bytesToHexStr(cipher.doFinal(StrUtil.getUTF8_Bytes(res)));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);

                return new String(cipher.doFinal(Objects.requireNonNull(StreamHelper.parseHexStr2Byte(res))));
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.warn("WARN>>>>>", e);
            return null;
        }
    }

    /**
     * 使用 DES 加密算法进行加密（可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     */
    public String DES_encode(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, true);
    }

    /**
     * 对使用 DES 加密算法的密文进行解密（可逆）
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     */
    public String DES_decode(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, false);
    }

    /**
     * 使用A ES 加密算法经行加密（可逆）
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     */
    public String AES_encode(String res, String key) {
        return keyGeneratorES(res, AES, key, keySizeAES, true);
    }

    /**
     * 对使用 AES 加密算法的密文进行解密
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
            bs[i] = (byte) (bs[i] ^ key.hashCode());

        return StreamHelper.bytesToHexStr(bs);
    }

    /**
     * 使用异或进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     */
    public String XOR_decode(String res, String key) {
        byte[] bs = StreamHelper.parseHexStr2Byte(res);

        for (int i = 0; i < Objects.requireNonNull(bs).length; i++)
            bs[i] = (byte) (bs[i] ^ key.hashCode());

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