package com.ajaxjs.fast_doc;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.fast_doc.Model.BeanInfo;

public class BeanParser {
	public static Map<String, BeanInfo> CACHE = new HashMap<>();

	public static BeanInfo getBeanInfo(Class<?> clz, Params params) {
		String fullName = clz.getName();
		BeanInfo bean = null;

//		LOGGER.info("want clz: " + fullName);
		if (CACHE.containsKey(fullName))
			bean = CACHE.get(fullName);
		else {
			if (clz.getName().contains("UAVRouteOutputOfApproach") || clz.getName().contains("PageResult")) {
			} else {
				bean = Doclet.parseFieldsOfOneBean(params, clz);
				CACHE.put(fullName, bean);
			}
		}

		return bean;
	}
}
