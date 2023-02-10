package com.ajaxjs.util;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.map.JsonHelper;

/**
 * 单元测试的辅助工具类
 * 
 * @author xinzhang
 *
 */
public class TestHelper {
	public static void json(Object obj) {
		System.out.println("---------------");
		System.out.println(JsonHelper.toJson(obj));
	}

	/**
	 * 打印 JSON 方便了解对象结构
	 * 
	 * @param obj
	 */
	public static void printJson(Object obj) {
		System.out.println("---------------");
		System.out.println(JsonHelper.format(JsonHelper.toJson(obj)));
	}

	public static void printJson2(Object obj) {
		System.out.println((JsonHelper.toJson(obj)));
	}

	private static Boolean isRunningTest;

	/**
	 * For static-way to get request in UNIT TEST
	 */
	public static HttpServletRequest request;

	/**
	 * 检测是否在运行单元测试
	 * 
	 * @return
	 */
	public static Boolean isRunningTest() {
		if (isRunningTest == null) {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

			for (StackTraceElement e : stackTrace) {
				if (e.toString().lastIndexOf("junit.runners") > -1) {
					isRunningTest = true;
					return isRunningTest;
				}
			}

			isRunningTest = false;
		}

		return isRunningTest;
	}
}
