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
package com.ajaxjs.web.security;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.web.security.filter.EncrySessionInCookie;
import com.ajaxjs.web.security.wrapper.CLRF_Response;
import com.ajaxjs.web.security.wrapper.CookieRequest;
import com.ajaxjs.web.security.wrapper.CookieResponse;
import com.ajaxjs.web.security.wrapper.UploadRequest;
import com.ajaxjs.web.security.wrapper.XSS_Request;
import com.ajaxjs.web.security.wrapper.XSS_Response;

/**
 * 读取配置，保存到 JVM 内存中（保存容器为静态 List）
 * 
 * @author Frank
 *
 */
public class ConfigLoader implements Filter {
	private static boolean isEnableXSSFilter;
	private static boolean isEnableCLRF_Filter;
	private static boolean isEnableUploadFilter;

	/**
	 * 读取配置，保存到 JVM 内存中
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		System.out.println("安全框架启动中……");

		String encryCookieKey = config.getInitParameter("encryCookieKey");
		if (encryCookieKey != null) {
			if (encryCookieKey == null || encryCookieKey.length() != 16)
				throw new ServletException("encrykey(" + encryCookieKey + ") 长度必须为 16");
			
			EncrySessionInCookie.key = encryCookieKey;
		}

		String cookieWhiteList = config.getInitParameter("cookieWhiteList");
		if (cookieWhiteList != null) {
			CookieRequest.delegate.whiteList = loadParams(cookieWhiteList);
			CookieResponse.delegate.whiteList = CookieRequest.delegate.whiteList;
		}
		
		String enableXSSFilter = config.getInitParameter("enableXSSFilter");
		if (cookieWhiteList != null && "true".equals(enableXSSFilter)) {
			isEnableXSSFilter = true;
		}
		
		String enableCLRF_Filter = config.getInitParameter("enableCLRF_Filter");
		if (enableCLRF_Filter != null && "true".equals(enableCLRF_Filter)) {
			isEnableCLRF_Filter = true;
		}
		
		String uploadfileWhiteList = config.getInitParameter("uploadfileWhiteList");
		if (uploadfileWhiteList != null) {
			UploadRequest.delegate.whiteList = loadParams(uploadfileWhiteList);
		}
		
		String CSRF_Filter = config.getInitParameter("CSRF_Filter");
		if (CSRF_Filter != null && "true".equals(CSRF_Filter)) {
			
		}
	}
	

	private static boolean checkRedirectValid(String location) {
		if (location == null || location.isEmpty())
			return false;

		for (Pattern pattern : ConfigLoader.redirectLocationWhiteList) {
			if (pattern.matcher(location).find())
				return true;
		}

		return false;
	}

	public String getCookieByName(String name, Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		if (CookieRequest.delegate.whiteList != null) {
			CookieRequest cookieRequest = new CookieRequest(httpRequest);
			httpRequest = (HttpServletRequest) cookieRequest;
			
			CookieResponse cookieResponse = new CookieResponse(httpResponse);
			httpResponse = (HttpServletResponse)cookieResponse;
		}

		if(isEnableXSSFilter) {
			XSS_Request xssRequest = new XSS_Request(httpRequest);
			httpRequest = (HttpServletRequest) xssRequest;
			
			XSS_Response xssResponse = new XSS_Response(httpResponse);
			httpResponse = (HttpServletResponse)xssResponse;
		}
		
		if(isEnableCLRF_Filter) {
			CLRF_Response CLRF_response = new CLRF_Response(httpResponse);
			httpResponse = (HttpServletResponse)CLRF_response;
		}

		if(isEnableUploadFilter) {
			UploadRequest uploadRequest = new UploadRequest(httpRequest);
			httpRequest = (HttpServletRequest)uploadRequest;
		}
		
		if (EncrySessionInCookie.key != null) {
			EncrySessionInCookie.deseriable(httpRequest, EncrySessionInCookie.key);
		}
 
		filterChain.doFilter(httpRequest, new CLRF_Response(httpResponse));

		if (EncrySessionInCookie.key != null) {
			EncrySessionInCookie.seriable(httpRequest, httpResponse, EncrySessionInCookie.key);
		}
	}

	private final static String URL_SPLIT_PATTERN = "[, ;\r\n]"; // 逗号  空格 分号 换行
	
	/**
	 * 字符转换为 LIST
	 * 
	 * @param str
	 * @return
	 */
	private static List<String> loadParams(String str) {
		String[] arr = str.split(URL_SPLIT_PATTERN);
		return Arrays.asList(arr);
	}

	// public void initRedictWhiteList(FilterConfig filterConfig) throws
	// ServletException {
	// String list = filterConfig.getInitParameter("redirectWhiteList");
	// if (list == null || list.isEmpty()) {
	// return;
	// }
	// String[] redirectWhiteList = list.split(",");
	// List<Pattern> patterns = new ArrayList<>();
	// for (String str : redirectWhiteList) {
	// patterns.add(Pattern.compile(str));
	// }
	// SecurityConstant.redirectLocationWhiteList.addAll(patterns);
	// }

	@Override
	public void destroy() {
	}

}
