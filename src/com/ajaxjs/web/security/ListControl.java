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
package com.ajaxjs.web.security;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 比较简单的过滤器，只是针对于 Request
 * @author Frank
 *
 */
public class ListControl {
	/**
	 * 白名单
	 * 
	 * @return
	 */
	public List<String> whiteList;

	/**
	 * 黑名单
	 * 
	 * @return
	 */
	public List<String> blackList;

	/**
	 * 是否在白名单列表中
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return true 表示为包含在白名单；false 表示为不包含在白名单
	 */
	public boolean isInWhiteList(String str) {
		return isInList(str, whiteList);
	}

	private final static String msg = "地址 %s 已列入黑名单！";

	/**
	 * 是否在黑名单列表中。黑名单会专门抛出异常，以便记录。
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return true 表示为包含在黑名单；false 表示为不包含在黑名单
	 */
	public boolean isInBlackList(String str) {
		boolean isIn = isInList(str, blackList);
		if (!isIn) 
			throw new SecurityException(String.format(msg, str));
		
		return isIn;
	}

	/**
	 * 是否在列表中
	 * 
	 * @param str
	 * @param list
	 * @return true 表示为包含；false 表示为不包含
	 */
	private static boolean isInList(String str, List<String> list) {
		if (list == null || list.size() == 0)
			return false;

		for (String pattern : list) {
			if (Pattern.matches(pattern, str)) 
				return true;
		}

		return false;
	}

	public static boolean isEmptyStr(String str) {
		return null == str || str.isEmpty();
	}
}
