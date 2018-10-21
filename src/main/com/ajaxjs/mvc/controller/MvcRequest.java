/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.keyvalue.BeanUtil;
import com.ajaxjs.keyvalue.MapHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.StreamUtil;

/**
 * 通过 HttpServletRequestWrapper （装饰模式的应用）增强 HttpServletRequest的功能。
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class MvcRequest extends HttpServletRequestWrapper {
	/**
	 * 创建一个 MVC 请求对象。构造方法会自动加入 UTF-8 编码。
	 * 
	 * @param request
	 *            原始请求对象
	 */
	public MvcRequest(HttpServletRequest request) {
		super(request);

		try {
			setCharacterEncoding(StandardCharsets.UTF_8.toString());// 为防止中文乱码，统一设置 UTF-8，设置请求编码方式
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	/**
	 * 获取必填的参数
	 * @param name 參數名称
	 * @return 参数值
	 */
	public String getRequiredParameter(String name) {
		if(name == null)
			throw new NullPointerException("缺少参数名称");
		
		String value = getParameter(name);
		if (StringUtil.isEmptyString(value)) 
			throw new IllegalArgumentException("缺少参数！");
		
		return value;
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
	 * 
	 * @return 请求路径
	 */
	public String getRoute() {
		String route = getRequestURI().replace(getContextPath(), "");
		return route.replaceFirst("/\\w+\\.\\w+$", ""); // 删除 index.jsp
	}

	public String getFolder() {
		return getRequestURI().replace(getContextPath(), "").replaceFirst("^/", "").replaceFirst("/\\w+\\.\\w+$", ""); // 删除 index.jsp;
	}

	/**
	 * 获取 PUT 请求所提交的内容。 Servlet 不能获取 PUT 请求内容，所以这里写一个方法
	 * 
	 * @return 参数、值集合
	 */
	public Map<String, Object> getPutRequestData() {
		try {
			String params = new StreamUtil().setIn(getInputStream()).byteStream2stringStream().close().getContent();
			return MapHelper.toMap(params.split("&"), true);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取去 url 上的值
	 * 
	 * @param value
	 *            值
	 * @param paramName
	 *            参数名称
	 * @return 值
	 */
	public String getValueFromPath(String value, String paramName) {
		/* 如果 context path 上有数字那就bug，所以先去掉 */
		String requestURI = getRequestURI().replace(getContextPath(), ""), regExp = "(" + value.replace("{" + paramName + "}", ")(\\d+)");/* 获取正则 暂时写死 数字 TODO */

		Matcher m = Pattern.compile(regExp).matcher(requestURI);
		String result = m.find() ? m.group(m.groupCount()) : null;

		if (result == null)
			throw new IllegalArgumentException("在 " + requestURI + "不能获取 " + paramName + "参数");

		return result;
	}

	/**
	 * 支持自动获取请求参数并封装到 bean 内
	 * 
	 * @param clazz
	 *            Bean 的类引用
	 * @return Java Bean
	 */
	public <T> T getBean(Class<T> clazz) {
		Map<String, Object> map;

		if (getMethod() != null && getMethod().toUpperCase().equals("PUT")) {
			map = getPutRequestData(); // Servlet 没有 PUT 获取表单，要自己处理
		} else {
			map = MapHelper.asObject(MapHelper.toMap(getParameterMap()), true);
		}

		// 抛出 IllegalArgumentException 这个异常 有可能是参数类型不一致造成的，要求的是 string 因为 map 从 request 转换时已经变为 int（例如纯数字的时候）
		// 所以最后一个参数为 true
		return BeanUtil.map2Bean(map, clazz, true);
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
		Map<String, String> map = new HashMap<>();

		Enumeration<String> initParams = servletCfg == null ? filterCfg.getInitParameterNames() : servletCfg.getInitParameterNames();

		while (initParams.hasMoreElements()) {
			String key = initParams.nextElement();

			String value;
			if (servletCfg == null)
				value = filterCfg.getInitParameter(key);
			else
				value = servletCfg.getInitParameter(key);

			map.put(key, value);
		}

		return map;
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
		for (String key : map.keySet())
			setAttribute(key, map.get(key));
	}

	/**
	 * 创建该实例的工厂方法
	 * 
	 * @return QueryParams
	 */
	public static QueryParams factory() {
		return new QueryParams(getHttpServletRequest().getParameterMap());
	}

	/*
	 * 为获取请求的上下文，能够在控制器中拿到最常用的对象，例如 HttpServletRequest 和 HttpServletResponse 等的对象（甚至
	 * Web App 的启动上下文（ 在web.xml 中配置的参数） ），因此还需要设计一个 RequestHelper 类，通过
	 * ThreadLocal让控制器能轻易地访问到这些对象。 一个容器，向这个容器存储的对象，在当前线程范围内都可以取得出来，向 ThreadLocal
	 * 里面存东西就是向它里面的Map存东西的，然后 ThreadLocal 把这个 Map 挂到当前的线程底下，这样 Map 就只属于这个线程了
	 */
	private static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<>();

	/**
	 * 保存一个 request 对象
	 * 
	 * @param request
	 *            请求对象
	 */
	public static void setHttpServletRequest(HttpServletRequest request) {
		threadLocalRequest.set(request);
	}

	/**
	 * 获取请求对象
	 * 
	 * @return 请求对象
	 */
	public static HttpServletRequest getHttpServletRequest() {
		HttpServletRequest request = threadLocalRequest.get();
		if (request == null)
			throw new RuntimeException("请求对象未初始化");

		return request;
	}

	/**
	 * 获取请求对象
	 * 
	 * @return 请求对象
	 */
	public static MvcRequest getMvcRequest() {
		HttpServletRequest request = getHttpServletRequest();
		if (!(request instanceof MvcRequest))
			throw new RuntimeException("非法 MvcRequest 类型");

		return (MvcRequest) request;
	}

	/**
	 * 保存一个 response 对象
	 * 
	 * @param response
	 *            响应对象
	 */
	public static void setHttpServletResponse(HttpServletResponse response) {
		threadLocalResponse.set(response);
	}

	/**
	 * 获取上下文中 response 对象
	 * 
	 * @return response 响应对象
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

	/**
	 * 获取磁盘真實地址。
	 * 
	 * @param cxt
	 *            Web 上下文
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public static String mappath(ServletContext cxt, String relativePath) {
		String absolute = cxt.getRealPath(relativePath);

		if (absolute != null)
			absolute = absolute.replace('\\', '/');
		return absolute;
	}

	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public String mappath(String relativePath) {
		return mappath(getServletContext(), relativePath);
	}

	/**
	 * 返回协议+主机名+端口+项目前缀（如果为 80 端口的话就默认不写 80）
	 * 
	 * @return 网站名称
	 */
	public String getBasePath() {
		String prefix = getScheme() + "://" + getServerName();

		int port = getServerPort();
		if (port != 80)
			prefix += ":" + port;

		return prefix + "/" + getContextPath();
	}

	/**
	 * 获取请求 ip
	 * 
	 * @param request
	 *            请求对象
	 * @return 客户端 ip
	 */
	public String getIp() {
		String ip = getHeader("x-forwarded-for");

		if (!"unknown".equalsIgnoreCase(ip) && ip != null && ip.length() != 0) {
			int index = ip.indexOf(",");
			if (index != -1)
				ip = ip.substring(0, index);

			return ip;
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getHeader("Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getHeader("WL-Proxy-Client-IP");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getHeader("X-Real-Ip");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = getRemoteAddr();

		return ip;
	}
}
