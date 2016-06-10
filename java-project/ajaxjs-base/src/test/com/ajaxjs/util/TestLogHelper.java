package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.util.logging.FileHandler;

import org.junit.Test;

import com.ajaxjs.util.LogHelper;

public class TestLogHelper {
	@Test
	public void testLog() {
		LogHelper log = new LogHelper(TestLogHelper.class);
		assertNotNull(log);
		log.warning("fooo");
		log.info("bar");
	}

	private static final LogHelper log = LogHelper.getLog(TestLogHelper.class);

	@Test
	public void testGetLog() {
		assertNotNull(log);
		log.warning("dsada");
		log.info("dsada");
	}

	@Test
	public void testCatchEx() {
		LogHelper log = LogHelper.getLog(TestLogHelper.class);
		
		try {
			throw new Throwable("h");
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

	@Test
	public void testFileHandlerFactory() {
		FileHandler handler = LogHelper.fileHandlerFactory("c://temp//rz.txt");
		assertNotNull(handler);
	}
}
