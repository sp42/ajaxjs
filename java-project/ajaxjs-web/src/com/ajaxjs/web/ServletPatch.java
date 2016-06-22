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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.util.FileUtil;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

/**
 * Servlet 的一些补丁，也有一些静态工具类
 * @author frank
 *
 */
public class ServletPatch {

	/**
	 * 遍历注解的配置，需要什么类，收集起来，放到一个 hash 之中， Servlet 或 Filter 通用
	 * 
	 * @param servletCfg
	 *            这两个参数任选一个，但不能同时传
	 * @param filterCfg
	 *            这两个参数任选一个，但不能同时传
	 * @return 指定的 Servlet 或 Filter 配置对象
	 */
	public static Map<String, String> parseInitParams(ServletConfig servletCfg, FilterConfig filterCfg ){
		
		/*
		 * 基于注解的配置方式
	@WebServlet(
		urlPatterns = {
			"/admin/entry/list/*",
		},
		initParams = {
			@WebInitParam (name = "news", 		value = "ajaxjsx.data.news.News"),
			@WebInitParam (name = "img", 		value = "ajaxjs.data.subObject.img.Img")
		}
	)
		 */
		Map<String, String> initParamsMap = new HashMap<>();
		
		Enumeration<String> initParams = servletCfg == null 
										? filterCfg.getInitParameterNames() 
										: servletCfg.getInitParameterNames();
		// Enumeration 转换为 MAP
		while (initParams.hasMoreElements()) {
			String initParamName 	= initParams.nextElement(),
				   initParamValue 	= servletCfg == null 
							? filterCfg.getInitParameter(initParamName) 
							: servletCfg.getInitParameter(initParamName);

//			System.out.println("initParamName：" + initParamName + ", initParamValue:" + initParamValue);
			initParamsMap.put(initParamName, initParamValue);
		}
		
		return initParamsMap;
	}
	
	public static void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response) {	
		// 为防止中文乱码，统一设置 UTF-8
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) { }
		response.setCharacterEncoding("UTF-8");
	}
	
	/**
	 * 获取 PUT 请求所提交的内容
	 * Servlet 不能获取 PUT 请求内容，所以这里写一个方法
	 * @param request
	 *            请求对象
	 * @return 参数、值集合
	 */
	public static Map<String, String> getPutRequest(HttpServletRequest request) {
		String params = null;

		try {
			params = FileUtil.readText(request.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return params != null ? getPutRequest(params) : null;
	}
	
	/**
	 * 
	 * @param str
	 *            提交的请求内容
	 * @return 参数、值集合
	 */
	public static Map<String, String> getPutRequest(String str) {
		str = StringUtil.urlDecode(str);

		Map<String, String> map = new HashMap<>();
		String[] arr = str.split("&"), pair;

		for (String item : arr) {
			pair = item.split("=");
			map.put(pair[0], pair[1]);
		}

		return map;
	}

	/**
	 * 通过正则获取值
	 * 
	 * @param url
	 *            原始的请求内容
	 * @param paramName
	 *            参数名
	 * @return 对应的参数值
	 */
	public static String getParameter(String url, String paramName) {
		// regex for "paramName='anything unless is &'"
		Matcher matcher = Pattern.compile(paramName + "=[^&]*").matcher(url);
		matcher.find();
		String value = matcher.group().split("=")[1];
		return value;
	}
	
	/**
	 * 获取某个 Cookie
	 * 
	 * @param cookies
	 *            由 request.getCookies(); 返回
	 * @param key
	 *            键
	 * @return
	 */
	public static String getCookie(Cookie[] cookies, String key) {
		if (Util.isNotNull(cookies)) {
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(key))
					return cookie.getValue();
		}

		return null;
	}
	
	/**
	 * 请求获取中文。只要过滤器设置了 utf-8 那么这里就不用重复转码了
	 * 
	 * @param value
	 *            参数
	 * @return 中文
	 */
	public static String toChinese(String value) {
		byte[] bytes = null;

		try {
			bytes = value.getBytes("ISO8859_1");
		} catch (UnsupportedEncodingException e) {
			return null;
		}

		return bytes != null ? StringUtil.byte2String(bytes) : null;
	}
}
