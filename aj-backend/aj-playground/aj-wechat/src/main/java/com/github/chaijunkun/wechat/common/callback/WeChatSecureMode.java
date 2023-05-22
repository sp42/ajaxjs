package com.github.chaijunkun.wechat.common.callback;

import java.io.IOException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.github.chaijunkun.wechat.common.callback.WeChatCallbackException.CallbackErrEnum;

/**
 * 微信安全模式接入
 * <p>之所以不做成静态方式调用是因为密码不常改变,将加解密对象一次初始化好之后对加解密速度的提升有利</p>
 * <p>微信回调加密消息明文数据结构 :${16字节随机字符[0-9a-zA-Z]}${4字节网络字节序表示的内容字节长度}${实际内容}$微信接入appId</p>
 */
public class WeChatSecureMode {

    /**
     * 车商城微信服务端消息加密算法
     */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 微信接入的appId
     */
    private String appId;

    private Cipher encryptCipher;

    private Cipher decryptCipher;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 微信安全模式下解密后的数据结构
     *
     * @author chaijunkun
     * @since 2016年9月13日
     */
    public static class WeChatDecryptStruct {

        /**
         * 随机数据
         */
        private byte[] randomData;

        /**
         * 实际内容
         */
        private String content;

        /**
         * 微信接入appId
         */
        private byte[] appId;

        /**
         * 获取随机数据
         *
         * @return 随机数据
         */
        public byte[] getRandomData() {
            return randomData;
        }

        /**
         * 设置随机数据
         *
         * @param randomData 随机数据
         */
        public void setRandomData(byte[] randomData) {
            this.randomData = randomData;
        }

        /**
         * 获取实际内容
         *
         * @return 实际内容
         */
        public String getContent() {
            return content;
        }

        /**
         * 设置实际内容
         *
         * @param content 实际内容
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * 获取微信接入appId
         *
         * @return 微信接入appId
         */
        public byte[] getAppId() {
            return appId;
        }

        /**
         * 设置微信接入appId
         *
         * @param appId 微信接入appId
         */
        public void setAppId(byte[] appId) {
            this.appId = appId;
        }

    }

    //本类仅用于回调中,不能随便初始化,因此放入回调包中,作为dispatcher的友元,只能由dispatcher初始化
    protected WeChatSecureMode(String appId, String encodingAesKey) throws WeChatCallbackException {
        if (StringUtils.isBlank(appId)) {
            throw new IllegalArgumentException("illegal appId parameter, it can not be blank");
        }
        this.appId = appId;
        if (null == encodingAesKey || encodingAesKey.length() != 43) {
            throw new IllegalArgumentException("illegal encodingAESKey size, it should be 43 characters");
        }
        byte[] defaultKey = Base64.decodeBase64(encodingAesKey + "=");
        try {
            decryptCipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(defaultKey, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(defaultKey, 0, 16);
            decryptCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        } catch (Exception e) {
            throw new WeChatCallbackException(CallbackErrEnum.CallbackDecryptionErr, e);
        }
        try {
            encryptCipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(defaultKey, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(defaultKey, 0, 16);
            encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        } catch (Exception e) {
            throw new WeChatCallbackException(CallbackErrEnum.CallbackEncryptionErr, e);
        }
    }

    /**
     * AES解密消息(用于车商城微信服务端消息解密)
     *
     * @param cipher 密文
     * @return
     * @throws WeChatCallbackException
     */
    public WeChatDecryptStruct decryptMsg(String cipher) throws WeChatCallbackException {
        //密文先是Base64编码的，需要先解码
        byte[] cipherData = Base64.decodeBase64(cipher);

        try {
            //对解码后的密文数据进行解密
            byte[] plainData = decryptCipher.doFinal(cipherData);
            //为了保证正常的数据结构,解密后的数据大小应不小于 16+4+1+1字节
            if (plainData.length < 22) {
                throw new IllegalArgumentException("decrypted data do not reach the minimum size of correct data structure");
            }
            //取出前16字节的随机数
            byte[] randomField = ArrayUtils.subarray(plainData, 0, 16);
            //取出描述数据长度字段
            byte[] xmlLengthField = ArrayUtils.subarray(plainData, 16, 20);
            int xmlLen = 0;
            for (byte data : xmlLengthField) {
                xmlLen <<= 8;
                xmlLen |= data & 0xFF;
            }
            //按上一步得到的数据长度描述截取核心内容数据
            byte[] contentField = ArrayUtils.subarray(plainData, 20, 20 + xmlLen);
            //获取appId
            byte[] appIdField = ArrayUtils.subarray(plainData, 20 + xmlLen, plainData.length);
            String decryptAppId = new String(appIdField);
            if (!this.appId.equals(decryptAppId)) {
                throw new IllegalArgumentException("this callback data is not fit for the current appId");
            }
            WeChatDecryptStruct struct = new WeChatDecryptStruct();
            struct.setRandomData(randomField);
            struct.setContent(new String(contentField));
            struct.setAppId(appIdField);
            return struct;
        } catch (Exception e) {
            throw new WeChatCallbackException(CallbackErrEnum.CallbackDecryptionErr, e);
        }
    }

    /**
     * 生成4个字节的网络字节序
     *
     * @param xmlLength
     * @return
     */
    private byte[] convertIntToByteArray(int xmlLength) {
        byte[] orderBytes = new byte[4];
        for (int i = orderBytes.length; i > 0; i--) {
            orderBytes[i - 1] = (byte) (xmlLength & 0xFF);
            //无符号右移一个字节
            xmlLength >>>= 8;
        }
        return orderBytes;
    }

    /**
     * AES加密消息(用于车商城微信服务端消息加密)
     *
     * @param plain
     * @param randomData
     * @param appId
     * @return
     * @throws WeChatCallbackException
     * @throws IOException
     */
    public String encryptMsg(String plain, byte[] randomData, byte[] appId) throws WeChatCallbackException, IOException {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            //写入随机数
            out.write(randomData);
            byte[] plainField = plain.getBytes();
            //写入网络字节序的数据长度
            out.write(this.convertIntToByteArray(plainField.length));
            //写入明文数据
            out.write(plainField);
            //写入appId
            out.write(appId);
            byte[] cipherData = encryptCipher.doFinal(out.toByteArray());
            return Base64.encodeBase64String(cipherData);
        } catch (Exception e) {
            throw new WeChatCallbackException(CallbackErrEnum.CallbackEncryptionErr, e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

}
