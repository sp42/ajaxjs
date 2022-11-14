package com.ajaxjs.util;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * 集合工具类
 * 
 * @author frank
 *
 */
public class ListUtils {
	public static <T> List<T> getList(List<T> list) {
		if (CollectionUtils.isEmpty(list))
			list = Collections.emptyList();

		return list;
	}
}
