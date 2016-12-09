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
package com.ajaxjs.http;


/**
 * 请求链接的异常
 * @author frank
 *
 */
public class ConnectException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建一个请求异常
	 * 
	 * @param msg
	 *            错误信息
	 */
	public ConnectException(String msg) {
		super(msg);
	}

	/**
	 * 出错信息
	 */
	private String feedback;

	/**
	 * 获取具体的服务端返回的出错信息
	 * @return 出错信息
	 */
	public String getFeedback() {
		return feedback;
	}

	/**
	 * 设置服务端返回的出错信息
	 * 
	 * @param feedback
	 *            出错信息
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
}
