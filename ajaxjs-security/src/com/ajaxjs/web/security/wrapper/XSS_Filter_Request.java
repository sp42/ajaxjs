/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.web.security.in_out_filter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.web.security.SimpleFilter;

/**
 * 
 * @author Frank
 *
 */
public class XSS_Filter_Request extends HttpServletRequestWrapper {
	public XSS_Filter_Request(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		name = xssFilter(name, XssFilterTypeEnum.DELETE.getValue());
		return xssFilter(super.getParameter(name), null);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramsMap = super.getParameterMap();
		if (paramsMap == null)
			return null;

		Map<String, String[]> resParamsMap = new HashMap<>();
		Iterator<Entry<String, String[]>> iter = paramsMap.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<String, String[]> entry = iter.next();
			resParamsMap.put((xssFilter(entry.getKey(), XssFilterTypeEnum.DELETE.getValue())), filterList(entry.getValue()));
		}

		return resParamsMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();
		Vector<String> vec = new Vector<>();

		while (enums.hasMoreElements()) {
			String value = enums.nextElement();
			vec.add(xssFilter(value, null));
		}

		return vec.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		return filterList(super.getParameterValues(name));
	}

	private String[] filterList(String[] value) {
		if (value == null || value.length == 0)
			return null;

		List<String> resValueList = new ArrayList<>();

		for (String val : value)
			resValueList.add(xssFilter(val, null));

		return resValueList.toArray(new String[resValueList.size()]);
	}

	private static String xssType = "<script[^>]*?>.*?</script>";

	private static Pattern xssPattern = Pattern.compile(xssType);

	/**
	 * XSS 过滤器
	 * 
	 * @param input
	 *            输入内容
	 * @param filterType
	 *            过滤器类型
	 * @return
	 */
	public static String xssFilter(String input, String filterType) {
		if (SimpleFilter.isEmptyStr(input))
			return input;
	
		if (filterType == null || !XssFilterTypeEnum.checkValid(filterType))
			filterType = XssFilterTypeEnum.ESCAPSE.getValue(); // 默认转义
	
		if (filterType.equals(XssFilterTypeEnum.ESCAPSE.getValue())) {
			Matcher matcher = xssPattern.matcher(input);
	
			if (matcher.find())
				return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
		}
		
		if (filterType.equals(XssFilterTypeEnum.DELETE.getValue()))
			return input.replaceAll(xssType, "");
	
		return input;
	}

	/**
	 * 过滤器类型
	 * 
	 * @author Frank
	 *
	 */
	public static enum XssFilterTypeEnum {
		ESCAPSE("escapse"), NO("no"), DELETE("delete");
		
		/**
		 * 过滤器类型的值
		 */
		private String value;

		/**
		 * 
		 * @param type 过滤器类型
		 */
		private XssFilterTypeEnum(String type) {
			value = type;
		}

		/**
		 * 获取过滤器类型的值
		 * 
		 * @return
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 
		 * @param type 过滤器类型
		 * @return
		 */
		public static boolean checkValid(String type) {
			if (SimpleFilter.isEmptyStr(type)) 
				return false;
			
			return ESCAPSE.getValue().equals(type) || NO.getValue().equals(type) || DELETE.getValue().equals(type);
		}
	}
}
