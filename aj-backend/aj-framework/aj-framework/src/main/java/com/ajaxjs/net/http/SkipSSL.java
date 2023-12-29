package com.ajaxjs.net.http;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

/**
 * 全局忽略 HTTPS 证书
 */
public class SkipSSL {
    private static class MyX509TrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
    }

    /**
     * 带证书的安全连接
     *
     * @param kms 证书管理器
     * @return SSLSocketFactory
     */
    public static SSLSocketFactory getSocketFactory(KeyManager[] kms) {
        TrustManager[] trustAllCerts = new TrustManager[]{new MyX509TrustManager()};

        try {
            SSLContext sc = SSLContext.getInstance("SSL"); // 有人用 SSLContext.getInstance("TLS");
            /*
             * 第一个参数是授权的密钥管理器，用来授权验证，参数1为 null，则不上传客户端证书（通常情况都是如此）；
             * 第二个是被授权的证书管理器，用来验证服务器端的证书。第三个参数是一个随机数值，可以填写 null
             */
            sc.init(kms, trustAllCerts, null);

            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建带证书的安全连接
     *
     * @param in  证书文件流
     * @param pwd 证书密码
     * @return 证书管理器
     */
    static KeyManager[] loadCert(InputStream in, String pwd) {
        try {
            // 将证书加载进证书库
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(in, pwd.toCharArray());

            // 初始化秘钥管理器
            // 有人用 getInstance("SunX509")
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, pwd.toCharArray());

            return kmf.getKeyManagers();
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建带证书的安全连接
     *
     * @param path 证书路径
     * @param pwd  证书密码
     * @return 证书管理器
     */
    static KeyManager[] loadCert(String path, String pwd) {
        try (FileInputStream in = new FileInputStream(path)) {
            return loadCert(in, pwd);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean init;

    /**
     * 全局设置
     */
    public static void init() {
        if (!init) {
            HttpsURLConnection.setDefaultSSLSocketFactory(Objects.requireNonNull(getSocketFactory(null)));
            HttpsURLConnection.setDefaultHostnameVerifier((urlHostName, session) -> true);

            init = true;
        }
    }

    /**
     * 单次忽略
     */
    public static void setSSL_Ignore(HttpsURLConnection conn) {
        conn.setSSLSocketFactory(getSocketFactory(null));
//        conn.setHostnameVerifier(new HostnameVerifier() {// 这一步好像不需要
//            @Override
//            public boolean verify(String urlHostName, SSLSession session) {
//                return true;
//            }
//        });
        // 这一步好像不需要
        conn.setHostnameVerifier((urlHostName, session) -> true);
    }
}
