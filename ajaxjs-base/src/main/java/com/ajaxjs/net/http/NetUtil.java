/**
 * Copyright sp42 frank@ajaxjs.com
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.IoHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 比 HttpBasicRequest 提供更多的功能
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class NetUtil extends HttpBasicRequest {
	private static final LogHelper LOGGER = LogHelper.getLog(NetUtil.class);

	/**
	 * HTTP Basic 用户认证
	 */
	public final static BiConsumer<HttpURLConnection, String[]> setBasicAuth = (conn, auth) -> {
		String username = auth[0], password = auth[1];
		String encoding = Encode.base64Encode(username + ":" + password);
		conn.setRequestProperty("Authorization", "Basic " + encoding);
	};

	/**
	 * HEAD 请求
	 * 
	 * @param url 请求目标地址
	 * @return 请求连接对象
	 */
	public static HttpURLConnection head(String url) {
		HttpURLConnection conn = initHttpConnection(url);
		setMedthod.accept(conn, "HEAD");
		conn.setInstanceFollowRedirects(false); // 必须设置 false，否则会自动 redirect 到 Location 的地址

		getResponse(conn, false, null);// 不需要转化响应文本，节省资源
		return conn;
	}

	/**
	 * 得到 HTTP 302 的跳转地址
	 * 
	 * @param url 请求目标地址
	 * @return 跳转地址
	 */
	public static String get302redirect(String url) {
		return head(url).getHeaderField("Location");
	}

	/**
	 * 检测资源是否存在
	 * 
	 * @param url 请求目标地址
	 * @return true 表示 404 不存在
	 */
	public static boolean is404(String url) {
		try {
			return head(url).getResponseCode() == 404;
		} catch (IOException e) {
			LOGGER.warning(e);
			return false;
		}
	}

	/**
	 * 得到资源的文件大小
	 * 
	 * @param url 请求目标地址
	 * @return 文件大小
	 */
	public static long getFileSize(String url) {
		return head(url).getContentLength();
	}

	/**
	 * 写入磁盘的回调
	 * 
	 * @param saveDir  保存的目录
	 * @param fileName 文件名
	 * @return 下载文件的完整磁盘路径
	 */
	public static Function<InputStream, String> initDownload2disk_Callback(String saveDir, String fileName) {
		return in -> {
			File file = FileHelper.createFile(saveDir, fileName);

			try (OutputStream out = new FileOutputStream(file);) {
				write(in, out, true);
				LOGGER.info("文件 {0} 写入成功", file.toString());

				return file.toString();
			} catch (IOException e) {
				LOGGER.warning(e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.warning(e);
				}
			}

			return null;
		};
	}

	/**
	 * 下载二进制文件
	 * 
	 * @param url         请求目标地址
	 * @param saveDir     保存的目录
	 * @param newFileName 是否有新的文件名，如无请传 null
	 * @return 下载文件的完整磁盘路径
	 */
	public static String download(String url, String saveDir, String newFileName) {
		HttpURLConnection conn = initHttpConnection(url);
		setUserAgentDefault.accept(conn);
		conn.setDoInput(true);// for conn.getOutputStream().write(someBytes); 需要吗？
		conn.setDoOutput(true);

		String fileName = FileHelper.getFileNameFromUrl(url);
		if (newFileName != null) {
			// 新文件名 + 旧扩展名
			fileName = newFileName + CommonUtil.regMatch("\\.\\w+$", fileName);
		}

		String newlyFilePath = getResponse(conn, false, initDownload2disk_Callback(saveDir, fileName));
		return newlyFilePath;
	}

	/**
	 * 下载二进制文件
	 * 
	 * @param url     请求目标地址
	 * @param saveDir 保存的目录
	 * @return 下载文件的完整磁盘路径
	 */
	public static String download(String url, String saveDir) {
		return download(url, saveDir, null);
	}

	/**
	 * POST 方法请求，然后返回的响应是文件下载
	 * 
	 * @param url      请求目标地址
	 * @param data     请求数据
	 * @param saveDir  保存的目录
	 * @param fileName 文件名
	 * @return 下载文件的完整磁盘路径
	 */
	public static String postDownload(String url, Map<String, Object> data, String saveDir, String fileName) {
		if (data != null && data.size() > 0) {
			return post(url, "{\"path\":\"pages/index/index\"}".getBytes(), null, initDownload2disk_Callback(saveDir, fileName));
		} else {
			return null;
		}
	}

	/**
	 * 多段 POST 的分隔，request 头和上传文件内容之间的分隔符
	 */
	private static final String divField = "\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n%s";

	// 换行符
	private static final String newLine = "\r\n";
	private static final String boundaryPrefix = "--";
	// 定义数据分隔线
	public static String BOUNDARY = "------------7d4a6d158c9";
	private static String str = boundaryPrefix + BOUNDARY + newLine + "Content-Disposition: form-data;name=\"%s\";filename=\"%s\"" + newLine + "Content-Type:%s" + newLine
			+ newLine;

	// 定义最后数据分隔线，即--加上BOUNDARY再加上--。
	private static byte[] endData = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine).getBytes();

	/**
	 * Map 转换为 byte
	 * 
	 * @param data Map
	 * @return Map 转换为 byte
	 */
	public static byte[] toFromData(Map<String, Object> data) {
		byte[] bytes = null;

		for (String key : data.keySet()) {
			Object v = data.get(key);
			byte[] _bytes;

			if (v instanceof File) {
				File file = (File) v;
				String field = String.format(str, key, file.getName(), "application/octet-stream");

				_bytes = IoHelper.concat(field.getBytes(), FileHelper.openAsByte(file));
			} else { // 普通字段
				String field = String.format(divField, BOUNDARY, key, v.toString());
				_bytes = field.getBytes();
			}

			if (bytes == null) // 第一次时候为空
				bytes = _bytes;
			else
				bytes = IoHelper.concat(bytes, _bytes);
		}

		return IoHelper.concat(bytes, endData);
	}

	/**
	 * 多段上传
	 * 
	 * @param url  请求目标地址
	 * @param data 请求数据，若包含 File 对象则表示二进制（文件）数据
	 * @return 请求之后的响应的内容
	 */
	public static String multiPOST(String url, Map<String, Object> data) {
		return post(url, toFromData(data), conn -> conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY));
	}

}
