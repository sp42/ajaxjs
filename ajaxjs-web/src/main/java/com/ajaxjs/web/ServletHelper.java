/**
 * Copyright Sp42 frank@ajaxjs.com
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet 辅助工具类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ServletHelper {

	/**
	 * 遍历注解的配置，需要什么类，收集起来，放到一个 hash 之中， Servlet 或 Filter 通用
	 * 
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

	private static final Pattern p = 
	 Pattern.compile("\\.jpg|\\.png|\\.gif|\\.js|\\.css|\\.less|\\.ico|\\.jpeg|\\.htm|\\.swf|\\.txt|\\.mp4|\\.flv");

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
	 * @param r 请求对象
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
}
