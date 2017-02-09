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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletRequest;
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
	 * 创建一个 Requester 对象
	 * 
	 * @param request
	 *            原生的 request 对象
	 */
	public Requester(HttpServletRequest request) {
		super(request);
		
		try {
			setCharacterEncoding(StandardCharsets.UTF_8.toString());// 为防止中文乱码，统一设置 UTF-8，设置请求编码方式
		} catch (UnsupportedEncodingException e) {} 
	}
	
	/**
	 * 创建一个 Requester 对象
	 * 
	 * @param request
	 *            ServletRequest 对象
	 */
	public Requester(ServletRequest request) {
		this((HttpServletRequest)request);
	}

	/**
	 * 获取原请求的 uri，而非模版所在的 uri
	 */
	@Override
	public String getRequestURI() {
		Object obj = getAttribute("javax.servlet.forward.request_uri");
	
		if (obj != null && !StringUtil.isEmptyString((String) obj)) {
			return (String) obj;
		} else {
			return super.getRequestURI();// 直接 jsp 的
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Deprecated
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
		String absolute = getServletContext().getRealPath(relativePath); // 绝对地址
		
		if (absolute != null)
			absolute = absolute.replace('\\', '/');
		return absolute;
	}
	
/**
 * 网站域名=协议+主机名+端口（如果为 80 端口的话就默认不写 80）
 * 
 * @return 网站域名
 */
public String getServerPath() {
	String prefix = getScheme() + "://" + getServerName();

	int port = getServerPort();
	if (port != 80)
		prefix += ":" + port;

	return prefix;
}
	
/**
 * 网站域名+项目目录
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
	
	/**
	 * 获取请求的 IP 地址
	
	 * @return IP 地址
	 */
	public String getIP() {
		String s = getHeader("X-Forwarded-For"), unknown = "unknown";
		if (StringUtil.isEmptyString(s) || unknown.equalsIgnoreCase(s))
			s = getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtil.isEmptyString(s) || unknown.equalsIgnoreCase(s))
			s = getRemoteAddr();
	
		return s;
	}

}
