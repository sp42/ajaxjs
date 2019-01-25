package com.ajaxjs.util;

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

import com.ajaxjs.util.logger.LogHelper;

public class NetUtil extends IoHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(NetUtil.class);

	/**
	 * 简单 GET 请求（原始 API 版），返回文本。
	 * 
	 * @param url 请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String simpleGET(String url) {
		try {
			return byteStream2stringStream(new URL(url).openStream());
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

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
	 * 客户端识别
	 */
	public final static BiConsumer<HttpURLConnection, String> setUserAgent = (conn, url) -> conn.addRequestProperty("User-Agent", url);

	/**
	 * 默认的客户端识别
	 */
	public final static Consumer<HttpURLConnection> setUserAgentDefault = conn -> setUserAgent.accept(conn, "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

	/**
	 * HTTP Basic 用户认证
	 */
	public final static BiConsumer<HttpURLConnection, String[]> setBasicAuth = (conn, auth) -> {
		String username = auth[0], password = auth[1];
		String encoding = Encode.base64Encode(username + ":" + password);
		conn.setRequestProperty("Authorization", "Basic " + encoding);
	};

	/**
	 * 设置启动 GZip 请求
	 */
	public final static Consumer<HttpURLConnection> setGizpRequest = conn -> conn.addRequestProperty("Accept-Encoding", "gzip, deflate");

	public static HttpURLConnection initHttpConnection(String url) {
		URL httpUrl = null;

		try {
			httpUrl = new URL(url);
		} catch (MalformedURLException e) {
			LOGGER.warning(e, "初始化连接出错！URL {0} 格式不对！", url);
		}

		try {
			return (HttpURLConnection) httpUrl.openConnection();
		} catch (IOException e) {
			LOGGER.warning(e, "初始化连接出错！URL {0}。", url);
		}

		return null;
	}

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

//	public static void initHttpConnection(HttpURLConnection conn, @SuppressWarnings("unchecked") Consumer<HttpURLConnection>... setters) {
//		for (Consumer<HttpURLConnection> setter : setters) {
//			setter.accept(conn);
//		}
//	}

	public static Function<InputStream, String> getContent = in -> byteStream2stringStream(in);

	/**
	 * GET 请求，返回文本内容
	 * 
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		HttpURLConnection conn = initHttpConnection(url);
		return getResponse(conn, false, getContent);
//		conn.setDoInput(true);
//		conn.setDoOutput(true);
//		conn.connect();
	}

	/**
	 * HEAD 请求
	 * 
	 * @param url
	 * @param headKey
	 * @return
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
	 * @param url 目标地址
	 * @return 跳转地址
	 */
	public static String get302redirect(String url) {
		return head(url).getHeaderField("Location");
	}

	/**
	 * 检测资源是否存在
	 * 
	 * @param url 目标地址
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
	 * @param url 目标地址
	 * @return 文件大小
	 */
	public static long getFileSize(String url) {
		return Long.parseLong(head(url).getHeaderField("content-length"));
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

	public static void download(String url) {
		HttpURLConnection conn = initHttpConnection(url);
		setUserAgentDefault.accept(conn);
		conn.setDoInput(true);
		conn.setDoOutput(true);
//		conn.connect();
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
			return POST(url, MapTool.join(data, v -> Encode.urlEncode(v.toString())));
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
		HttpURLConnection conn = initHttpConnection(url);
		setMedthod.accept(conn, "POST");

		try (OutputStream out = conn.getOutputStream();) {
			out.write(b); // 输出流写入字节数据
			out.flush();
		} catch (IOException e) {
			LOGGER.warning("写入 post 数据时失败！{0}", e);
		}

		conn.setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		return getResponse(conn, false, getContent);
	}

 
}
