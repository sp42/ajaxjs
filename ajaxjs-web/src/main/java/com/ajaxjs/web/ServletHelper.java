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
	 * @param getInitParameterNames
	 * @param getValue
	 * @return
	 */
	private static Map<String, String> initParams2map(Supplier<Enumeration<String>> getInitParameterNames,
			Function<String, String> getValue) {
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
	 * 
	 * @param config
	 * @return
	 */
	public static Map<String, String> initFilterConfig2Map(FilterConfig config) {
		return initParams2map(() -> config.getInitParameterNames(), key -> config.getInitParameter(key));
	}

	/**
	 * 
	 * @param config
	 * @return
	 */
	public static Map<String, String> initServletConfig2Map(ServletConfig config) {
		return initParams2map(() -> config.getInitParameterNames(), key -> config.getInitParameter(key));
	}
}
