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
     * @param name
     * @param map
     * @return
     */
    public static String getKey(String name, Map<String, byte[]> map) {
        return Base64Utils.encodeToString(map.get(name));
    }

    /**
     * @param generator
     * @param publicKey
     * @param privateKey
     * @return
     */
    public static Map<String, byte[]> getKeyPair(KeyPairGenerator generator, String publicKey, String privateKey) {
        KeyPair keyPair = generator.generateKeyPair();

        Map<String, byte[]> map = new HashMap<>();
        map.put(publicKey, keyPair.getPublic().getEncoded());
        map.put(privateKey, keyPair.getPrivate().getEncoded());

        return map;
    }

    /**
     * @param algorithm
     * @param keySize
     * @param publicKey
     * @param privateKey
     * @return
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
