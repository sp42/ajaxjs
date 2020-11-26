/**
 * Copyright sp42 frank@ajaxjs.com
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
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.net.http.Tools;
import com.ajaxjs.util.cache.LRUCache;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * Servlet 辅助工具类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ServletHelper {
	/**
	 * 获取所有 URL 上的参数
	 * 
	 * @param r       请求对象
	 * @param without 不需要的字段
	 * @return URL 查询参数结对的字符串
	 */
	public static String getAllQueryParameters(HttpServletRequest r, String without) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> parameterList = r.getParameterNames();
		int i = 0;

		while (parameterList.hasMoreElements()) {
			String name = parameterList.nextElement().toString();
			if (name.equals(without)) // 排除指定的
				continue;

			if (i++ > 0)
				sb.append("&");
			sb.append(name + "=" + r.getParameter(name));
		}

		return sb.toString();
	}

	public static String getAllQueryParameters(HttpServletRequest r) {
		return getAllQueryParameters(r, null);
	}

	/**
	 * 简单检查字符串有否 SQL 关键字
	 * 
	 * @param str
	 * @return true 表示为字符串中有 SQL 关键字
	 */
	public static boolean preventSQLInject(String str) {
		str = str.toUpperCase();

		if (str.indexOf("DELETE") >= 0 || str.indexOf("ASCII") >= 0 || str.indexOf("UPDATE") >= 0 || str.indexOf("SELECT") >= 0 || str.indexOf("'") >= 0
				|| str.indexOf("SUBSTR(") >= 0 || str.indexOf("COUNT(") >= 0 || str.indexOf(" OR ") >= 0 || str.indexOf(" AND ") >= 0 || str.indexOf("DROP") >= 0
				|| str.indexOf("EXECUTE") >= 0 || str.indexOf("EXEC") >= 0 || str.indexOf("TRUNCATE") >= 0 || str.indexOf("INTO") >= 0 || str.indexOf("DECLARE") >= 0
				|| str.indexOf("MASTER") >= 0) {

			return false;
		}

		return true;
	}

	/**
	 * 转义 MySql 语句中使用的字符串中的特殊字符
	 * 
	 * @param str SQL
	 * @return 转换后的字符串
	 */
	public static String MysqlRealScapeString(String str) {
		str = str.replace("\\", "\\\\");
		str = str.replace("'", "\\'");
		str = str.replace("\0", "\\0");
		str = str.replace("\n", "\\n");
		str = str.replace("\r", "\\r");
		str = str.replace("\"", "\\\"");
		str = str.replace("\\x1a", "\\Z");

		return str;
	}

	// --------------IP 拦截----------------

	static LRUCache<String, Boolean> cache = new LRUCache<>(20);

	public static boolean isChinaMainlandIp(String ip) {
		try {
			Map<String, Object> map = Tools.getIpLocation(ip);
			Object c = map.get("country");

			if (c != null && "中国".equals(c.toString())) {
				Object r = map.get("region");

				if (r != null && ("香港".equals(r.toString()) || "澳门".equals(r.toString()) || "台湾".equals(r.toString()))) {
					return false;
				}

				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static boolean isChinaMainlandIp_Cache(String ip) {
		Boolean isChinaMainlandIp = cache.get(ip);

		if (isChinaMainlandIp == null) {
			isChinaMainlandIp = isChinaMainlandIp(ip);
			cache.put(ip, isChinaMainlandIp);
		}

		return isChinaMainlandIp;
	}

	public static boolean isChinaMainlandIp_Cache(HttpServletRequest req) {
		String ip = req instanceof MvcRequest ? ((MvcRequest) req).getIp() : new MvcRequest(req).getIp();
		return isChinaMainlandIp_Cache(ip);
	}
}
