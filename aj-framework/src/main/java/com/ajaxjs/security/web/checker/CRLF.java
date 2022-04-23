package com.ajaxjs.security.web.checker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * 检测 CRLF 的过滤器 又叫做 HTTP Response Splitting
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class CRLF {
	public static Pattern CRLF_Pattern = Pattern.compile("\\r|\\n");

	public String clean(String str) {
		if (!StringUtils.hasText(str))
			return str;

		Matcher matcher = CRLF_Pattern.matcher(str);

		if (!matcher.find())
			return str;
		else
			return matcher.replaceAll("");
	}
}
