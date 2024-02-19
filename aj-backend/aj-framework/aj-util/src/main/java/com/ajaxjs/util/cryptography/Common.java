package com.ajaxjs.util.cryptography;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Base64Utils;

/**
 * @author Frank Cheung
 */
public class Common {
    /**
     * 获取指定名称的密钥
     *
     * @param name 密钥名称
     * @param map  密钥映射表
     * @return 密钥的 Base64 编码
     */
    public static String getKey(String name, Map<String, byte[]> map) {
        return Base64Utils.encodeToString(map.get(name));
    }

    /**
     * 生成一对密钥，并返回密钥对的 Base64 编码
     *
     * @param generator  密钥生成器
     * @param publicKey  公钥名称
     * @param privateKey 私钥名称
     * @return 密钥对的 Base64 编码
     */
    public static Map<String, byte[]> getKeyPair(KeyPairGenerator generator, String publicKey, String privateKey) {
        KeyPair keyPair = generator.generateKeyPair();

        Map<String, byte[]> map = new HashMap<>();
        map.put(publicKey, keyPair.getPublic().getEncoded());
        map.put(privateKey, keyPair.getPrivate().getEncoded());

        return map;
    }

    /**
     * 生成密钥对
     *
     * @param algorithm  算法
     * @param keySize    密钥长度
     * @param publicKey  公钥文件路径
     * @param privateKey 私钥文件路径
     * @return 密钥对
     */
    public static Map<String, byte[]> getKeyPair(String algorithm, int keySize, String publicKey, String privateKey) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
            generator.initialize(keySize);

            return getKeyPair(generator, publicKey, privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
    }

}
