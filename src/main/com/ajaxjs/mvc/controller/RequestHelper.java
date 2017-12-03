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
package com.ajaxjs.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 为获取请求的上下文，能够在控制器中拿到最常用的对象，例如 HttpServletRequest 和 HttpServletResponse 等的对象（甚至 Web App 的启动上下文（ 在web.xml 中配置的参数） ），因此还需要设计一个 RequestHelper 类，通过
 * ThreadLocal让控制器能轻易地访问到这些对象。 一个容器，向这个容器存储的对象，在当前线程范围内都可以取得出来，向
 * ThreadLocal 里面存东西就是向它里面的Map存东西的，然后 ThreadLocal 把这个 Map 挂到当前的线程底下，这样 Map
 * 就只属于这个线程了
 * 
 * @author frank
 *
 */
public class RequestHelper {
	private static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<>();

	/**
	 * 保存一个 request 对象
	 * @param request request 对象
	 */	
	public static void setHttpServletRequest(HttpServletRequest request) {
		threadLocalRequest.set(request);
	}

	public static HttpServletRequest getHttpServletRequest() {
		HttpServletRequest request = threadLocalRequest.get();
		if (request == null)
			throw new RuntimeException("请求对象未初始化");

		return request;
	}

	/**
	 * 保存一个 response 对象
	 * @param response response 对象
	 */
	public static void setHttpServletResponse(HttpServletResponse response) {
		threadLocalResponse.set(response);
	}

	/**
	 * 获取上下文中 response 对象
	 * @return response 对象
	 */
	public static HttpServletResponse getHttpServletResponse() {
		HttpServletResponse resp = threadLocalResponse.get();
		if (resp == null)
			throw new RuntimeException("响应对象未初始化");

		return resp;
	}

	/**
	 * 清空 request 和 response
	 */
	public static void clean() {
		threadLocalRequest.set(null);
		threadLocalResponse.set(null);
	}

}
