package com.ajaxjs.mvc.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.collection.Map2Pojo;
import com.ajaxjs.util.collection.MapHelper;
import com.ajaxjs.util.io.StreamUtil;

/**
 * 
 * @author frank
 *
 */
public class MvcRequest extends HttpServletRequestWrapper {
	public MvcRequest(HttpServletRequest request) {
		super(request);
		
		try {
			setCharacterEncoding(StandardCharsets.UTF_8.toString());// 为防止中文乱码，统一设置 UTF-8，设置请求编码方式
		} catch (UnsupportedEncodingException e) {} 
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
	 * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源（最原始的版本）
	 * @return
	 */
	public String getRoute() {
		String route = getRequestURI().replace(getContextPath(), "");
		
		return route.replaceFirst("/\\w+\\.\\w+$", ""); // 删除 index.jsp
	}
	
	/**
	 * 获取 PUT 请求所提交的内容。 Servlet 不能获取 PUT 请求内容，所以这里写一个方法
	 * 
	 * @return 参数、值集合
	 */
	public Map<String, Object> getPutRequestData() {
		String params = null;
		
		try {
			params = new StreamUtil().setIn(getInputStream()).byteStream2stringStream().close().getContent();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (params == null)
			return null;
		
		return MapHelper.toMap(params.split("&"), true);
	}
	
	private static String matchList(String regexp, String str) {
		Matcher m = Pattern.compile(regexp).matcher(str);

		return m.find() ? m.group(m.groupCount()) : null;
	}

	/**
	 * 取去 url 上的值
	 * 
	 * @param value
	 * @param paramName
	 * @return
	 */
	public String getValueFromPath(String value, String paramName) {
//		System.out.println("::"+value);
//		System.out.println(paramName);
		
		/* 如果 context path 上有数字那就bug，所以先去掉 */
		String requestURI = getRequestURI().replace(getContextPath(), ""), regExp = "(" + value.replace("{" + paramName + "}", ")(\\d+)");/* 获取正则 暂时写死 数字 TODO */
		
//		System.out.println(requestURI);
//		System.out.println(regExp);
		
		String result = matchList(regExp, requestURI);
//
//		System.out.println(regExp);
//		System.out.println(result);
		
		if (result == null)
			throw new IllegalArgumentException("在 " + requestURI + "不能获取 " + paramName + "参数");
		return result;
	}

	/**
	 * 支持自动获取请求参数并封装到 bean 内
	 * @param clazz
	 * @return
	 */
	public Object getBean(Class<?> clazz) {
		// Object bean = Reflect.newInstance(clazz);
		// Map2Pojo<?> m = new Map2Pojo<>(clazz); // 这里怎么 不用 ?？

		Map<String, Object> map;
		
		if (getMethod().toUpperCase().equals("PUT")) {
			map = getPutRequestData(); // Servlet 没有 PUT 获取表单，要自己处理
		} else {
			map = MapHelper.asObject(MapHelper.toMap(getParameterMap()), true);
		}

		return new Map2Pojo<>(clazz).map2pojo(map);
	}
	
	/**
	 * 遍历注解的配置，需要什么类，收集起来，放到一个 hash 之中， Servlet 或 Filter 通用
	 * 
	 * @param servletCfg
	 *            这两个参数任选一个，但不能同时传
	 * @param filterCfg
	 *            这两个参数任选一个，但不能同时传
	 * @return 指定的 Servlet 或 Filter 配置对象
	 */
	public static Map<String, String> parseInitParams(ServletConfig servletCfg, FilterConfig filterCfg) {
		Map<String, String> initParamsMap = new HashMap<>();
		
		Enumeration<String> initParams = servletCfg == null ? filterCfg.getInitParameterNames() : servletCfg.getInitParameterNames();
		
		while (initParams.hasMoreElements()) {	// Enumeration 转换为 MAP
			String initParamName  = initParams.nextElement(),
				   initParamValue = servletCfg == null ? filterCfg.getInitParameter(initParamName) : servletCfg.getInitParameter(initParamName);
	
			initParamsMap.put(initParamName, initParamValue);
		}
		
		return initParamsMap;
	}

	/**
	 * 全局的 callback 参数名
	 */
	public static final String callback_param = "callback";

	/**
	 * 保存到 request
	 * 
	 * @param map
	 *            请求参数
	 */
	public void saveToReuqest(Map<String, Object> map) {
		for (String key : map.keySet()) {
			setAttribute(key, map.get(key));
		}
	}
	
	/**
	 * 获取请求 ip
	 * @param request
	 * @return 客户端 ip
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		
		if (!"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个 ip 值，第一个 ip 才是真实 ip
			int index = ip.indexOf(",");

			if (index != -1) 
				ip = ip.substring(0, index);
		}

		ip = request.getHeader("X-Real-IP");
		if (!"unKnown".equalsIgnoreCase(ip))
			return ip;
		
		ip = request.getRemoteAddr();

		return ip;
	}
}
