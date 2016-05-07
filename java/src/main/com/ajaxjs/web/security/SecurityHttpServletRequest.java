package com.ajaxjs.web.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

import com.ajaxjs.Constant;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;

public class SecurityHttpServletRequest extends HttpServletRequestWrapper {
	public SecurityHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		name = xssFilter(name, XssFilterTypeEnum.DELETE.getValue());
		return xssFilter(super.getParameter(name), null);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramsMap = super.getParameterMap();
		if (!Util.isNotNull(paramsMap)) return paramsMap;

		Map<String, String[]> resParamsMap = new HashMap<>();
		Iterator<Entry<String, String[]>> iter = paramsMap.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, String[]> entry = iter.next();
			resParamsMap.put( (xssFilter(entry.getKey(), XssFilterTypeEnum.DELETE.getValue())), filterList(entry.getValue()));
		}
		
		return resParamsMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();
		Vector<String> vec = new Vector<>();
		
		while (enums.hasMoreElements()) {
			String value = enums.nextElement();
			vec.add(xssFilter(value, null));
		}
		
		return vec.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		return filterList(super.getParameterValues(name));
	}

	private String[] filterList(String[] value) {
		if (!Util.isNotNull(value)) return value;
		
		List<String> resValueList = new ArrayList<>();
		
		for (String val : value) 
			resValueList.add(xssFilter(val, null));
		
		return resValueList.toArray(new String[resValueList.size()]);
	}

	/**
	 * 文件上传安全过滤
	 */
	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		Collection<Part> parts = super.getParts();
		
		if (parts == null || parts.isEmpty()
				|| SecurityFilter.whitefilePostFixList == null
				|| SecurityFilter.whitefilePostFixList.isEmpty()) {
			return parts;
		}
		
		List<Part> resParts = new ArrayList<>();
		for (Part part : parts) {
			for (String extension : SecurityFilter.whitefilePostFixList) {
				if (part.getName().toUpperCase().endsWith(extension)) 
					resParts.add(part);
				
			}
		}
		
		return resParts;
	}

	/**
	 * 上传文件后缀名符合白名单则允许上传
	 */
	@Override
	public Part getPart(String name) throws IOException, ServletException {
		Part part = super.getPart(name);
		
		if (SecurityFilter.whitefilePostFixList.isEmpty()) {
			return part;
		}
		
		String value = part.getHeader("content-disposition");
		String filename = value.substring(value.lastIndexOf("=") + 2, value.length() - 1);
		
		for (String extension : SecurityFilter.whitefilePostFixList) {
			if (filename.toUpperCase().endsWith(extension.toUpperCase()))
				return part;
		}
		
		return null;
	}
	
	/**
	 * 返回符合白名单的 cookie
	 * 输入输出cookie白名单验证过滤
	 */
	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();
		
		if (!Util.isNotNull(cookies)) return null;
		
		List<Cookie> cookieList = new ArrayList<>();
		for (Cookie cookie : cookies) {
			for (String name : SecurityFilter.cookieWhiteList) {
				if (name.equalsIgnoreCase(cookie.getName()))
					cookieList.add(cookie);
			}
		}
		
		return cookieList.toArray(new Cookie[cookieList.size()]);
	}
	
	private static String xssType = "<script[^>]*?>.*?</script>";

	private static Pattern xssPattern = Pattern.compile(xssType);

	/**
	 * XSS 过滤器
	 * @param input
	 * @param filterType 过滤器类型
	 * @return
	 */
	public static String xssFilter(String input, String filterType) {
		if (StringUtil.isEmptyString(input))
			return input;
		
		if (filterType == null || !XssFilterTypeEnum.checkValid(filterType)) 
			filterType = XssFilterTypeEnum.ESCAPSE.getValue(); // 默认转义
		
		if (filterType.equals(XssFilterTypeEnum.ESCAPSE.getValue())) {
			Matcher matcher = xssPattern.matcher(input);
			
			if (matcher.find()) 
				return matcher.group().replace("<", "&lt;").replace(">", "&gt;");
		}
		if (filterType.equals(XssFilterTypeEnum.DELETE.getValue())) 
			return input.replaceAll(xssType, Constant.emptyString);
		
		return input;
	}
	
	/**
	 * 过滤器类型
	 * @author frank
	 *
	 */
	public static enum XssFilterTypeEnum {
		ESCAPSE("escapse"), NO("no"), DELETE("delete");

		private String value;
		
		/**
		 * 
		 * @param type
		 */
		private XssFilterTypeEnum(String type) {
			value = type;
		}
		
		/**
		 * 获取值
		 * @return
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * 
		 * @param type
		 * @return
		 */
		public static boolean checkValid(String type) {
			if (StringUtil.isEmptyString(type)) {
				return false;
			}
			return (ESCAPSE.getValue().equals(type) || NO.getValue().equals(type) || DELETE.getValue().equals(type));
		}
	}
}
