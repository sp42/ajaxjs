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
package com.ajaxjs.web.view;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.map.MapTool;

/**
 * 分页标签
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class PageTag {
	/**
	 * 对某段 URL 参数剔除其中的一个。
	 * 
	 * @param withoutParam 不需要的那个参数
	 * @param queryString  通常由 request.getQueryString() 或
	 *                     ${pageContext.request.queryString} 返回的 url 参数
	 * @return 特定的 url 参数
	 */
	public static String getParams_without(String withoutParam, String queryString) {
		if (queryString == null)
			return null;

		queryString = queryString.replaceAll("&?" + withoutParam + "=[^&]*", "");// 删除其中一个参数

		if (!StringUtils.hasText(queryString))
			return null;

		return queryString.startsWith("&") ? queryString : "&" + queryString; // 补充关联的符号

	}

	/**
	 * 保持状态，对当前的参数记录，不受分页的影响.对某段 URL 参数剔除其中的一个。但是返回 map。
	 * 
	 * @param withoutParam 不需要的那个参数
	 * @param queryString  通常由 request.getQueryString() 或
	 *                     ${pageContext.request.queryString} 返回的 url 参数
	 * @return 已处理过的 Map
	 */
	public static Map<String, Object> getParams_without_asMap(String withoutParam, String queryString) {
		queryString = getParams_without(withoutParam, queryString);

		if (!StringUtils.hasText(queryString))
			return null;

		// if(queryString.startsWith("&"))
		queryString = queryString.replaceFirst("&", "");// 去掉第一个无用的 &

		return MapTool.toMap(queryString.split("&"), v -> v);
	}

	public int[] jumpPage(int totalPage) {
		return new int[totalPage];
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public static Object[] page2start(Object[] args) {
		int pageStart = (int) args[0];
		int pageSize = (int) args[1];

		if (pageSize == 0)
			pageSize = 8;

		int start = 0;
		if (pageStart >= 1)
			start = (pageStart - 1) * pageSize;

		args[0] = start;

		return args;
	}

	static String a = "<a href=\"?start=%s\">%s</a>";

	public static String rendererBtns(PageResult<?> result) {
		String[] html = new String[2];

		if (result.getStart() > 0)
			html[0] = String.format(a, result.getStart() - result.getPageSize(), "上一页");

		if (result.getStart() + result.getPageSize() < result.getTotalCount())
			html[1] = String.format(a, result.getStart() + result.getPageSize(), "下一页");

		boolean is0 = StringUtils.hasText(html[0]), is1 = StringUtils.hasText(html[1]);

		if (is0 && is1)
			return String.join("&nbsp;&nbsp; | &nbsp;&nbsp;", html);

		if (is0)
			return html[0];

		if (is1)
			return html[1];

		return "ERROR";
	}
}