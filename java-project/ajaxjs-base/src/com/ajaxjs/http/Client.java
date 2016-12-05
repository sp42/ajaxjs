package com.ajaxjs.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.util.StringUtil;

import sun.misc.BASE64Encoder;

public class Client extends Request<Client> {

	
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