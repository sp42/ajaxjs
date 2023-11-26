package com.ajaxjs.cms.service.wechat.common;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.io.Resources;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * 私钥文件工具类
 */
public class PemUtil {
    private static String privateKeyContent;

    /**
     * 从 classpath 上指定私钥文件的路径
     *
     * @param privateKeyPath 私钥文件的路径
     * @return 私钥文件 PrivateKey
     */
    public static PrivateKey loadPrivateKeyByPath(String privateKeyPath) {
        if (privateKeyContent == null)
            privateKeyContent = Resources.getResourceText(privateKeyPath); // cache it

        return loadPrivateKey(privateKeyContent);
    }

    /**
     * 转换为 Java 里面的 PrivateKey 对象
     *
     * @param privateKey 私钥内容
     * @return 私钥对象
     */
    public static PrivateKey loadPrivateKey(String privateKey) {
        Objects.requireNonNull(privateKey, "没有私钥内容");
        privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * @param inputStream 输入流
     * @return 私钥
     */
    public static PrivateKey loadPrivateKey(InputStream inputStream) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(2048);
        byte[] buffer = new byte[1024];
        String privateKey;

        try {
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                os.write(buffer, 0, length);
            }

            privateKey = os.toString(StrUtil.UTF8_SYMBOL);
        } catch (IOException e) {
            throw new IllegalArgumentException("无效的密钥", e);
        }

        return loadPrivateKey(privateKey);
    }

    /**
     * 反序列化证书并解密
     *
     * @param apiV3Key APIv3 密钥
     * @param pMap     下载证书的请求返回体
     * @return 证书 list
     * @throws GeneralSecurityException 当证书过期或尚未生效时
     */
    @SuppressWarnings("unchecked")
    public static Map<BigInteger, X509Certificate> deserializeToCerts(String apiV3Key, Map<String, Object> pMap) throws GeneralSecurityException {
        byte[] apiV3KeyByte = StrUtil.getUTF8_Bytes(apiV3Key);

        AesUtil aesUtil = new AesUtil(apiV3KeyByte);
        List<Map<String, Object>> list = (List<Map<String, Object>>) pMap.get("data");
        Map<BigInteger, X509Certificate> newCertList = new HashMap<>();

        if (!CollectionUtils.isEmpty(list)) {
            for (Map<String, Object> map : list) {
                Map<String, Object> certificate = (Map<String, Object>) map.get("encrypt_certificate");

                // 解密
                String cert = aesUtil.decryptToString(StrUtil.getUTF8_Bytes(remove(certificate.get("associated_data"))), StrUtil.getUTF8_Bytes(remove(certificate.get("nonce"))),
                        remove(certificate.get("ciphertext")));

                CertificateFactory cf = CertificateFactory.getInstance("X509");
                X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cert.getBytes(StandardCharsets.UTF_8)));

                try {
                    x509Cert.checkValidity();
                } catch (CertificateExpiredException | CertificateNotYetValidException ignored) {
                    continue;
                }

                newCertList.put(x509Cert.getSerialNumber(), x509Cert);
            }
        }

        return newCertList;
    }

    private static String remove(Object v) {
        return v.toString().replace("\"", "");
    }
}
