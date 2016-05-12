package com.ajaxjs.util.websecurity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SecurityConstant {
	/**
	 * 白名单
	 */
	public static final List<String> cookieWhiteList = new ArrayList<>();	
	public static final List<String> onlyPostUrlList = new ArrayList<>();
	public static final List<String> whitefilePostFixList = new ArrayList<>();
	public static final List<Pattern> redirectLocationWhiteList = new ArrayList<>();
	public static String key;
}
