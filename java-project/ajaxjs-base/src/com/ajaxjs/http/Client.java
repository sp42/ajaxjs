package com.ajaxjs.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.util.StringUtil;

import sun.misc.BASE64Encoder;

public class Client extends Request {

	/**
	 * 连接对象
	 */
	private HttpURLConnection connection;

	public Client connect() throws ConnectException, IOException {
		init();
		
		// 写入数据（POST/PUT ONLY， GET 不需要）
		if (getData() != null && !getMethod().equalsIgnoreCase("GET")) {
			setOut(connection.getOutputStream());
			outputWriteData();// 写入 POST 数据
		}
		
		// 接受响应
		setIn(connection.getInputStream());
		
		// 是否启动 GZip 请求
		// 有些网站强制加入 Content-Encoding:gzip，而不管之前的是否有 GZip 的请求
		boolean isGzip = isEnableGzip() || "gzip".equals(connection.getHeaderField("Content-Encoding"));
		
		ConnectException err = checkErrCode(isGzip);
		if(err != null)throw err;
		
		if (getCallback() != null) {
			getCallback().onDataLoad(getIn());
		}
		
		if (isTextResponse()){
			if(isGzip)
				setIn(new GZIPInputStream(getIn()));
			byteStream2stringStream();
		}
		
		close();// 自动关闭流
		
		return this;
	}
	
	private ConnectException checkErrCode(boolean isGzip) throws IOException {
		if (connection.getResponseCode() >= 400) {// 如果返回的结果是400以上，那么就说明出问题了
			ConnectException e = null;

			if (connection.getResponseCode() < 500) {
				e = new ConnectException(connection.getResponseCode() + "：客户端请求参数错误！");
			} else {
				e = new ConnectException(connection.getResponseCode() + "：抱歉！我们服务端出错了！");
			}
			
			if(isGzip)
				setIn(new GZIPInputStream(getIn()));
			byteStream2stringStream();
			
			e.setFeedback(getContent());
		}
		
		return null;
	}

	/**
	 * 内部初始化
	 */
	private void init() {
		URL url = null;
		
		try {
			url = new URL(getUrl());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(getMethod());
		} catch (IOException e) {
			System.err.println("初始化连接出错！" + getUrl());
			e.printStackTrace();
		}
		
		connection.addRequestProperty("User-Agent", getUserAgent());
		connection.addRequestProperty("Referer", getUrl() == null ? url.getHost() : getUrl());
		connection.setConnectTimeout(getTimeout() * 1000);// 设置超时
		// connection.setReadTimeout(30000);

		if (getCookies() != null) {
			String cookieStr = StringUtil.HashJoin(getCookies(), ";");
			connection.setRequestProperty("Cookie", cookieStr);
		}
		
		if(isEnableGzip()) // 是否启动 GZip 请求
			connection.addRequestProperty("Accept-Encoding", "gzip, deflate"); 

		if (getBasicAuthorization() != null) { // HTTP 用户认证
			String username = getBasicAuthorization()[0], password = getBasicAuthorization()[1];
			String encoding = new BASE64Encoder().encode((username + ":" + password).getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encoding);
		}
	}
	
	/**
	 * 简单 GET 请求（原始 API 版），返回文本。
	 * 
	 * @param url
	 *            请求目标地址
	 * @return 响应内容（如 HTML，JSON 等）
	 */
	public static String simpleGET(String url) {
		Client client = new Client();
		
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
			Client client = new Client();
			client.setUrl(url);
			return client.connect().getContent();
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	 
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	public Client setConnection(HttpURLConnection connection) {
		this.connection = connection;

		return this;
	}
	
}