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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ajaxjs.Constant;
import com.ajaxjs.json.Json;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;
import com.ajaxjs.util.IO.text;
import com.ajaxjs.util.LogHelper;

/**
 * 仿 Apache 重新写的一个轮子，可以满足基本的 HTTP GET/POST 请求。
 * @author frank
 *
 */
public class HttpClient {
	private static final LogHelper LOGGER = LogHelper.getLog(HttpClient.class);
	
	/**
	 * 简单 GET 请求
	 * 
	 * @param url
	 *            请求目标地址
	 * @return 响应内容
	 */
	public static String GET(String url) {
		return new Request(url).getText();
	}

	/**
	 * 简单 GET 请求（原始 API 版）
	 * 
	 * @param url
	 *            请求目标地址
	 * @return 响应内容
	 */
	public static String simpleGET(String url) {
		try {
			return text.readStream(new URL(url).openStream());
		} catch (IOException e) {
			LOGGER.warning("请求出错" + url, e);
			return null;
		}
	}
	
	/**
	 * 简单 GET 请求
	 * 
	 * @param url
	 *            请求目标地址
	 * @return 响应内容
	 */
	public static String POST(String url) {
		return new Post(url).getText();
	}
	
	/**
	 * 读取远程接口，把返回的 JSON 转换为 Map
	 * 
	 * @param url
	 *            远程接口地址
	 * @return 响应内容 Map
	 */
	public static Map<String, Object> getRemoteJSON_Object(String url) {
		String json = GET(url);

		if (!StringUtil.isEmptyString(json)) {
//			LOGGER.info(json);
			return Json.callExpect_Map(json);
		} else {
			LOGGER.warning("异常：读取远程接口，不能把返回的 JSON 转换为 Map！");
			return null;
		}
	}
	
	/**
	 * 读取远程接口，把返回的 JSON 转换为 Map[]
	 * 
	 * @param url
	 *            远程接口地址
	 * @return 响应内容 Map[]
	 */
	public static Map<String, Object>[] getRemoteJSON_Array(String url) {
		String json = GET(url);

		if (!StringUtil.isEmptyString(json)) {
			return Json.callExpect_MapArray(json);
		} else {
			LOGGER.warning("异常：读取远程接口，不能把返回的 JSON 转换为 Map[]！");
			return null;
		}
	}
	
	/**
	 * 得到 http 302 的跳转地址
	 * 
	 * @param url
	 *            目标地址
	 * @return
	 * @throws IOException
	 */
	public static String get302redirect(String url) throws IOException {
		URL urlObj = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
		conn.setRequestMethod("HEAD"); // head 请求即可
		conn.setInstanceFollowRedirects(false); // 必须设置false，否则会自动redirect到Location的地址
		conn.addRequestProperty("Accept-Charset", "UTF-8;");
		conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
		conn.addRequestProperty("Referer", urlObj.getHost());
		conn.connect();

		return conn.getHeaderField("Location");
	}
	
	public static void main(String[] args) {
		URL urlObj;
		try {
//			urlObj = new URL("http://ck2.diediao.com/api23/dy.php?vid=3032807.acfun");
			urlObj = new URL("http://ck2.diediao.com/api23/dy.php?vid=2169045.acfun");
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("GET"); // head 请求即可
			conn.setInstanceFollowRedirects(false); // 必须设置false，否则会自动redirect到Location的地址
//			conn.addRequestProperty("Accept-Charset", "UTF-8;");
			conn.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25");
//			conn.addRequestProperty("Referer", urlObj.getHost());
			conn.connect();
			
			String l =  conn.getHeaderField("Location");
			System.out.println(l);
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 不要使用 很多坑
	 * @param url
	 */
	public static void aysncRequest(String url) {
		Request req = new Request(url);
		// 开始时调用，准备线程进行异步任务
		ExecutorService executor = Executors.newFixedThreadPool(1); 
		Future<Request> response = executor.submit(req);// 触发请求
		
		// 继续执行代码，当前的线程不会阻塞
		// 获取响应（响应返回的时候会返回到这里线程进行阻塞）  
		try {
			req = response.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.warning("异步出错！", e);
		}
		
		executor.shutdown();// 关闭线程
	}
	
	/**
	 * 封装 GET 请求，支持多种自定义
	 * @author frank
	 *
	 */
	public static class Request implements Callable<Request> {
	    private URL url;
	    
	    public URL getUrl(){
	    	return url;
	    }
	    
	    public void setUrl(URL url){
	    	this.url = url;
	    }
	    
	    private String urlStr;
	    
		public String getUrlStr() {
			return urlStr;
		}

		public void setUrlStr(String urlStr) {
			this.urlStr = urlStr;

			try {
				setUrl(new URL(urlStr));
			} catch (MalformedURLException e) {
				LOGGER.warning(urlStr + "不合法 ", e);
				
				setUrl(null);
				setUrlStr(null);
			}
		}
		
		/**
		 * 请求一个 url 地址
		 * 
		 * @param url
		 *            请求地址
		 */
	    public Request(String url) {
	    	this.setUrlStr(url);
	    }
	    
		/**
		 * 送入一个 URL 对象，可以对 Connection 先行配置
		 * 
		 * @param url
		 *            请求地址
		 */
	    public Request(URL url) {
	        this.url = url;
	    }
	    
	    /**
	     * 设置响应内容的编码
	     */
	    public String encoding = Constant.encoding_UTF8;

	    private InputStream body;
	    
		public InputStream getBody() {
			return body;
		}

		public void setBody(InputStream body) {
			this.body = body;
		}

		/**
		 * 本来不想写在类成员上，而是变量用的，但 discon 比较麻烦，所以
		 */
		public HttpURLConnection conn = null;
		
		/**
		 * 断开 http 连接
		 */
		private void closeConn(){
			conn.disconnect();
		}
		
		/**
		 * 
		 */
		private boolean called = false;
		
		/**
		 * 异步调用的
		 */
		@Override
		public Request call() {
			LOGGER.info("请求地址：" + getUrlStr());

			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				LOGGER.warning("初始化请求出错！" + getUrlStr(), e);
			}

			initConn(conn);

			InputStream body = null;
			try {
				try {
					body = conn.getInputStream();
				} catch (UnknownHostException e) {
					LOGGER.warning("未知地址！" + getUrlStr(), e);
				} catch (FileNotFoundException e) {
					LOGGER.warning("404 地址！" + getUrlStr(), e);
				} catch (SocketTimeoutException e) {
					LOGGER.warning("请求地址超时！" + getUrlStr(), e);
				}
				
				if (conn.getResponseCode() > 400) // 如果返回的结果是400以上，那么就说明出问题了
					LOGGER.warning("Err when got responseCode :" + conn.getResponseCode() + getUrlStr());

			} catch (IOException e) {
				LOGGER.warning("初始化请求出错！" + getUrlStr(), e);
			}

			if (body != null) {
				setBody(body);
				called = true;
			}

			return this;
		}
	    
		/**
		 * 可以重写这个函数实现更多的设置
		 * 
		 * @param conn
		 *            HTTP 链接
		 */
		public void initConn(HttpURLConnection conn) {
			conn.setRequestProperty("Host", url.getHost());
			conn.setRequestProperty("User-Agent", ua);
			conn.setConnectTimeout(5000);// 设置超时
			conn.setReadTimeout(30000);
		}
	    
		/**
		 * 返回请求内容（字符串）
		 * 
		 * @return
		 */
		public String getText() {
			if (!called) call();

			String text = com.ajaxjs.util.IO.text.readStream(getBody(), encoding);
			closeConn();
			return text;
		}
		
		/**
		 * 在请求中塞入 cookie
		 * 
		 * @param conn
		 *            HTTP 链接
		 * @param cookies
		 */
		public static void setCookie(HttpURLConnection conn, Map<String, String> cookies) {
			String cookieStr = StringUtil.HashJoin(cookies, ";");
			conn.setRequestProperty("Cookie", cookieStr);
		}
		
		/**
		 * 浏览器标识
		 */
		public String ua = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	}
	
	
	/**
	 * HTTP Basic 认证
	 * 
	 * @author frank
	 *
	 */
	public static class BasicAuthRequest extends Request {
		public String theUsername;
		public String thePassword;

		public BasicAuthRequest(String url) {
			super(url);
		}

		@Override
		public void initConn(HttpURLConnection conn) {
			super.initConn(conn);

			String encoding = Util.base64Encode(theUsername + ":" + thePassword);
			conn.setRequestProperty("Authorization", "Basic " + encoding);
		}
	}
}
