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
package com.ajaxjs.web.security.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全过滤器
 * 
 * @author Frank
 *
 */
public interface SecurityFilter {

	/**
	 * 检查是否通过测试
	 * 
	 * @param request
	 * @return true 表示为通过；false 不通过
	 */
	public boolean check(HttpServletRequest request);

	public boolean isInWhiteList(String str);

	public boolean isInBlackList(String str);
}
