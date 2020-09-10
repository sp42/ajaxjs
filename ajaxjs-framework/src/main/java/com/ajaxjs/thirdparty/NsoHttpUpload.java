package com.ajaxjs.thirdparty;

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

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetriCipher;
import com.ajaxjs.util.io.FileHelper;

/**
 * 网易云对象存储 HTTP 文件上传
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class NsoHttpUpload {
	/**
	 * 列出所有的桶
	 * 
	 * @return XML 结果
	 */
	public static String listBuk() {
		String now = getDate();
		String canonicalizedHeaders = "", canonicalizedResource = "/";
		String data = "GET\n" + "\n" + "\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		String authorization = getAuthorization(data);
		String xmlResult = HttpBasicRequest.get("http://nos-eastchina1.126.net", false, conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Date", now);
			conn.addRequestProperty("Host", "nos-eastchina1.126.net");
		});

		return xmlResult;
	}

//	public static void main(String[] args) {
//		ConfigService.load("c:\\project\\aj-website-site_config.json");
//		System.out.println(listBuk());
//		createEmptyFile("test.jpg");
//		uploadFile("C:\\project\\ajaxjs-maven-global.xml");
//	}

	/**
	 * 请求的时间戳，格式必须符合 RFC1123 的日期格式
	 * 
	 * @return 当前日期
	 */
	private static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.format(new Date());
	}

	/**
	 * 生成验证的字符串
	 * 
	 * @param data
	 * @return
	 */
	private static String getAuthorization(String data) {
		String accessKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.accessKey");
		String secretKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.secretKey");

		String signature = Encode.base64Encode(SymmetriCipher.HMACSHA256(data, secretKey));
		String authorization = "NOS " + accessKey + ":" + signature;

		return authorization;
	}

	/**
	 * 创建空文件
	 * 
	 * @param filename 文件名
	 */
	public static void createEmptyFile(String filename) {
		String bucket = ConfigService.get("uploadFile.ObjectStorageService.NOS.bucket");
		String now = getDate();
		String canonicalizedHeaders = "", canonicalizedResource = "/" + bucket + "/" + filename;
		String data = "PUT\n" + "\n\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		String authorization = getAuthorization(data);

		HttpBasicRequest.put("https://ajaxjs.nos-eastchina1.126.net/" + filename, new byte[0], conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Content-Length", "0");
			conn.addRequestProperty("Date", now);
			conn.addRequestProperty("Host", "ajaxjs.nos-eastchina1.126.net");
		}, null);
	}

	/**
	 * 上传文件
	 * 
	 * @param filePath 文件路径
	 */
	public static void uploadFile(String filePath) {
		uploadFile(filePath, null);
	}

/**
 * 上传文件
 * 
 * @param filePath 文件路径
 * @param filename 文件名，若不指定则按原来的文件名
 */
public static void uploadFile(String filePath, String filename) {
	String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.bucket");

	File file = new File(filePath);
	if (filename == null)
		filename = file.getName();

	String md5 = calcMD5(file);
	String now = getDate();
	String canonicalizedHeaders = "", canonicalizedResource = "/" + bucket + "/" + filename;
	String data = "PUT\n" + md5 + "\n\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
	String authorization = getAuthorization(data);
	HttpBasicRequest.put("https://ajaxjs.nos-eastchina1.126.net/" + filename, FileHelper.openAsByte(file), conn -> {
		conn.addRequestProperty("Authorization", authorization);
		conn.addRequestProperty("Content-Length", file.length() + "");
//			conn.addRequestProperty("Content-Type", "");
		conn.addRequestProperty("Content-MD5", md5);
		conn.addRequestProperty("Date", now);
		// conn.addRequestProperty("Host", "ajaxjs.nos-eastchina1.126.net");
		// conn.addRequestProperty("x-nos-entity-type", "json");
	}, null);
}

	/**
	 * 计算文件 MD5
	 * 
	 * @param file
	 * @return 返回文件的md5字符串，如果计算过程中任务的状态变为取消或暂停，返回null， 如果有其他异常，返回空字符串
	 */
	protected static String calcMD5(File file) {
		try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
			byte[] buf = new byte[8192];
			int len;
			MessageDigest digest = MessageDigest.getInstance("MD5");

			while ((len = stream.read(buf)) > 0)
				digest.update(buf, 0, len);

			return toHexString(digest.digest());
		} catch (IOException | NoSuchAlgorithmException e) {
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
