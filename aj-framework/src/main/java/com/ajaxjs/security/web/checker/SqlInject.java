package com.ajaxjs.security.web.checker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * XSS 过滤器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class SqlInject {
	/**
	 * 侦测 SQL 脚本的正则
	 */
	private static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(select|update|union|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

	private static Pattern SQL_Pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	public String clean(String str) {
		if (!StringUtils.hasText(str))
			return str;

		Matcher matcher = SQL_Pattern.matcher(str);

		if (!matcher.find())
			return str;
		else
			return matcher.replaceAll("");
	}
}
