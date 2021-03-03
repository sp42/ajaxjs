package com.demo.base;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleLogger {
	// 通过静态方法 Logger.getLogger 创建 LOGGER 实例
	private static final Logger LOGGER = Logger.getLogger(SimpleLogger.class.getName());

	public static void main(String[] args) {
		LOGGER.info("Logger 名称" + LOGGER.getName());
		LOGGER.warning("数组越界！");
		int[] a = { 1, 2, 3 };// 数组长度为 3
		int index = 4;
		LOGGER.config("index is set to " + index);
		String.format("Hello %s", "World!");

		try {
			System.out.println(a[index]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			LOGGER.log(Level.SEVERE, "发送异常", ex);
		}
	}
}