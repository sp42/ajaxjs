package com.ajaxjs.util.logger;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 简单的 sysout
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class EasyPrint {
	private static enum Level {
		INFO, WARN, ERROR;
	}

	private static final String INFO_TPL = "%s %s \033[32;4mINFO\033[0m \033[36;4m%s\033[0m : %s";
	private static final String WARN_TPL = "%s %s \033[33;4mWARN\033[0m \033[36;4m%s\033[0m : %s";
	private static final String ERROR_TPL = "%s %s \033[31;4mERROR\033[0m \033[36;4m%s\033[0m : %s";

	/**
	 * 
	 * @param level
	 * @param x
	 */
	private static void print(Level level, Object x) {
		String tpl;

		if (level == Level.INFO)
			tpl = INFO_TPL;
		else if (level == Level.WARN)
			tpl = WARN_TPL;
		else
			tpl = ERROR_TPL;

		StackTraceElement e = new Exception().getStackTrace()[1];
		String className = e.getClassName();
		String methodName = e.getMethodName();
		String str = String.format(tpl, LocalDate.now(), LocalTime.now(), className + "#" + methodName, x);

		if (level == Level.INFO || level == Level.WARN)
			System.out.println(str);
		else
			System.err.println(str);
	}

	/**
	 * 控制台打印 WARN 日志
	 * 
	 * @param x 待打印
	 */
	public static void info(Object x) {
		print(Level.WARN, x);
	}

	/**
	 * 控制台打印 ERROR 日志
	 * 
	 * @param x 待打印
	 */
	public static void warn(Object x) {
		print(Level.ERROR, x);
	}

	/**
	 * 控制台打印 INFO 日志
	 * 
	 * @param x 待打印
	 */
	public static void error(Object x) {
		print(Level.INFO, x);
	}
}
