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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * POST 请求
 * 
 * @author frank
 *
 */
public class Post extends HttpClient.Request {
	public Post(String url) {
		super(url);
	}

	private String FORM_POST_ContentType = "application/x-www-form-urlencoded;charset=utf-8";

	public String getFORM_POST_ContentType() {
		return FORM_POST_ContentType;
	}

	public void setFORM_POST_ContentType(String c) {
		FORM_POST_ContentType = c;
	}

	/**
	 * 在之前的基础上配置 POST 的参数
	 * @param conn
	 *            Http 连接
	 */
	@Override
	public void initConn(HttpURLConnection conn) {
		super.initConn(conn);

		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		conn.setDoOutput(true); // for conn.getOutputStream().write(someBytes);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-type", getFORM_POST_ContentType());

		writeData(conn);
	}

	/**
	 * 写入 POST 数据
	 * 
	 * @param conn
	 *            Http 连接
	 */
	private void writeData(HttpURLConnection conn) {
		if (getRequestData() != null) {
			try (OutputStream os = conn.getOutputStream()) {
				os.write(getRequestData().getBytes());
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String requestData;

	public String getRequestData() {
		return requestData;
	}

	/**
	 * 设置表单数据
	 * @param requestData KeyValue的请求数据，注意要进行 ? & 编码，使用 URLEncoder.encode()
	 */
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
}