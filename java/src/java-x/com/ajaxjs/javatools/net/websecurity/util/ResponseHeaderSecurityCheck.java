package com.ajaxjs.util.websecurity.util;

import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import com.ajaxjs.util.websecurity.SecurityConstant;

/**
 * 
 * @author weijian.zhongwj
 * 
 */
public class ResponseHeaderSecurityCheck {

	public static Cookie checkCookie(Cookie inputCookie) {
		if (inputCookie == null) 
			return null;
		
		String name = inputCookie.getName();
		String value = inputCookie.getValue();
		
		if (containCLRF(name)) 
			throw new RuntimeException("cookie name could not contain CLRF " + name);
		
		String newValue = filterCLRF(value);
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

	private static boolean containCLRF(String name) {
		if (name == null || name.isEmpty())
			return false;
		
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '\r' || name.charAt(i) == '\n') 
				return true;
		}
		return false;
	}

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

	public static boolean checkRedirectValid(String location) {
		if (location == null || location.isEmpty()) 
			return false;
		
		for (Pattern pattern : SecurityConstant.redirectLocationWhiteList) {
			if (pattern.matcher(location).find()) 
				return true;
		}
		
		return false;
	}

}
