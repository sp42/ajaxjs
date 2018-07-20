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
package com.ajaxjs.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.config.SiteStruService;

/**
 * 该类对应 tagfile：head.tag
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class HtmlHead {
	Map<String, Object> node;

	/**
	 * 初始化
	 * 
	 * @param request
	 *            请求对象
	 */
	public void init(HttpServletRequest request) {
		// 设置页面 node
		node = SiteStruService.getPageNode(request.getRequestURI(), request.getContextPath());
	}

	public Map<String, Object> getNode() {
		return node;
	}
}
