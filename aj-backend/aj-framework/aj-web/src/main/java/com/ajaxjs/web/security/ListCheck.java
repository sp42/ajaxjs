package com.ajaxjs.web.security;

import java.util.List;
import java.util.regex.Pattern;

import com.ajaxjs.web.Utils;

/**
 * 白名单/黑名单
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ListCheck {
	/**
	 * 白名单
	 */
	private List<String> whiteList;

	/**
	 * 黑名单
	 */

	private List<String> blackList;

	/**
	 * 是否在白名单列表中
	 * 
	 * @param str 待检查的字符串
	 * @return true 表示为包含在白名单；false 表示为不包含在白名单
	 */
	public boolean isInWhiteList(String str) {
		return isInList(str, whiteList);
	}

	/**
	 * 是否在黑名单列表中。黑名单会专门抛出异常，以便记录。
	 * 
	 * @param str 待检查的字符串
	 * @return true 表示为包含在黑名单；false 表示为不包含在黑名单
	 */
	public boolean isInBlackList(String str) {
		boolean isIn = isInList(str, blackList);

//		if (!isIn)
//			throw new SecurityException(String.format("地址[%s]已列入黑名单！", str));

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
		if (Utils.isEmptyList(list))
			return false;

		for (String pattern : list) {
			if (Pattern.matches(pattern, str)) // TODO 可以改为静态的 Pattern 编译类型，更快（但也更占内存）
				return true;
		}

		return false;
	}
}
