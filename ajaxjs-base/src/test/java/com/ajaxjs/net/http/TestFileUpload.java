package com.ajaxjs.net.http;

import java.util.HashMap;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

public class TestFileUpload {
	//	public static void main2(String[] args) {
	//		String url = "http://192.168.1.141:8080/gdzbt/user/1/avatar/upload.do";
	//		Request request = Post.MultiPOST(url, new HashMap<String, Object>() {
	//			private static final long serialVersionUID = 1L;
	//			{
	//				put("foo", "bar");
	//			}
	//		}, new HashMap<String, String>() {
	//			private static final long serialVersionUID = 1L;
	//			{
	//				put("foo2", "C:\\Users\\frank\\Desktop\\11.jpg");
	//			}
	//		});
	//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filepath = "C:\\Users\\frank\\Desktop\\11.jpg";
		String urlStr = "http://192.168.1.141:8080/gdzbt/user/1/avatar/upload.do";
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("name", "testname");
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("userfile", filepath);
		formUpload(urlStr, textMap, fileMap);
	}

	/**
	 * 上传图片
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符

		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = getMime(file);

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					strBuf.append("Content-Length:266" + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * 获取文件名的 MIME 类型
	 * 
	 * @param file
	 *            文件对象
	 * @return MIME 类型
	 */
	public static String getMime(File file) {
		String contentType = new MimetypesFileTypeMap().getContentType(file);
		if (file.getName().endsWith(".png"))
			contentType = "image/png"; // TODO needs?
		if (contentType == null)
			contentType = "application/octet-stream";
		return contentType;
	}
}
