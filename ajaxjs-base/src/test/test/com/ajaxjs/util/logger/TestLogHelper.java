package test.com.ajaxjs.util.logger;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ajaxjs.util.logger.LogHelper;

public class TestLogHelper {
	@Test
	public void testLog() {
		LogHelper log = new LogHelper(TestLogHelper.class);
		assertNotNull(log);

		log.info("bar");
		log.warning("fooo");
		log.warning("脚本引擎 {0} 没有 {1}() 这个方法", "js", "foo");
		log.warning(new Exception("致命错误！"), "脚本引擎 {0} 没有 {1}() 这个方法", "js", "foo");
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
}
