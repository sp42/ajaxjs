package com.ajaxjs.web.security.filter;

import javax.servlet.http.Cookie;

import com.ajaxjs.web.security.SecurityException;

public class CLRF {

	/**
	 * 如果 cookie 名称包含 CLRF 则抛出异常；接着过滤 cookie 内容
	 * @param inputCookie
	 * @return
	 * @throws SecurityException
	 */
	public static Cookie checkCookie(Cookie inputCookie) throws SecurityException {
		if (inputCookie == null)
			return null;
	
		String name = inputCookie.getName(), value = inputCookie.getValue();
	
		if (CLRF.containCLRF(name))
			throw new SecurityException(" cookie 名称不能包含 CLRF，该 cookie 是：" + name);
	
		String newValue = CLRF.filterCLRF(value);// 已经过滤好的 Cookie value
		// 重新创建 cookie
		Cookie newCookie = new Cookie(name, newValue);
		newCookie.setComment(inputCookie.getComment());
	
		if (inputCookie.getDomain() != null)
			newCookie.setDomain(inputCookie.getDomain());
	
		newCookie.setHttpOnly(inputCookie.isHttpOnly());
		newCookie.setMaxAge(inputCookie.getMaxAge());
		newCookie.setPath(inputCookie.getPath());
		newCookie.setSecure(inputCookie.getSecure());
		newCookie.setVersion(inputCookie.getVersion());
	
		return newCookie;
	}

	/**
	 * 过滤  \r 、\n之类的换行符
	 * @param value
	 * @return
	 */
	public static String filterCLRF(String value) {
		if (value == null || value.isEmpty())
			return value;
	
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			if (!(value.charAt(i) == '\r' || value.charAt(i) == '\n'))
				sb.append(value.charAt(i));
		}
		return sb.toString();
	}

	/**
	 * 是否包含 \r 、\n之类的换行符
	 * @param name
	 * @return
	 */
	public static boolean containCLRF(String name) {
		if (name == null || name.isEmpty())
			return false;
	
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '\r' || name.charAt(i) == '\n')
				return true;
		}
		return false;
	}

}
