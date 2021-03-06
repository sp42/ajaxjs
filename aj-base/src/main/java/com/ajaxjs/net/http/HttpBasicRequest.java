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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.util.Encode;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * 发送基础的 HTTP 请求
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class HttpBasicRequest extends StreamHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(HttpBasicRequest.class);

	/**
	 * 设置请求方法
	 */
	public final static BiConsumer<HttpURLConnection, String> setMedthod = (conn, method) -> {
		try {
			conn.setRequestMethod(method);
		} catch (ProtocolException e) {
			LOGGER.warning(e);
		}
	};

	/**
	 * 设置 cookies
	 */
	public final static BiConsumer<HttpURLConnection, Map<String, String>> setCookies = (conn, map) -> conn.addRequestProperty("Cookie", MapTool.join(map, ";"));

	/**
	 * 请求来源
	 */
	public final static BiConsumer<HttpURLConnection, String> setReferer = (conn, url) -> conn.addRequestProperty("Referer", url); // httpUrl.getHost()?

	/**
	 * 设置超时 （单位：秒）
	 */
	public final static BiConsumer<HttpURLConnection, Integer> setTimeout = (conn, timeout) -> conn.setConnectTimeout(timeout * 1000);

	/**
	 * 设置客户端识别
	 */
	public final static BiConsumer<HttpURLConnection, String> setUserAgent = (conn, url) -> conn.addRequestProperty("User-Agent", url);

	/**
	 * 默认的客户端识别
	 */
	public final static Consumer<HttpURLConnection> setUserAgentDefault = conn -> setUserAgent.accept(conn,
			"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

	/**
	 * HttpURLConnection 工厂函数，对输入的网址进行初始化
	 * 
	 * @param url 请求目的地址
	 * @return 请求连接对象
	 */
	public static HttpURLConnection initHttpConnection(String url) {
		URL httpUrl = null;

		try {
			httpUrl = new URL(url);
		} catch (MalformedURLException e) {
			LOGGER.warning(e, "初始化连接出错！URL[{0}]格式不对！", url);
		}

		try {
			return (HttpURLConnection) httpUrl.openConnection();
		} catch (IOException e) {
			LOGGER.warning(e, "初始化连接出错！URL[{0}]。", url);
		}

		return null;
	}

	/**
	 * 发送请求，返回响应信息
	 * 
	 * @param conn         请求连接对象
	 * @param isEnableGzip 是否需要 GZip 解码
	 * @param callback     回调里面请记得关闭 InputStream
	 * @return 可指定返回类型
	 */
	public static <T> T getResponse(HttpURLConnection conn, Boolean isEnableGzip, Function<InputStream, T> callback) {
		try {
			InputStream in = conn.getInputStream();// 发起请求，接收响应

			// 是否启动 GZip 请求
			// 有些网站强制加入 Content-Encoding:gzip，而不管之前的是否有 GZip 的请求
			boolean isGzip = isEnableGzip || "gzip".equals(conn.getHeaderField("Content-Encoding"));

			if (isGzip)
				in = new GZIPInputStream(in);

			int responseCode = conn.getResponseCode();

			if (responseCode >= 400) {// 如果返回的结果是400以上，那么就说明出问题了
				RuntimeException e = new RuntimeException(responseCode < 500 ? responseCode + "：客户端请求参数错误！" : responseCode + "：抱歉！我们服务端出错了！");
				LOGGER.warning(e);
			}

			if (callback == null) {
				in.close();
			} else
				return callback.apply(in);
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return null;
	}

	/**
	 * 设置启动 GZip 请求
	 */
	public final static Consumer<HttpURLConnection> setGizpRequest = conn -> conn.addRequestProperty("Accept-Encoding", "gzip, deflate");

	/////////////////////// --------- GET -------------///////////////////////

	/**
	 * 简单 GET 请求（原始 API 版），返回文本。
	 * 
	 * @param url 请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String simpleGET(String url) {
		try {
			return byteStream2string(new URL(url).openStream());
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * GET 请求，返回文本内容
	 * 
	 * @param url 请求目标地址
	 * @return 响应的文本内容
	 */
	public static String get(String url) {
		return get(url, false);
	}

	/**
	 * GET 请求，返回文本内容
	 * 
	 * @param url    请求目标地址
	 * @param isGzip true 表示为带 GZip 压缩
	 * @return 响应的文本内容
	 */
	public static String get(String url, boolean isGzip) {
		return get(url, isGzip, null);
	}

	/**
	 * GET 请求，返回文本内容
	 * 
	 * @param url    请求目标地址
	 * @param isGzip true 表示为带 GZip 压缩
	 * @return 响应的文本内容
	 */
	public static String get(String url, boolean isGzip, Consumer<HttpURLConnection> fn) {
		HttpURLConnection conn = initHttpConnection(url);
		if (isGzip)
			setGizpRequest.accept(conn);

		if (fn != null)
			fn.accept(conn);

		return getResponse(conn, isGzip, StreamHelper::byteStream2string);
	}

	/////////////////////// --------- POST -------------///////////////////////

	/**
	 * POST 请求
	 * 
	 * @param url  请求目标地址
	 * @param data 表单数据 KeyValue的请求数据，注意要进行 ? &amp; 编码，使用
	 * 
	 *             <pre>
	 *             URLEncoder.encode()
	 *             </pre>
	 * 
	 * @return 携带请求信息的 Bean
	 */
	public static String post(String url, Map<String, Object> data) {
		if (data != null && data.size() > 0) {
			return post(url, MapTool.join(data, v -> v == null ? null : Encode.urlEncode(v.toString())));
		} else {
			return null;
		}
	}

	/**
	 * POST 请求
	 * 
	 * @param url    请求目标地址
	 * @param params 字符串类型的请求数据，例如
	 * 
	 *               <pre>
	 * ip=35.220.250.107&amp;verifycode=
	 *               </pre>
	 * 
	 * @return 请求之后的响应的内容
	 */
	public static String post(String url, String params) {
		return post(url, params.getBytes(), null);
	}

	/**
	 * POST 请求
	 * 
	 * @param url 请求目标地址
	 * @param b   请求数据
	 * @param fn  对 Conn 进行配置的函数
	 * @return 请求之后的响应的内容
	 */
	public static String post(String url, byte[] b, Consumer<HttpURLConnection> fn) {
		return post(url, b, fn, null);
	}

	public static String post(String url, String params, Consumer<HttpURLConnection> fn) {
		return post(url, params.getBytes(), fn);
	}

	/**
	 * POST 请求
	 * 
	 * @param url 请求目标地址
	 * @param b   字节格式的请求数据
	 * @param fn  对 Conn 进行配置的函数
	 * @return 请求之后的响应的内容
	 */
	public static String post(String url, byte[] b, Consumer<HttpURLConnection> fn, Function<InputStream, String> responseHandler) {
		HttpURLConnection conn = initHttpConnection(url);
		setMedthod.accept(conn, "POST");
		conn.setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		conn.setDoInput(true);

		if (fn != null)
			fn.accept(conn);
		else
			setFormPost.accept(conn);

		try (OutputStream out = conn.getOutputStream();) {
			out.write(b); // 输出流写入字节数据
			out.flush();
		} catch (IOException e) {
			LOGGER.warning("写入 post 数据时失败！[{0}]", e);
		}

		return getResponse(conn, false, responseHandler == null ? StreamHelper::byteStream2string : responseHandler);
	}
	
	public static String put(String url, byte[] b, Consumer<HttpURLConnection> fn, Function<InputStream, String> responseHandler) {
		HttpURLConnection conn = initHttpConnection(url);
		setMedthod.accept(conn, "PUT");
		conn.setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		conn.setDoInput(true);
		
		if (fn != null)
			fn.accept(conn);
		else
			setFormPost.accept(conn);
		
		try (OutputStream out = conn.getOutputStream();) {
			out.write(b); // 输出流写入字节数据
			out.flush();
		} catch (IOException e) {
			LOGGER.warning("写入 post 数据时失败！[{0}]", e);
		}
		
		return getResponse(conn, false, responseHandler == null ? StreamHelper::byteStream2string : responseHandler);
	}
	
	
	public static String delete(String url, Consumer<HttpURLConnection> fn, Function<InputStream, String> responseHandler) {
		HttpURLConnection conn = initHttpConnection(url);
		setMedthod.accept(conn, "DELETE");
		conn.setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		conn.setDoInput(true);
		
		if (fn != null)
			fn.accept(conn);

		return getResponse(conn, false, responseHandler == null ? StreamHelper::byteStream2string : responseHandler);
	}

	/**
	 * 设置 POST 方式
	 */
	public final static Consumer<HttpURLConnection> setFormPost = conn -> conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

}
