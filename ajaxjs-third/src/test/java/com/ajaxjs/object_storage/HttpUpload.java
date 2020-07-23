package com.ajaxjs.object_storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.Encode;
import com.netease.cloud.auth.SigningAlgorithm;
import com.netease.cloud.services.nos.internal.NosSigner;
import com.netease.cloud.services.nos.internal.ServiceUtils;

public class HttpUpload {
	public static void main(String[] args) {
		ConfigService.load("c:\\project\\aj-website\\WebContent\\META-INF\\site_config.json");

		String accessKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.accessKey");
		String secretKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.secretKey");
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.bucket");

		String md5 = "", contentType = "";
		System.out.println(accessKey);
		System.out.println(secretKey);

		// 正确， 推荐使用.
		SimpleDateFormat sdf3 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		sdf3.setTimeZone(TimeZone.getTimeZone("GMT"));
		String now = ServiceUtils.formatRfc822DateShangHai(new Date());
		System.out.println("rfc1123_3 = " + now);

		String canonicalizedHeaders = "";
		String canonicalizedResource = "/";

		String data = "GET\n" + "\n" + "\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		System.out.println(data);

		X sg = new X();
		String signature = sg.signAndBase64Encode(data, secretKey, SigningAlgorithm.HmacSHA256);
//		String signature = Encode.base64Encode(HMACSHA256(data, secretKey));
		System.out.println(signature);

		String authorization = "NOS " + accessKey + ":" + signature;
		System.out.println(authorization);

		String result = HttpBasicRequest.get("http://nos-eastchina1.126.net", false, conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Date", now);
			conn.addRequestProperty("Host", "nos-eastchina1.126.net");
		});

		System.out.println(result);
	}

	public static void main2(String[] args) {
		ConfigService.load("d:\\project\\leidong\\WebContent\\META-INF\\site_config.json");

		String accessKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.accessKey");
		String secretKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.secretKey");
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.bucket");

		System.out.println(accessKey);

		long beginTime = System.currentTimeMillis();
		File file = new File("C:\\project\\ajaxjs-maven-global.xml");

		String md5 = calcMD5(file);
		long endTime = System.currentTimeMillis();
		System.out.println("MD5:" + md5 + "\n 耗时:" + ((endTime - beginTime) / 1000) + "s");

		Map<String, Object> map = new HashMap<>();
		map.put("name", "foo");
		map.put("file23", file);

		byte[] b = NetUtil.toFromData(map);

		String contentType = "text/xml";
		// 正确， 推荐使用.
		SimpleDateFormat sdf3 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		sdf3.setTimeZone(TimeZone.getTimeZone("GMT"));
		String now = sdf3.format(new Date());
		System.out.println("rfc1123_3 = " + now);

		String canonicalizedHeaders = "";
		String canonicalizedResource = "/leidong/";

		md5 = "";
		contentType = "";
		String data = "GET\n" + md5 + "\n" + contentType + "\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		String signature = Encode.base64Encode(HMACSHA256(data, secretKey));
		String authorization = "NOS " + accessKey + ":" + signature;
		System.out.println(authorization);

		String result = HttpBasicRequest.put("https://leidong.nos-eastchina1.126.net", b, conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Content-Length", file.length() + "");
			conn.addRequestProperty("Content-Type", "");
			conn.addRequestProperty("Content-MD5", "");
			conn.addRequestProperty("Date", now);
			conn.addRequestProperty("Host", "leidong.nos-eastchina1.126.net");
			// conn.addRequestProperty("x-nos-entity-type", "json");
		}, null);

		System.out.println(result);
	}

	public static String HMACSHA256(String data, String key) {

		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();

			for (byte item : array) {
				sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

			}

			return sb.toString().toUpperCase();
		} catch (Throwable e) {

			e.printStackTrace();

			return null;
		}

	}

	/**
	 * 计算文件 MD5
	 * 
	 * @param file
	 * @return 返回文件的md5字符串，如果计算过程中任务的状态变为取消或暂停，返回null， 如果有其他异常，返回空字符串
	 */
	protected static String calcMD5(File file) {
		try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] buf = new byte[8192];
			int len;

			while ((len = stream.read(buf)) > 0) {
				digest.update(buf, 0, len);
			}

			return toHexString(digest.digest());
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

	/**
	 * 
	 * @param data
	 * @return
	 */
	private static String toHexString(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);

		for (byte b : data) {
			r.append(hexCode[(b >> 4) & 0xF]);
			r.append(hexCode[(b & 0xF)]);
		}

		return r.toString();
	}
}
