package com.ajaxjs.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;

public class ServletHelper {

	/**
	 * 遍历注解的配置，需要什么类，收集起来，放到一个 hash 之中， Servlet 或 Filter 通用
	 * 
	 * @param getInitParameterNames
	 * @param getValue
	 * @return
	 */
	private static Map<String, String> initParams2map(Supplier<Enumeration<String>> getInitParameterNames, Function<String, String> getValue) {
		Map<String, String> map = new HashMap<>();

		Enumeration<String> initParams = getInitParameterNames.get();

		while (initParams.hasMoreElements()) {
			String key = initParams.nextElement();
			String value = getValue.apply(key);

			map.put(key, value);
		}

		return map;
	}

	/**
	 * 将过滤器的配置转换为 Map
	 * 
	 * @param config 过滤器配置
	 * @return 过滤器配置的 Map 结构
	 */
	public static Map<String, String> initFilterConfig2Map(FilterConfig config) {
		return initParams2map(() -> config.getInitParameterNames(), key -> config.getInitParameter(key));
	}

	/**
	 * 将 Servlet 的配置转换为 Map
	 * 
	 * @param config Servlet 配置
	 * @return Servlet 配置的 Map 结构
	 */
	public static Map<String, String> initServletConfig2Map(ServletConfig config) {
		return initParams2map(() -> config.getInitParameterNames(), key -> config.getInitParameter(key));
	}
}
