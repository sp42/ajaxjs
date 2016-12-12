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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.io.StreamUtil;

public class Client extends Connection<Client> {
	public Client(String urlStr) {
		super(urlStr);
	}

	/**
	 * 简单 GET 请求（原始 API 版），返回文本。
	 * 
	 * @param url
	 *            请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String simpleGET(String url) {
		StreamUtil client = new StreamUtil();
		
		try {
			client.setIn(new URL(url).openStream());
			client.byteStream2stringStream().close();
			
			return client.getContent();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 简单 GET 请求，返回文本。
	 * 
	 * @param url
	 *            请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String GET(String url) {
		try {
			return new Client(url).connect().getContent();
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		} 
	 
	}
	
	public static String GET_Gzip(String url) {
		try {
			return new Client(url).setEnableGzip(true).connect().getContent();
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	private static Client initHead(String url) {
		Client client = new Client(url);
		
		// 不需要转化响应文本，节省资源
		// 必须设置false，否则会自动redirect到Location的地址
		client.setMethod("HEAD").setTextResponse(false).customConnection(new CustomConnection() {
			@Override
			public void callback(HttpURLConnection connection) {
				connection.setInstanceFollowRedirects(false); // 必须设置false，否则会自动redirect到Location的地址
			}
		});
		
		return client;
	}
	
	/**
	 * 检测资源是否存在
	 * 
	 * @param url
	 *            目标地址
	 * @return true 表示 404 不存在
	 */
	public static boolean is404(String url) {
		try {
			return initHead(url).connect().getConnection().getResponseCode() == 404;
		} catch (ConnectException e) {
			e.printStackTrace();

			if (e.getMessage().indexOf("404") != -1) // 已知异常为 404
				return true;
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 得到 HTTP 302 的跳转地址
	 * 
	 * @param url
	 *            目标地址
	 * @return 跳转地址
	 */
	public static String get302redirect(String url) {
		try {
			return initHead(url).connect().getConnection().getHeaderField("Location");
		} catch (ConnectException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 得到资源的文件大小
	 * 
	 * @param url
	 *            目标地址
	 * @return 文件大小
	 */
	public static long getFileSize(String url) {
		String contentLength = null;
		
		try {
			contentLength = initHead(url).connect().getConnection().getHeaderField("content-length");
		} catch (ConnectException e) {
			e.printStackTrace();
			return 0;
		}
		
		return Long.parseLong(contentLength);
	}
	
	/**
	 * 获取远程资源的大小 （另外一种写法，可参考之）
	 * 
	 * @param url
	 *            目标地址
	 * @return 文件大小
	 */
	public static long getRemoteSize(String url) {
		long size = 0;

		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
			size = conn.getContentLength();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return size;
	}
	
	/**
	 * POST 请求
	 * 
	 * @param url
	 *            请求目标地址
	 * @param data
	 *            表单数据 KeyValue的请求数据，注意要进行 ? & 编码，使用 URLEncoder.encode()
	 * @return 携带请求信息的 Bean
	 */
	public static String POST(String url, Map<String, Object> data) {
		if (data != null && data.size() > 0) {
			return POST(url, join(data).getBytes());
		} else {
			return null;
		}
	}
	
	public static String POST(String url, String params) {
		return POST(url, params.getBytes());
	}
	
	public static String POST(String url, byte[] b) {
		Client client = new Client(url);
		client.setMethod("POST").setData(b);
		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		try {
			client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		}

		client.setDone(true);
		return client.getContent();
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
	public static String MultiPOST(String url, Map<String, Object> text, Map<String, String> fileMap) {
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
			
			System.out.println(strs.toString());
			data = strs.toString().getBytes();
		}

		if (fileMap != null && fileMap.size() > 0) {
			for (String name : fileMap.keySet()) {
				String value = fileMap.get(name);
				if (StringUtil.isEmptyString(value))
					continue;

				File file = new File(value);
				String filename = file.getName(), contentType = FileUtil.getMime(file);

//				String str = String.format(DIV, BOUNDARY, name, filename) + "Content-Type:" + contentType + "\r\n\r\n";
				StringBuffer strBuf = new StringBuffer();  
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");  
                strBuf.append("Content-Disposition: form-data; name=\"" + "ddd" + "\"; filename=\"" + filename + "\"\r\n");  
//                strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
				//
				// out.write(strBuf.toString().getBytes());
				//
                concat(data, strBuf.toString().getBytes());
				
                System.out.println(strBuf.toString());
                
                concat(data, getBytes(file));
                
			}
		}

		byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
		concat(data, endData);

		Client client = new Client(url);
		client.setData(data);
		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-type", "multipart/form-data; boundary=" + BOUNDARY);

		try {
			client.connect();
		} catch (ConnectException e) {
			e.printStackTrace();
		}

		client.setDone(true);
		
		return client.getContent();

		// try (OutputStream out = new
		// DataOutputStream(client.getConnection().getOutputStream());) {
	}

	static byte[] concat(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytes(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}