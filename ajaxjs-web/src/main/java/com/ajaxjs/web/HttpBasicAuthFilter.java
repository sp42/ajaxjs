/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.util.Encode;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 简单的 HTTP Basic 登录
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class HttpBasicAuthFilter implements Filter {
	private static final LogHelper LOGGER = LogHelper.getLog(HttpBasicAuthFilter.class);

	/**
	 * 登录名，写死只有一个用户 admin
	 */
	private static final String userid = "admin";

	/**
	 * 登录密码
	 */
	private static String pwd = "123123";

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.info("启动 HTTP BasicAuth 后台管理");

		if (config.getInitParameter("adminPassword") != null)
			pwd = config.getInitParameter("adminPassword");// 读取 web.xml 配置里的密码
	}

	@Override
	@SuppressWarnings("deprecation")
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) _request;
		HttpServletResponse response = (HttpServletResponse) _response;

		if (!checkAuth(request)) {
			String msg = "\"Please input your account\""; // 如果认证失败,则要求认证 ，不能输入中文

			response.setCharacterEncoding("utf-8");
			response.setStatus(401, "Authentication Required");// 发送状态码 401, 不能使用 sendError，坑
			response.setHeader("WWW-Authenticate", "Basic realm=" + msg);// 发送要求输入认证信息,则浏览器会弹出输入框
			response.setCharacterEncoding("utf-8");
			response.getWriter().append("<meta charset=\"utf-8\" />Please login! 请登录系统！");

			LOGGER.info("HTTP BasicAuth 登录失败！");
		} else {
			//			request.setAttribute("userName", userid);
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

	/**
	 * 检查是否合法登录
	 * 
	 * @param request
	 *            请求对象
	 * @return 是否合法登录
	 */
	private static boolean checkAuth(HttpServletRequest request) {
		return checkAuth(request.getHeader("Authorization"), userid, pwd);
	}

	/**
	 * 是否不合法的数组
	 * 
	 * @param arr
	 * @return 是否不合法的数组
	 */
	private static boolean isBadArray(String[] arr) {
		return arr == null || arr.length != 2;
	}

	/**
	 * 检查是否合法登录
	 * 
	 * @param authorization
	 *            认证后每次HTTP请求都会附带上 Authorization 头信息
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return true = 认证成功/ false = 需要认证
	 */
	private static boolean checkAuth(String authorization, String username, String password) {
		if (CommonUtil.isEmptyString(authorization))
			return false;

		String[] basicArray = authorization.split("\\s+");
		if (isBadArray(basicArray))
			return false;

		String idpass = Encode.base64Decode(basicArray[1]);
		if (CommonUtil.isEmptyString(idpass))
			return false;

		String[] idpassArray = idpass.split(":");
		if (isBadArray(idpassArray))
			return false;

		return username.equalsIgnoreCase(idpassArray[0]) && password.equalsIgnoreCase(idpassArray[1]);
	}
}
