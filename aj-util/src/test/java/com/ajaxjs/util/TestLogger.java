package com.ajaxjs.util;

import static org.junit.Assert.*;
import static com.ajaxjs.util.logger.EasyPrint.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;

import com.ajaxjs.util.logger.FileHandler;
import com.ajaxjs.util.logger.LogHelper;

public class TestLogger {
	@Test
	public void testLog() {
		LogHelper log = new LogHelper(TestLogger.class);
		assertNotNull(log);

		log.info("bar");
		log.warning("foo");
		log.warning("脚本引擎 {0} 没有 {1}() 这个方法", "js", "foo");
		log.warning(new Exception("致命错误！"), "脚本引擎 {0} 没有 {1}() 这个方法", "js", "foo");
	}

	private static final LogHelper log = LogHelper.getLog(TestLogger.class);

	@Test
	public void testGetLog() {
		assertNotNull(log);
		log.warning("You got an error!");
		log.info("It's ok.");
	}

	@Test
	public void testCatchEx() {
		LogHelper log = LogHelper.getLog(TestLogger.class);

		try {
			throw new Throwable("You got an error!");
		} catch (Throwable e) {
			assertNotNull(e);
			log.warning(e);
		}
	}

	/**
	 * 获取当前异常是在哪一行代码上发生的。
	 */
	public static void getCurrentCodeLine() {
		StackTraceElement s = Thread.currentThread().getStackTrace()[1];
		System.out.printf("%s.%s(%s:%s)%n", s.getClassName(), s.getMethodName(), s.getFileName(), s.getLineNumber());
	}

//	 @Test
	public void testFileHandler() throws SecurityException, IOException {
		FileHandler handler = new FileHandler("c:/temp", "CMS-", ".log");
		assertNotNull(handler);
		LogRecord record = new LogRecord(Level.WARNING, "test");
		handler.flush();
		record.setMessage("You got a message!");
		handler.publish(record);
		handler.close();
	}
	
	@Test
	public void test() {
		info("Hello");
		warn("Hello");
		error("Hello");
	}
}
