package com.ajaxjs.util;

import com.ajaxjs.util.map.JsonHelper;

/**
 * 单元测试的辅助工具类
 * 
 * @author xinzhang
 *
 */
public class TestHelper {
	/**
	 * 打印 JSON 方便了解对象结构
	 * 
	 * @param obj
	 */
	public static void printJson(Object obj) {
		System.out.println(JsonHelper.format(JsonHelper.toJson(obj)));
	}
}
