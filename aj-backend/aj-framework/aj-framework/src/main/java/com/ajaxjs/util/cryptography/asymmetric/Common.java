package com.ajaxjs.util.cryptography.asymmetric;

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
     * 将密钥对的数据转换为字符串
     *
     * @param name    密钥对的 Key
     * @param keyPair 密钥对
     * @return 密钥的字符串形式
     */
    public static String getKey(String name, Map<String, byte[]> keyPair) {
        return Base64Utils.encodeToString(keyPair.get(name));
    }

    /**
     * 生成密钥对
     *
     * @param generator  密钥对生成器
     * @param publicKey  公钥
     * @param privateKey 私钥
     * @return 密钥对
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
     * @param algorithm  加密算法
     * @param keySize    密钥长度
     * @param publicKey  公钥
     * @param privateKey 私钥
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
