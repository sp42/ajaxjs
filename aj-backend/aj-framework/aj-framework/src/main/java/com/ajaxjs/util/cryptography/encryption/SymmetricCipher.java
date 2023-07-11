/**
 * Copyright 2015 sp42 frank@ajaxjs.com
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
package com.ajaxjs.util.cryptography.encryption;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

import com.ajaxjs.util.binrary.BytesUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 对称算法 SymmetricCipher
 *
 * @author sp42 frank@ajaxjs.com
 */
public class SymmetricCipher {
    private static final LogHelper LOGGER = LogHelper.getLog(SymmetricCipher.class);

    private final static SymmetricCipherInfo AES = new SymmetricCipherInfo("AES", 128);

    private final static SymmetricCipherInfo DES = new SymmetricCipherInfo("DES", 56);

    ///////////////////////// --------------AES----------------------------

    /**
     * AES 加密
     *
     * @param str 要加密的内容
     * @param key 密钥
     * @return 密文，加密后的内容
     */
    public static String AES_Encrypt(String str, String key) {
        // (这里要设置为 utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        bytes = SymmetricCipherInfo.doCipher(AES, Cipher.ENCRYPT_MODE, key, bytes);

        assert bytes != null;
        return Base64Utils.encodeToString(bytes);
    }

    /**
     * AES 解密
     *
     * @param str 密文，要解密的内容
     * @param key 密钥
     * @return 解密后的内容
     */
    public static String AES_Decrypt(String str, String key) {
        byte[] b = SymmetricCipherInfo.doCipher(AES, Cipher.DECRYPT_MODE, key, Base64Utils.decodeFromString(str));

        if (b == null || b.length == 0)
            return null;

        return BytesUtil.byte2String(b);
    }

    ///////////////////////// --------------DES----------------------------

    /**
     * DES 加密
     *
     * @param str 要加密的内容
     * @param key 密钥
     * @return 密文，加密后的内容
     */
    public static String DES_Encrypt(String str, String key) {
        return Base64Utils.encodeToString(Objects.requireNonNull(SymmetricCipherInfo.doCipher(DES, Cipher.ENCRYPT_MODE, key, str.getBytes(StandardCharsets.UTF_8))));

    }

    /**
     * DES 解密
     *
     * @param str 密文，要解密的内容
     * @param key 密钥
     * @return 解密后的内容
     */
    public static String DES_Decrypt(String str, String key) {
        return BytesUtil.byte2String(SymmetricCipherInfo.doCipher(DES, Cipher.DECRYPT_MODE, key, Base64Utils.decodeFromString(str)));
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
        SecretKey _key = new SecretKeySpec(key, TripleDES_ALGORITHM);
        int mode = isEnc ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        return SymmetricCipherInfo.doCipher(TripleDES_ALGORITHM, mode, _key, null, data);
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
     * @return 盐数据
     */
    public static byte[] init() {
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
            LOGGER.warning(e);
        }

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, SALT_COUNT);
        int mode = isEnc ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        return SymmetricCipherInfo.doCipher(KEY_PBE, mode, k, parameterSpec, data);
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
}
