package com.util.logger;

import java.io.PrintStream;
import java.util.Date;

/**
 * 简单日志输出工具类
 * 
 * log4j输出日志是很方便的,但有的时候，不希望依赖太多第三方库，但希望用类似log4j的方式方便在控制台输出程序运行的基本信息,用System.out.printf输出又无法显示代码位置，而且printf还要注意匹配输出参数的类型有点麻烦。
 * 就想着自己写一个简单的日志输出方法代替System.out.printf.
 * 基本的原理就是获取当前线程的堆栈信息StackTraceElement[]，通过StackTraceElement获取当前的类的文件名和行号,与输入的参数一起转成String输出。
 * ———————————————— 版权声明：本文为CSDN博主「10km」的原创文章，遵循CC 4.0
 * BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/10km/article/details/79719592
 * 
 * @author guyadong
 *
 */
public class SampleLog {
	private static final String DELIM_STR = "{}";
	private static final Object[] EMPTY_ARGS = new Object[0];

	private static void log(PrintStream printStream, int level, String format, Object... args) {
		if (null == printStream || null == format)
			return;

		if (null == args)
			args = EMPTY_ARGS;

		StringBuilder buffer = new StringBuilder(format.length() + 64);
		int beginIndex = 0, endIndex = 0, count = 0;

		while ((endIndex = format.indexOf(DELIM_STR, beginIndex)) >= 0) {
			buffer.append(format.substring(beginIndex, endIndex));

			try {
				buffer.append(args[count++]);
			} catch (IndexOutOfBoundsException e) {
				// 数组越界时对应占位符填null
				buffer.append("null");
			}
			beginIndex = endIndex + DELIM_STR.length();
		}

		buffer.append(format.substring(beginIndex, format.length()));
		Thread currentThread = Thread.currentThread();
		StackTraceElement stackTrace = currentThread.getStackTrace()[level];
		printStream.printf("[%s] (%s:%d) %s\n", currentThread.getName(), stackTrace.getFileName(), stackTrace.getLineNumber(), buffer.toString());
	}

	/**
	 * 向{@code printStream}输出日志信息<br>
	 * example:
	 * 
	 * <pre>
	 * log("name : {},age:{}", "tom", 23);
	 * </pre>
	 * 
	 * @param printStream
	 * @param format      格式字符串,采用{@code '{}'}为占位符,占位符个数要与{@code args}数组长度匹配
	 * @param args
	 */
	public static void log(PrintStream printStream, String format, Object... args) {
		log(printStream, 3, format, args);
	}

	/**
	 * 向控制台输出日志信息<br>
	 * 
	 * @param format 格式字符串,采用{@code '{}'}为占位符
	 * @param args
	 * @see #log(PrintStream, String, Object...)
	 */
	public static void log(String format, Object... args) {
		log(System.out, 3, format, args);
	}

	public void test() {
		SampleLog.log("wwwwww", "tom");
		SampleLog.log("name {},age:{}");
		SampleLog.log("name {},age:{} ww", "tom");
		SampleLog.log("name {},age:{},date:{},time:{}", "tom", 23, new Date());
	}
}