/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.net.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.function.Consumer;

import com.ajaxjs.util.logger.LogHelper;

/**
 * HEAD 请求
 * 
 * @author Frank Cheung
 *
 */
public class Head extends Base {
	private static final LogHelper LOGGER = LogHelper.getLog(Head.class);

	/**
	 * HEAD 请求
	 *
	 * @param url 请求目标地址
	 * @return 请求连接对象
	 */
	public static HttpURLConnection head(String url) {
		HttpURLConnection conn = initHttpConnection(url, "HEAD");
		conn.setInstanceFollowRedirects(false); // 必须设置 false，否则会自动 redirect 到 Location 的地址

		ResponseEntity response = connect(conn);

		if (response.getIn() != null) {// 不需要转化响应文本，节省资源
			try {
				response.getIn().close();
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		}
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
	 * 加入 HTTP 头为 JSON
	 */
	public final static Consumer<HttpURLConnection> GET_JSON = (head) -> {
		head.setRequestProperty("Content-Type", "application/json");
	};

	/**
	 * OAuth
	 */
	public final static Consumer<HttpURLConnection> oauth(String token) {
		return (head) -> head.setRequestProperty("Authorization", "Bearer " + token);
	}
}
