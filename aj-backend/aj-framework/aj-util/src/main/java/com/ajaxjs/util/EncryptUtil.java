package com.ajaxjs.util;


import com.ajaxjs.util.io.StreamHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.Random;

/**
 * AES/DES/3DES/PBE 对称加密/解密
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
            throw new RuntimeException(e);
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

    ///////////////////////// --------------3DES----------------------------

    /**
     * 定义加密方式 支持以下任意一种算法
     *
     * <pre>
     * DES
     * DESede
     * Blowfish
     * </pre>
     */
    private static final String TripleDES_ALGORITHM = "DESede";

    /**
     * TripleDES(3DES) 加解密
     *
     * @param isEnc 是否加密
     * @param key   密钥
     * @param data  数据
     * @return 结果
     */
    private static byte[] initTripleDES(boolean isEnc, byte[] key, byte[] data) {
        // 根据给定的字节数组和算法构造一个密钥
        SecretKey desKey = new SecretKeySpec(key, TripleDES_ALGORITHM);
        int mode = isEnc ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        return doCipher(TripleDES_ALGORITHM, mode, desKey, null, data);
    }

    /**
     * TripleDES(3DES) 加密
     *
     * @param key  加密密钥，长度为24字节
     * @param data 字节数组（根据给定的字节数组构造一个密钥）
     * @return 加密结果
     */
    public static byte[] encryptTripleDES(byte[] key, String data) {
        return initTripleDES(true, key, data.getBytes());
    }

    /**
     * TripleDES(3DES) 解密
     *
     * @param key  密钥
     * @param data 需要解密的数据
     * @return 解密结果
     */
    public static String decryptTripleDES(byte[] key, byte[] data) {
        return new String(initTripleDES(false, key, data));
    }

    ///////////////////////// --------------PBE----------------------------

    /**
     * 定义加密方式 支持以下任意一种算法
     *
     * <pre>
     * PBEWithMD5AndDES
     * PBEWithMD5AndTripleDES
     * PBEWithSHA1AndDESede
     * PBEWithSHA1AndRC2_40
     * </pre>
     */
    private final static String KEY_PBE = "PBEWITHMD5andDES";

    /**
     * 初始化盐（salt）
     *
     * @return 盐（salt）
     */
    public static byte[] initSalt() {
        byte[] salt = new byte[8];
        new Random().nextBytes(salt);

        return salt;
    }

    private final static int SALT_COUNT = 100;

    /**
     * PBE 加解密
     *
     * @param isEnc 是否加密
     * @param key   密钥
     * @param data  数据
     * @return 结果
     */
    private static byte[] initPBE(boolean isEnc, String key, byte[] salt, byte[] data) {
        Key k = null;

        try {
            k = SecretKeyFactory.getInstance(KEY_PBE).generateSecret(new PBEKeySpec(key.toCharArray()));// 获取密钥，转换密钥
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.warn("WARN>>>>>", e);
            throw new RuntimeException(e);
        }

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, SALT_COUNT);
        int mode = isEnc ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        return doCipher(KEY_PBE, mode, k, parameterSpec, data);
    }

    /**
     * PBE 加密
     *
     * @param key  加密密钥
     * @param data 字节数组(根据给定的字节数组构造一个密钥。 )
     * @return 加密结果
     */
    public static byte[] encryptPBE(String key, byte[] salt, String data) {
        return initPBE(true, key, salt, data.getBytes());
    }

    /**
     * PBE 解密
     *
     * @param key  密钥
     * @param data 需要解密的数据
     * @return 解密结果
     */
    public static String decryptPBE(String key, byte[] salt, byte[] data) {
        return new String(initPBE(false, key, salt, data));
    }

    /**
     * 进行加密或解密，三步走
     *
     * @param algorithm 选择的算法
     * @param mode      是解密模式还是加密模式？
     * @param key       密钥
     * @param params    参数，可选的
     * @param s         输入的内容
     * @return 结果
     */
    public static byte[] doCipher(String algorithm, int mode, Key key, AlgorithmParameterSpec params, byte[] s) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);

            if (params != null)
                try {
                    cipher.init(mode, key, params);
                } catch (InvalidAlgorithmParameterException e) {
                    log.warn("WARN>>>>>", e);
                    throw new RuntimeException(e);
                }
            else
                cipher.init(mode, key);

            /*
             * 为了防止解密时报 javax.crypto.IllegalBlockSizeException: Input length must be
             * multiple of 8 when decrypting with padded cipher 异常， 不能把加密后的字节数组直接转换成字符串
             */
            return cipher.doFinal(s);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.warn("WARN>>>>>", e);
            throw new RuntimeException(e);
        }
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