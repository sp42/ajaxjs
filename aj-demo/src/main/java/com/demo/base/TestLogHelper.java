package com.demo.base;

import org.junit.Test;

import com.ajaxjs.util.logger.LogHelper;

public class TestLogHelper {
	// 创建类成员为日志服务
	private static final LogHelper LOGGER = LogHelper.getLog(TestLogHelper.class);

	@Test
	public void testGetLog() {
		// …… 其他代码
		LOGGER.warning("发生异常！……");
		LOGGER.info("bar");
		LOGGER.warning("foo");
		// 带有多个日志消息的对象参数，用 {0},{1},{2} 预留消息位置
		LOGGER.warning("脚本引擎 {0} 没有 {1}() 这个方法", "js", "foo");
		LOGGER.warning(new Exception("致命错误！"), "脚本引擎 {0} 没有 {1}() 这个方法", "js", "foo");
	}
}