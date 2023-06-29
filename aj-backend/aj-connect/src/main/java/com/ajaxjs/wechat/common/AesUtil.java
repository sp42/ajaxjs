package com.ajaxjs.wechat.common;

import com.ajaxjs.util.binrary.BytesUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 证书和回调报文解密
 *
 * @author Frank Cheung
 */
public class AesUtil {
    private static final int KEY_LENGTH_BYTE = 32;
    private static final int TAG_LENGTH_BIT = 128;

    private final byte[] aesKey;

    /**
     * 解密器
     *
     * @param key 密钥
     */
    public AesUtil(byte[] key) {
        if (key.length != KEY_LENGTH_BYTE)
            throw new IllegalArgumentException("无效的 ApiV3Key，长度必须为32个字节");

        this.aesKey = key;
    }

    /**
     * AEAD_AES_256_GCM 解密
     *
     * @param associatedData 相关数据
     * @param nonce          随机字符串
     * @param ciphertext     密文
     * @return 解密后的文本
     * @throws GeneralSecurityException 异常
     */
    public String decryptToString(byte[] associatedData, byte[] nonce, String ciphertext) throws GeneralSecurityException {
        SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);

            return BytesUtil.byte2String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 解密小程序提供的加密数据，返回包含手机号码等信息的 JSON 对象
     *
     * @param iv         前端给的
     * @param ciphertext 前端给的，密文
     * @param sessionKey 后端申请返回
     * @return 解密后的文本
     */
    public static String decryptPhone(String iv, String ciphertext, String sessionKey) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] sessionKeyBytes = decoder.decode(sessionKey);
        byte[] ivBytes = decoder.decode(iv);

        SecretKeySpec key = new SecretKeySpec(sessionKeyBytes, "AES");
        IvParameterSpec spec = new IvParameterSpec(ivBytes);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            return BytesUtil.byte2String(cipher.doFinal(decoder.decode(ciphertext)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
