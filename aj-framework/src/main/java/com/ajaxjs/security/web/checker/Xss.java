package com.ajaxjs.security.web.checker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ajaxjs.security.web.Handle;

/**
 * XSS 过滤器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class Xss {
	/**
	 * 侦测脚本的正则
	 */
	private static final Pattern XSS_Pattern = Pattern.compile("<script[^>]*?>.*?</script>");

	public String clean(String str) {
		return clean(str, Handle.TYPE_DELETE);
	}

	public String clean(String str, Handle type) {
		if (!StringUtils.hasText(str))
			return str;

		Matcher matcher = XSS_Pattern.matcher(str);

		if (!matcher.find())
			return str;

		if (type == Handle.TYPE_DELETE)
			return matcher.replaceAll("");
		else
			return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
	}
}
