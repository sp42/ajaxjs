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

import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.map.MapTool;

/**
 * Servlet 辅助工具类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ServletHelper {
	/**
	 * 将过滤器的配置转换为 Map
	 * 
	 * @param config 过滤器配置
	 * @return 过滤器配置的 Map 结构
	 */
	public static Map<String, String> initFilterConfig2Map(FilterConfig config) {
		return MapTool.emu2map(config.getInitParameterNames(), key -> config.getInitParameter(key));
	}

	/**
	 * 将 Servlet 的配置转换为 Map
	 * 
	 * @param config Servlet 配置
	 * @return Servlet 配置的 Map 结构
	 */
	public static Map<String, String> initServletConfig2Map(ServletConfig config) {
		return MapTool.emu2map(config.getInitParameterNames(), key -> config.getInitParameter(key));
	}

	private static final Pattern p = Pattern
			.compile("\\.jpg|\\.png|\\.gif|\\.js|\\.css|\\.less|\\.ico|\\.jpeg|\\.htm|\\.swf|\\.txt|\\.mp4|\\.flv");

	/**
	 * 检查是否静态资源。Check the url if there is static asset.
	 * 
	 * @param url URL 地址
	 * @return true 表示为静态资源
	 */
	public static boolean isStaticAsset(String url) {
		return p.matcher(url).find();
	}

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

		if (str.indexOf("DELETE") >= 0 || str.indexOf("ASCII") >= 0 || str.indexOf("UPDATE") >= 0
				|| str.indexOf("SELECT") >= 0 || str.indexOf("'") >= 0 || str.indexOf("SUBSTR(") >= 0
				|| str.indexOf("COUNT(") >= 0 || str.indexOf(" OR ") >= 0 || str.indexOf(" AND ") >= 0
				|| str.indexOf("DROP") >= 0 || str.indexOf("EXECUTE") >= 0 || str.indexOf("EXEC") >= 0
				|| str.indexOf("TRUNCATE") >= 0 || str.indexOf("INTO") >= 0 || str.indexOf("DECLARE") >= 0
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
}
