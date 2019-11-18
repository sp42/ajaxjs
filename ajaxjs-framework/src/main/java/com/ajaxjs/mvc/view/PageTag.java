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
package com.ajaxjs.mvc.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.map.MapTool;

/**
 * 分页标签
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class PageTag extends SimpleTagSupport {
	private String queryString;

	private int totalPage;

	@Override
	public void doTag() throws JspException, IOException {
		JspContext context = getJspContext();
		context.setAttribute("getParams_without_start", getParams_without("start", queryString));
		context.setAttribute("getParams_without_asMap", getParams_without_asMap("limit", queryString));
		context.setAttribute("page_jumpTo", jumpPage(totalPage));
	}

	/**
	 * 对某段 URL 参数剔除其中的一个。
	 * 
	 * @param withoutParam 不需要的那个参数
	 * @param queryString 通常由 request.getQueryString() 或  ${pageContext.request.queryString} 返回的 url 参数
	 * @return 特定的 url 参数
	 */
	public static String getParams_without(String withoutParam, String queryString) {
		if (queryString == null)
			return null;

		queryString = queryString.replaceAll("&?" + withoutParam + "=[^&]*", "");// 删除其中一个参数

		if (CommonUtil.isEmptyString(queryString))
			return null;

		return queryString.startsWith("&") ? queryString : "&" + queryString; // 补充关联的符号

	}

	/**
	 * 对某段 URL 参数剔除其中的一个。但是返回 map。
	 * 
	 * @param withoutParam 不需要的那个参数
	 * @param queryString 通常由 request.getQueryString() 或
	 * ${pageContext.request.queryString} 返回的 url 参数
	 * @return 已处理过的 Map
	 */
	public static Map<String, Object> getParams_without_asMap(String withoutParam, String queryString) {
		queryString = getParams_without(withoutParam, queryString);

		if (CommonUtil.isEmptyString(queryString))
			return null;

		// if(queryString.startsWith("&"))
		queryString = queryString.replaceFirst("&", "");// 去掉第一个无用的 &
		return MapTool.toMap(queryString.split("&"), v -> v);
	}

	/**
	 * 保持状态，对当前的参数记录，不受分页的影响
	 * 
	 * @deprecated
	 * @param request
	 * @param params
	 * @return URL 地址
	 */
	public static String appendParams(HttpServletRequest request, Map<String, String> params) {
		Map<String, String[]> map = request.getParameterMap();

		List<String> list = new ArrayList<>();

		for (String name : map.keySet()) {
			String[] values = map.get(name); // 即使是 key 都变成 value 了

			// 如果要重新指定参数
			if (params != null && params.size() > 0) {
				for (int i = 0; i < values.length; i++) {
					String v = values[i];
					if (params.containsKey(v)) {
						values[i] = params.get(v);
					}
				}
			}

			String aa;
			if (name.equals("start")) {
				continue;
			} else if (values.length == 1) {
				aa = String.join(name + "=", values);
			} else {
				aa = name + "=" + String.join("&" + name + "=", values);
			}

			list.add(aa);
		}

		String s = String.join("&", list);
		return s;
	}

	public int[] jumpPage(int totalPage) {
		return new int[totalPage];
	}

	public static String arrayJoin(String[] arr) {
		return String.join(",", arr);
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}