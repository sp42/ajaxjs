package com.ajaxjs.framework.model;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class ModelAndView extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * 保存到 request
	 * 
	 * @param request
	 */
	public void saveToReuqest(HttpServletRequest request) {
		for (String key : keySet()) {
			request.setAttribute(key, get(key));
		}
	}
}
