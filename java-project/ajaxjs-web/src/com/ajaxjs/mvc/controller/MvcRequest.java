package com.ajaxjs.mvc.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;

import com.ajaxjs.framework.model.Map2Pojo;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.map.MapHelper;
import com.ajaxjs.web.Requester;

/**
 * 
 * @author frank
 *
 */
public class MvcRequest extends Requester{
	public MvcRequest(ServletRequest request) {
		super(request);
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

		params = StringUtil.urlDecode(params);
		return MapHelper.toMap(params.split("&"));
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
		String requestURI = getRequestURI(), regExp = "(" + value.replace("{" + paramName + "}", ")(\\d+)");/* 获取正则 暂时写死 数字 TODO */
		String result = matchList(regExp, requestURI);

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
}
