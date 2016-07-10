/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.net.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.ajaxjs.util.FileUtil;
import com.ajaxjs.util.StringUtil;

/**
 * HTTP POST 请求
 * 
 * @author frank
 *
 */
public class Post {
	/**
	 * POST 请求
	 * 
	 * @param url
	 *            请求目标地址
	 * @param data
	 *            表单数据 KeyValue的请求数据，注意要进行 ? & 编码，使用 URLEncoder.encode()
	 * @return 携带请求信息的 Bean
	 */
	public static Request POST(String url, Map<String, Object> data) {
		if (data != null && data.size() > 0) {
			return POST(url, join(data).getBytes());
		} else {
			return null;
		}
	}
	
	public static Request POST(String url, String params) {
		return POST(url, params.getBytes());
	}
	
	public static Request POST(String url, byte[] b) {
		Request request = new Request();
		request.setMethod("POST");
		request.setUrl(url);
		request.setWriteData(b);

		RequestClient client = new RequestClient(request);
		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		try {
			client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		}

		request.setDone(true);
		return request;
	}

	/**
	 * Map 转换为 String
	 * 
	 * @param map
	 *            Map
	 * @return String
	 */
	public static String join(Map<String, Object> map) {
		String[] pairs = new String[map.size()];

		int i = 0;
		try {
			for (String key : map.keySet())
				pairs[i++] = key + "=" + URLEncoder.encode(map.get(key).toString(), StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return StringUtil.stringJoin(pairs, "&");
	}

	/**
	 * request 头和上传文件内容之间的分隔符
	 */
	private static final String BOUNDARY = "---------------------------123821742118716";

	/**
	 * 多段 POST 的分隔
	 */
	private static final String DIV = "\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n%s";

	/**
	 * 多段上传
	 * 
	 * @param url
	 *            请求目标地址
	 * @param text
	 *            文本数据
	 * @param fileMap
	 *            二进制（文件）数据
	 * @return 携带请求信息的 Bean
	 */
	public static Request MultiPOST(String url, Map<String, Object> text, Map<String, String> fileMap) {
		Request request = new Request();
		request.setMethod("POST");
		request.setUrl(url);

		byte[] data = null;
		if (text != null && text.size() > 0) {
			StringBuilder strs = new StringBuilder();

			for (String name : text.keySet()) {
				String value = text.get(name).toString();
				if (StringUtil.isEmptyString(value))
					continue;
				strs.append(String.format(DIV, BOUNDARY, name, value));

				// strs.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				// strs.append("Content-Disposition: form-data; name=\"" + name
				// + "\"\r\n\r\n");
				// strs.append(value);
			}

			data = strs.toString().getBytes();
		}

		if (fileMap != null && fileMap.size() > 0) {
			for (String name : fileMap.keySet()) {
				String value = fileMap.get(name);
				if (StringUtil.isEmptyString(value))
					continue;

				File file = new File(value);
				String filename = file.getName(), contentType = FileUtil.getMime(file);

				String str = String.format(DIV, BOUNDARY, name, filename) + "Content-Type:" + contentType + "\r\n\r\n";

				concat(data, str.getBytes());
				// StringBuilder strBuf = new StringBuilder();
				// strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				// strBuf.append(
				// "Content-Disposition: form-data; name=\"" + name + "\";
				// filename=\"" + filename + "\"\r\n");
				// strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
				//
				// out.write(strBuf.toString().getBytes());
				//
				// DataInputStream in = new DataInputStream(new
				// FileInputStream(file));
				// int bytes = 0;
				// byte[] bufferOut = new byte[1024];
				// while ((bytes = in.read(bufferOut)) != -1)
				// out.write(bufferOut, 0, bytes);
				// in.close();
			}
		}

		byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
		concat(data, endData);
		request.setWriteData(data);

		RequestClient client = new RequestClient(request);
		client.getConnection().setDoOutput(true); // for
													// conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-type", "multipart/form-data; boundary=" + BOUNDARY);

		try {
			client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
		}

		request.setDone(true);
		return request;

		// try (OutputStream out = new
		// DataOutputStream(client.getConnection().getOutputStream());) {
	}

	static byte[] concat(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}
