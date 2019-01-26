/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.ajaxjs.util.MapTool;
import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 远程请求器
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class Client extends Connection<Client> {
	private static final LogHelper LOGGER = LogHelper.getLog(Client.class);

	/**
	 * 创建一个远程请求器
	 * 
	 * @param url 请求目标地址
	 */
	public Client(String url) {
		super(url);
	}

	/**
	 * 简单 GET 请求（原始 API 版），返回文本。
	 * 
	 * @param url 请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String simpleGET(String url) {
		StreamUtil client = new StreamUtil();

		try {
			client.setIn(new URL(url).openStream());
			client.byteStream2stringStream().close();

			return client.getContent();
		} catch (IOException e) {// 简单调用，不作异常处理
			return null;
		}
	}

	/**
	 * 简单 GET 请求，返回文本。
	 * 
	 * @param url 请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String GET(String url) {
		try {
			return new Client(url).connect().getContent();
		} catch (ConnectException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 简单 GET 请求（带 GZip 处理），返回文本。
	 * 
	 * @param url 请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String GET_Gzip(String url) {
		try {
			return new Client(url).setEnableGzip(true).connect().getContent();
		} catch (ConnectException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * HEAD 请求之前的准备
	 * 
	 * @param url 请求目标地址
	 * @return 本类实例
	 */
	private static Client initHead(String url) {
		Client client = new Client(url);

		// 不需要转化响应文本，节省资源
		client.setMethod("HEAD").setTextResponse(false).customConnection(new CustomConnection() {
			@Override
			public void callback(HttpURLConnection connection) {
				connection.setInstanceFollowRedirects(false); // 必须设置 false，否则会自动 redirect 到 Location 的地址
			}
		});

		return client;
	}

	/**
	 * 检测资源是否存在
	 * 
	 * @param url 目标地址
	 * @return true 表示 404 不存在
	 */
	public static boolean is404(String url) {
		try {
			return initHead(url).connect().getConnection().getResponseCode() == 404;
		} catch (ConnectException e) {
			LOGGER.warning(e);

			if (e.getMessage().indexOf("404") != -1) // 已知异常为 404
				return true;
			return false;
		} catch (IOException e) {
			LOGGER.warning(e);
			return false;
		}
	}

	/**
	 * 得到 HTTP 302 的跳转地址
	 * 
	 * @param url 目标地址
	 * @return 跳转地址
	 */
	public static String get302redirect(String url) {
		try {
			return initHead(url).connect().getConnection().getHeaderField("Location");
		} catch (ConnectException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 得到资源的文件大小
	 * 
	 * @param url 目标地址
	 * @return 文件大小
	 */
	public static long getFileSize(String url) {
		String contentLength = null;

		try {
			contentLength = initHead(url).connect().getConnection().getHeaderField("content-length");
		} catch (ConnectException e) {
			LOGGER.warning(e);
			return 0;
		}

		return Long.parseLong(contentLength);
	}

	/**
	 * 获取远程资源的大小 （另外一种写法，可作为参考之）
	 * 
	 * @param url 目标地址
	 * @return 文件大小
	 */
	public static long getRemoteSize(String url) {
		long size = 0;

		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
			size = conn.getContentLength();
			conn.disconnect();
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return size;
	}

	/**
	 * POST 请求
	 * 
	 * @param url 请求目标地址
	 * @param data 表单数据 KeyValue的请求数据，注意要进行 ? & 编码，使用 URLEncoder.encode()
	 * @return 携带请求信息的 Bean
	 */
	public static String POST(String url, Map<String, Object> data) {
		if (data != null && data.size() > 0) {
			return POST(url, MapTool.join(data).getBytes());
		} else {
			return null;
		}
	}

	/**
	 * POST 请求
	 * 
	 * @param url 请求目标地址
	 * @param params 字符串类型的请求数据
	 * @return 请求之后的响应的内容
	 */
	public static String POST(String url, String params) {
		return POST(url, params.getBytes());
	}

	/**
	 * POST 请求
	 * 
	 * @param url 请求目标地址
	 * @param b 字节格式的请求数据
	 * @return 请求之后的响应的内容
	 */
	public static String POST(String url, byte[] b) {
		Client client = new Client(url);
		client.setMethod("POST").setData(b);
		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		try {
			client.connect();
		} catch (ConnectException e) {
			LOGGER.warning(e);
			return null;
		}

		client.setDone(true);
		return client.getContent();
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
	 * 模拟多段上传 TODO
	 * 
	 * @param url 请求目标地址
	 * @param text 文本数据
	 * @param fileMap 二进制（文件）数据
	 * @return 携带请求信息的 Bean
	 */
	public static String multiPOST(String url, Map<String, Object> text, Map<String, String> fileMap) {
		byte[] data = null;

 

		if (fileMap != null && fileMap.size() > 0) {
			for (String name : fileMap.keySet()) {
				String value = fileMap.get(name);

				File file = new File(value);
				String filename = file.getName();

				// String contentType = FileUtil.getMime(file);
				// String str = String.format(DIV, BOUNDARY, name, filename) + "Content-Type:" +
				// contentType + "\r\n\r\n";

				StringBuffer strBuf = new StringBuffer();
				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				strBuf.append("Content-Disposition: form-data; name=\"" + "ddd" + "\"; filename=\"" + filename + "\"\r\n");
				// strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
				// out.write(strBuf.toString().getBytes());

				StreamUtil.concat(data, strBuf.toString().getBytes());
				StreamUtil.concat(data, StreamUtil.fileAsByte(file));
			}
		}

		StreamUtil.concat(data, ("\r\n--" + BOUNDARY + "--\r\n").getBytes());

		Client client = new Client(url);
		client.setData(data);
		client.getConnection().setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		client.getConnection().setDoInput(true);
		client.getConnection().setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		try {
			client.connect();
		} catch (ConnectException e) {
			LOGGER.warning(e);
			return null;
		}

		client.setDone(true);

		return client.getContent();
	}
}