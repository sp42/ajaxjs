package com.ajaxjs.security;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ajaxjs.util.collection.CollectionUtil;

public class StateMachinRequest extends HttpServletRequestWrapper {

	public StateMachinRequest(HttpServletRequest request) {
		super(request);
	}

	String keyFilter(String key) {
		return key;
	}

	String valueFliter(String value) {
		return value;
	}

	@Override
	public String getParameter(String key) {
		return valueFliter(super.getParameter(keyFilter(key)));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramsMap = super.getParameterMap();
		if (paramsMap == null)
			return null;

		Map<String, String[]> map = new HashMap<>();
		for (String key : paramsMap.keySet())
			map.put(keyFilter(key), valueFliter(paramsMap.get(key)));

		return map;
	}

	/**
	 * 过滤 for 数组
	 * 
	 * @param value
	 * @return
	 */
	public String[] valueFliter(String[] value) {
		if (CollectionUtil.isNull(value))
			return null;

		String[] cleaned = new String[value.length];

		for (int i = 0; i < value.length; i++)
			cleaned[i] = valueFliter(value[i]);

		return cleaned;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Enumeration<String> enums = super.getParameterNames();
		Vector<String> vector = new Vector<>();

		while (enums.hasMoreElements())
			vector.add(keyFilter(enums.nextElement()));

		return vector.elements();
	}

	@Override
	public String[] getParameterValues(String key) {
		return valueFliter(super.getParameterValues(keyFilter(key)));
	}
}
