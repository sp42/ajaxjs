package com.ajaxjs.cms.utils;

public class CmsUtils {
	public static Object[] page2start(Object[] args) {
		int pageStart = (int) args[0];
		int pageSize = (int) args[1];
		if (pageSize == 0)
			pageSize = 10;
		
		int start = 0;
		if (pageStart >= 1) {
			start = (pageStart - 1) * pageSize;
		}
		
		System.out.println("start:::" + start);
		args[0] = start;
		return args;
	}
}
