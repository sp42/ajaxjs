package com.ajaxjs.web.security;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * 过滤器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Filter {
	/**
	 * 正则匹配，若找到的话进行替换，或者按照 replFn 处理
	 * 
	 * @param str
	 * @param p
	 * @param replFn
	 * @return
	 */
	private static String clean(String str, Pattern p, Function<Matcher, String> replFn) {
		if (!StringUtils.hasText(str))
			return str;

		Matcher matcher = p.matcher(str);

		if (matcher.find())
			return replFn == null ? matcher.replaceAll("") : replFn.apply(matcher);
		else
			return str;
	}

	private static Pattern CRLF_Pattern = Pattern.compile("\\r|\\n");

	/**
	 * 检测 CRLF 的过滤器 又叫做 HTTP Response Splitting
	 * 
	 * @param str
	 * @return
	 */
	public static String cleanCRLF(String str) {
		return clean(str, CRLF_Pattern, null);
	}

	/**
	 * 侦测 SQL 脚本的正则
	 */
	private static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(select|update|union|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

	private static Pattern SQL_Pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	/**
	 * 检测 SQL 注入的过滤器
	 * 
	 * @param str
	 * @return
	 */
	public static String cleanSqlInject(String str) {
		return clean(str, SQL_Pattern, null);
	}

	private static final Pattern XSS_Pattern = Pattern.compile("<script[^>]*?>.*?</script>");

	/**
	 * 对于敏感字符串该如何处理
	 */
	public static enum Handle {
		/**
		 * 转义字符串
		 */
		TYPE_ESCAPSE,

		/**
		 * 删除字符串
		 */
		TYPE_DELETE;
	}

	/**
	 * XSS 过滤器
	 * 
	 * @param str
	 * @return
	 */
	public static String cleanXSS(String str) {
		return cleanXSS(str, Handle.TYPE_DELETE);
	}

	/**
	 * XSS 过滤器
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static String cleanXSS(String str, Handle type) {
		return clean(str, XSS_Pattern, matcher -> {
			if (type == Handle.TYPE_DELETE)
				return matcher.replaceAll("");
			else
				return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
		});
	}
}
