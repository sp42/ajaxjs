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
package com.ajaxjs.util.map;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.StringUtil;

public class Form<K, V extends FormValue> extends MapHelper<String, IValue>{
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	public Form(HttpServletRequest request) {
		this.request = request;
		getAll();
		
//		Map<String, String[]> rawMap = request.getParameterMap();
		
//		for (String k : rawMap.keySet()) {
//
//		}
	}
	
	/**
	 * 获取客户端请求的参数和值
	 * 
	 * @return
	 */
	public void getAll() {
		String key = null, value = null;
		Enumeration<String> enu = request.getParameterNames();

		while (enu.hasMoreElements()) {
			key = enu.nextElement();
			value = request.getParameter(key);

			try {
				value = StringUtil.byte2String(value.getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				continue; // 跳过
			}

			put(key, new FormValue(value));
		}
	}
}
