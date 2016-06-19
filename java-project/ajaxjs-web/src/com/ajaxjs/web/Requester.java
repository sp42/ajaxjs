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

package com.ajaxjs.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.StringUtil;

/**
 * 
 * 建立一个请求包装器
 * @author frank
 *
 */
public class Requester extends HttpServletRequestWrapper {
	/**
	 * 创建一个 RequestHelper 对象
	 * 
	 * @param request
	 *            原生的 request 对象
	 */
	public Requester(HttpServletRequest request) {
		super(request);
	}
	
	/**
	 * 获取请求参数的内容。 防止空参数处理
	 * 
	 * @param key
	 *            键
	 * @return 参数值
	 */
	@Override
	public String getParameter(String key){
		String value = super.getParameter(key);

		if (value == null)
			throw new NullPointerException("你未传入参数：" + key);
		if (StringUtil.isEmptyString(value))
			throw new IllegalArgumentException(String.format("你有传入 %s 参数，但是该参数为空字符串！", key));
		
		return value;
	}

	/**
	 * 获取原请求的 uri，而非模版所在的 uri
	 */
	@Override
	public String getRequestURI() {
		if (getAttribute("javax.servlet.forward.request_uri") != null) {
			String uri = (String) getAttribute("javax.servlet.forward.request_uri");
			if (StringUtil.isEmptyString(uri)) {
				uri = super.getRequestURI(); // 直接 jsp 的
			}
			return uri;
		} else {
			return super.getRequestURI();// 直接 jsp 的
		}
	}
	
	/**
	 * 是否有某个参数？
	 * 
	 * @param key
	 *            健
	 * @return true 表示有这个参数并且带值得
	 */
	public boolean hasValue(String key) {
		return !StringUtil.isEmptyString(getParameter(key));
	}
	
	/**
	 * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源（最原始的版本）
	 * @return
	 */
	public String getRoute() {
		String route = getRequestURI().replace(getContextPath(), "");
		
		return route.replaceFirst("/\\w+\\.\\w+$", ""); // 删除 index.jsp
	}

	/**
	 * @deprecated
	 * @return
	 */
	public String getLastRoute() {
		String route = getRequestURI().replace(getContextPath(), "");
		String [] arr = route.split("/");
		
		return arr != null && arr.length > 0 ? arr[arr.length - 1] : null;
	}
	
	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public String Mappath(String relativePath) {
		String absoluteAddress = getServletContext().getRealPath(relativePath); // 绝对地址
		
		if (absoluteAddress != null)
			absoluteAddress = absoluteAddress.replace('\\', '/');
		return absoluteAddress;
	}
	
	/**
	 * 协议+主机名+端口（如果为 80 端口的话就默认不写 80）
	 * 
	 * @return 网站名称
	 */
	public String getServerPath() {
		String prefix = getScheme() + "://" + getServerName();

		int port = getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix;
	}
	
	/**
	 * 网站名称+项目目录
	 * 
	 * @return 比较完整的 url 但不是最完整的，最完整的见下个方法
	 */
	public String getBasePath() {
		return getServerPath() + getContextPath();
	}
	
	/**
	 * 获取本页 URL，有时可能要对 url redirectUri = java.net.URLEncoder.encode(redirectUri, "utf-8");
	 * 
	 * @param isFull
	 *            是否把 QueryString 参数都带进去？
	 * @return 本页地址
	 */
	public String getCurrentPageUrl(boolean isFull) {
		String url = getServerPath() + getRequestURI();
		if (isFull)
			url += '?' + getQueryString();
		return url;
	}
	
	private List<String> logger = new ArrayList<>();
	
	private List<Exception> errLogger = new ArrayList<>();
	
	/**
	 * 保存日志
	 * 
	 * @param msg
	 *            日志信息
	 */
	public void pushLog(String msg) {
		logger.add(msg);
	}

	/**
	 * 保存异常
	 * 
	 * @param msg
	 *            异常信息，并转变为异常对象
	 */
	public void pushErr(String msg) {
		errLogger.add(new Exception(msg));
	}

	/**
	 * 保存异常
	 * 
	 * @param e
	 *            异常
	 */
	public void pushErr(Exception e) {
		errLogger.add(e);
	}

	/**
	 * 是否有异常内容
	 * 
	 * @return true 表示为有异常内存
	 */
	public boolean hasError() {
		return errLogger.size() > 0;
	}
	
	/**
	 * 合并异常信息输出
	 * 
	 * @return 异常信息
	 */
	public String getError() {
		if (hasError()) {
			StringBuilder sb = new StringBuilder();

			for (Exception err : errLogger)
				sb.append(err.getMessage());

			return sb.toString();
		} else
			return null;
	}
	
	/**
	 * 获取请求的 IP 地址

	 * @return IP 地址
	 */
	public String getIP() {
		String s = getHeader("X-Forwarded-For");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = getHeader("Proxy-Client-IP");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = getHeader("WL-Proxy-Client-IP");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = getHeader("HTTP_CLIENT_IP");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtil.isEmptyString(s) || "unknown".equalsIgnoreCase(s))
			s = getRemoteAddr(); // REMOTE_ADDR

		return s;
	}
	
	/**
	 * 设置信息
	 * 
	 * @param msg
	 *            信息
	 */
	public void setActionMsg(String msg) {
		setAttribute("msg", msg);
	}
}
