package com.ajaxjs.net.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 全局忽略 HTTPS 证书
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
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
	 * @return
	 */
	public static SSLSocketFactory getSocketFactory(KeyManager[] kms) {
		TrustManager[] trustAllCerts = new TrustManager[] { new MyX509TrustManager() };
		SSLContext sc = null;

		try {
			sc = SSLContext.getInstance("SSL"); // 有人用 SSLContext.getInstance("TLS");

			/*
			 * 第一个参数是授权的密钥管理器，用来授权验证，参数1为 null，则不上传客户端证书（通常情况都是如此）； TrustManager[]
			 * 第二个是被授权的证书管理器，用来验证服务器端的证书。第三个参数是一个随机数值，可以填写 null
			 */
			sc.init(kms, trustAllCerts, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
			return null;
		}

		return sc.getSocketFactory();
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
		try (FileInputStream in = new FileInputStream(new File(path))) {
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
			HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(null));
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			});

			init = true;
		}
	}

	/**
	 * 单次忽略
	 */
	public static void setSSL_Ignore(HttpsURLConnection conn) {
		conn.setSSLSocketFactory(getSocketFactory(null));
		conn.setHostnameVerifier(new HostnameVerifier() {// 这一步好像不需要
			@Override
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		});
	}

	static {
//        init();
	}
}