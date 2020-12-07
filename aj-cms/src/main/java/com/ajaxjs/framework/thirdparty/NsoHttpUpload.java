package com.ajaxjs.framework.thirdparty;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
		String api = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.api");

		HttpBasicRequest.put(api + filename, new byte[0], conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Content-Length", "0");
			conn.addRequestProperty("Date", now);
//			conn.addRequestProperty("Host", "ajaxjs.nos-eastchina1.126.net");
		}, null);
	}
	
	public static boolean delete(String filename) {
		String bucket = ConfigService.get("uploadFile.ObjectStorageService.NOS.bucket");
		String now = getDate();
		String canonicalizedHeaders = "", canonicalizedResource = "/" + bucket + "/" + filename;
		String data = "DELETE\n" + "\n\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		String authorization = getAuthorization(data);
		String api = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.api");

		HttpBasicRequest.delete(api + filename, conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Date", now);
		}, null);
		
		return false;
	}

	/**
	 * 上传文件
	 * 
	 * @param filePath 文件路径
	 */
	public static boolean uploadFile(String filePath) {
		return uploadFile(filePath, null);
	}

	/**
	 * 上传文件
	 * 
	 * @param filePath 文件路径
	 * @param filename 文件名，若不指定则按原来的文件名
	 */
	public static boolean uploadFile(String filePath, String filename) {

		File file = new File(filePath);
		if (filename == null)
			filename = file.getName();

		return uploadFile(FileHelper.openAsByte(file), filename, calcMD5(file, null));
	}

	/**
	 * 
	 * @param bytes
	 * @param filename
	 * @param md5
	 */
	public static boolean uploadFile(byte[] bytes, String filename, String md5) {
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.bucket");

		String now = getDate();
		String canonicalizedHeaders = "", canonicalizedResource = "/" + bucket + "/" + filename;
		String data = "PUT\n" + md5 + "\n\n" + now + "\n" + canonicalizedHeaders + canonicalizedResource;
		String authorization = getAuthorization(data);
		String api = ConfigService.getValueAsString("uploadFile.ObjectStorageService.NOS.api");
		// "https://ajaxjs.nos-eastchina1.126.net/"

		final TempClz clz = new TempClz();
		HttpBasicRequest.put(api + filename, bytes, conn -> {
			conn.addRequestProperty("Authorization", authorization);
			conn.addRequestProperty("Content-Length", bytes.length + "");
//			conn.addRequestProperty("Content-Type", "");
			conn.addRequestProperty("Content-MD5", md5);
			conn.addRequestProperty("Date", now);
//			conn.addRequestProperty("HOST", "gdhdc-org.nos-eastchina1.126.net/cover");
			// conn.addRequestProperty("x-nos-entity-type", "json");
			clz.conn = conn;
		}, null);

		// 判定是否上传成功
		try {
			String ETag = clz.conn.getHeaderField("ETag");
			if (ETag == null)
				return false;

			if (clz.conn.getResponseCode() == 200 && ETag.equalsIgnoreCase("\"" + md5 + "\""))
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 解决 lambda final var
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	private static class TempClz {
		HttpURLConnection conn;
	}

	/**
	 * 计算文件 MD5
	 * 
	 * @param file
	 * @return 返回文件的md5字符串，如果计算过程中任务的状态变为取消或暂停，返回null， 如果有其他异常，返回空字符串
	 */
	public static String calcMD5(File file, byte[] bytes) {
		try (InputStream stream = file != null ? Files.newInputStream(file.toPath(), StandardOpenOption.READ) : new ByteArrayInputStream(bytes)) {
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
