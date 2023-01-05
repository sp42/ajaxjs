package com.ajaxjs.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

/**
 * Web 工具类
 *
 * @author Frank Cheung
 */
public class WebHelper2 {
	public static String uriDecode(String str) {
		return UriUtils.decode(str, "utf-8");
	}

	/**
	 * 获取所有 QueryString 参数，并转化为真实的值，最后返回一个 Map
	 *
	 * @param request 请求对象
	 * @return 所有 QueryString 参数
	 */
	public static Map<String, Object> getQueryParameters(HttpServletRequest request) {
		String queryString = request.getQueryString();

		if (!StringUtils.hasText(queryString))
			return null;

		String[] parameters = queryString.split("&");
		Map<String, Object> map = new HashMap<>();

		for (String parameter : parameters) {
			String[] keyValuePair = parameter.split("=");
			String v = keyValuePair[1];
			v = uriDecode(v);

			map.put(keyValuePair[0], keyValuePair.length == 1 ? "" : MappingValue.toJavaValue(v));
		}

		return map;
	}
}
