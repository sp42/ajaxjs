package com.ajaxjs.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.util.MapData;

/**
 * 请求的数据
 * 
 * @author frank
 *
 */
public class RequestData extends MapData {
	public RequestData(HttpServletRequest request) {
		setParameterMapRaw(request.getParameterMap()); 	  // 一般没什么用
		setParameterMap_String(getParameterMap(request)); // 一般调用这个，导出 Object
	}

	/**
	 * 获取客户端请求的参数和值 代替原生 getParameterMap() getParameterMap() 返回 <String, String[]>，使用比较繁琐
	 * 
	 * @return 包含 Request 所有键值的 Map
	 */
	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		Map<String, String> data = new HashMap<>();
		String key;
		Enumeration<String> enu = request.getParameterNames();

		while (enu.hasMoreElements()) {
			key = enu.nextElement();
			data.put(key, request.getParameter(key));
		}

		return data;
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
		
		return matcher.group().split("=")[1];
	}

	/**
	 * 跳转的异常页面
	 * @param request
	 * @param response
	 * @param ex 异常对象
	 */
	public static void gotoErrorPage(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		/*
		 * 不能传这个 jspException 否则不能跳转
		 */
		// request.setAttribute("javax.servlet.jsp.jspException", ex); 
		request.setAttribute("javax.servlet.error.exception_type", ex);
		request.setAttribute("javax.servlet.error.message", ex.getMessage());
		
		new Output(response).setRedirect("/public/error.jsp").go(request);
	}
}
