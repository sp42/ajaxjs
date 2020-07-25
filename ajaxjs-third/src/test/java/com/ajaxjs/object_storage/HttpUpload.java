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
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetriCipher;
import com.ajaxjs.util.io.FileHelper;

public class HttpUpload {
//	@Test
	public void listBuk() {
		ConfigService.load("c:\\project\\aj-website\\WebContent\\META-INF\\site_config.json");

		String now = getDate();
		String canonicalizedHeaders = "";
		String canonicalizedResource = "/";

		String data = "GET\n" + "\n" + "\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;

		String authorization = getAuthorization(data);
		System.out.println(authorization);

		String result = HttpBasicRequest.get("http://nos-eastchina1.126.net", false, conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Date", now);
			conn.addRequestProperty("Host", "nos-eastchina1.126.net");
		});

		System.out.println(result);
	}

	/**
	 * 请求的时间戳，格式必须符合 RFC1123 的日期格式
	 * 
	 * @return 当前日期
	 */
	private static String getDate() {
		SimpleDateFormat sdf3 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		sdf3.setTimeZone(TimeZone.getTimeZone("GMT"));
		String now = sdf3.format(new Date());

		return now;
	}

	private static String getAuthorization(String data) {
		String accessKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.accessKey");
		String secretKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.secretKey");

		String signature = Encode.base64Encode(SymmetriCipher.HMACSHA256(data, secretKey));
		String authorization = "NOS " + accessKey + ":" + signature;

		return authorization;
	}

//	@Test
	public void createEmptyFile() {
		ConfigService.load("c:\\project\\aj-website\\WebContent\\META-INF\\site_config.json");
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.bucket");

		System.out.println(bucket);

		String now = getDate();
		String canonicalizedHeaders = "";
		String canonicalizedResource = "/" + bucket + "/foo.xml";
		String data = "PUT\n" + "\n" + "\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		System.out.println(data);

		String authorization = getAuthorization(data);
		System.out.println(authorization);

		String result = HttpBasicRequest.put("https://ajaxjs.nos-eastchina1.126.net/foo.xml", new byte[0], conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Content-Length", "0");
//			conn.addRequestProperty("Content-Type", "");
//			conn.addRequestProperty("Content-MD5", "");
			conn.addRequestProperty("Date", now);
			conn.addRequestProperty("Host", "ajaxjs.nos-eastchina1.126.net");
			// conn.addRequestProperty("x-nos-entity-type", "json");
		}, null);

		System.out.println(result);
	}

	@Test
	public void uploadFile() {
		ConfigService.load("c:\\project\\aj-website\\WebContent\\META-INF\\site_config.json");
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.bucket");

		File file = new File("C:\\project\\ajaxjs-maven-global.xml");
		String md5 = calcMD5(file);
		System.out.println(bucket);

		String now = getDate();
		String canonicalizedHeaders = "";
		String canonicalizedResource = "/" + bucket + "/foo.xml";
		String data = "PUT\n" + md5 + "\n" + "\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		System.out.println(data);

		String authorization = getAuthorization(data);
		System.out.println(authorization);

		byte[] b = FileHelper.openAsByte(file);

		String result = HttpBasicRequest.put("https://ajaxjs.nos-eastchina1.126.net/foo.xml", b, conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Content-Length", file.length() + "");
//			conn.addRequestProperty("Content-Type", "");
			conn.addRequestProperty("Content-MD5", md5);
			conn.addRequestProperty("Date", now);
			conn.addRequestProperty("Host", "ajaxjs.nos-eastchina1.126.net");
			// conn.addRequestProperty("x-nos-entity-type", "json");
		}, null);

		System.out.println(result);
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
