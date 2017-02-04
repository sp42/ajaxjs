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
 */package com.ajaxjs.web.security;

import java.io.IOException;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.web.security.wrapper.CLRF_Response;
import com.ajaxjs.web.security.wrapper.XSS_Request;

/**
 * 读取配置，保存到 JVM 内存中（保存容器为静态 List）
 * @author Frank
 *
 */
public class ConfigLoader implements Filter {
	public static final List<Pattern> redirectLocationWhiteList = new ArrayList<>();
	public static final List<String> whitefilePostFixList = new ArrayList<>();
	public static final List<String> onlyPostUrlList = new ArrayList<>();
	
	/**
	 * 白名单
	 */
	public static final List<String> cookieWhiteList = new ArrayList<>();
	
	/**
	 * 程序入口。启动过滤器，加载配置
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		loadParams(filterConfig, "cookieWhiteList", 	 cookieWhiteList);
		loadParams(filterConfig, "whitefilePostFixList", whitefilePostFixList);
		loadParams(filterConfig, "onlyPostUrlList", 	 onlyPostUrlList);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		XSS_Request security =  new XSS_Request(httpRequest);
		try {
		} catch (SecurityException e) {
			return;
		}
		
		filterChain.doFilter(security, new CLRF_Response(httpResponse));
	}

	/**
	 * 读取配置参数
	 * 
	 * @param filterConfig
	 *            过滤器全部配置，可以从 XML 配置文件中获得
	 * @param configName
	 *            指定哪个的配置
	 * @param list
	 *            配置值列表
	 */
	private static void loadParams(FilterConfig filterConfig, String configName, List<String> list) {
		String paramList = filterConfig.getInitParameter(configName);

		if (paramList != null) { // 字符转换为 LIST
			String[] arr = paramList.split(",");
			list.addAll(Arrays.asList(arr));
		}
	}

//	public void initRedictWhiteList(FilterConfig filterConfig) throws ServletException {
//		String list = filterConfig.getInitParameter("redirectWhiteList");
//		if (list == null || list.isEmpty()) {
//			return;
//		}
//		String[] redirectWhiteList = list.split(",");
//		List<Pattern> patterns = new ArrayList<>();
//		for (String str : redirectWhiteList) {
//			patterns.add(Pattern.compile(str));
//		}
//		SecurityConstant.redirectLocationWhiteList.addAll(patterns);
//	}
	 
	

	@Override
	public void destroy() {}
	
}
