package com.ajaxjs.security.xss;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * 获取用户输入参数和参数值进行 XSS 过滤，对 Header 和 cookie value 值进行 XSS 过滤（转码 Script 标签的< > 符号
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class XssReqeust extends HttpServletRequestWrapper {

	public XssReqeust(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String key) {
		key = Xss.clean(key, Xss.type_DELETE);
		return Xss.clean(super.getParameter(key));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramsMap = super.getParameterMap();
		if (paramsMap == null)
			return null;

		Map<String, String[]> map = new HashMap<>();
		for (String key : paramsMap.keySet())
			map.put(Xss.clean(key, Xss.type_DELETE), Xss.clean(paramsMap.get(key)));

		return map;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();
		Vector<String> vec = new Vector<>();

		while (enums.hasMoreElements()) {
			String value = enums.nextElement();
			vec.add(Xss.clean(value));
		}

		return vec.elements();
	}

	@Override
	public String[] getParameterValues(String key) {
		return Xss.clean(super.getParameterValues(key));
	}
}
