/**
 * Copyright Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.logger;

import java.util.Arrays;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.ajaxjs.Version;

/**
 * 自定义日志工具类，封装了 Java 自带的日志类 java.util.logging.Logger。
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class LogHelper {
	/**
	 * 创建一个日志类
	 * 
	 * @param clazz 当前日志记录的那个类
	 */
	public LogHelper(Class<?> clazz) {
		className = clazz.getName().trim();
		logger = Logger.getLogger(className);

		if (!Version.isDebug) {
//			if(fileHandler == null) {
//				fileHandler = new FileHandler(Version.srcFolder, null, ".log");
//			}
//		
//			logger.addHandler(fileHandler);// 初始化保存到磁盤的處理器
		}

		logger.setFilter(filter);
	}
	
	/**
	 * 简单打印信息，用于代替 System.out.println()
	 * @param msg
	 */
	public static void p(String msg) {
		Logger.getGlobal().info(msg);
	}
	/**
	 * 简单打印信息，用于代替 System.out.println()
	 * @param msg
	 */
	public static void p(Object[] arr) {
		Logger.getGlobal().info(Arrays.toString(arr));
	}
	
	

	@SuppressWarnings("unused")
	private static FileHandler fileHandler;

	private static final int NORMAL = 0;
	private static final int BRIGHT = 1;
//	private static final int FOREGROUND_BLACK = 30;
	private static final int FOREGROUND_RED = 31;
	private static final int FOREGROUND_GREEN = 32;
	private static final int FOREGROUND_YELLOW = 33;
//	private static final int FOREGROUND_BLUE = 34;
//	private static final int FOREGROUND_MAGENTA = 35;
	private static final int FOREGROUND_CYAN = 36;
//	private static final int FOREGROUND_WHITE = 37;

	private static final String PREFIX = "\u001b[";
	private static final String SUFFIX = "m ";
	private static final char SEPARATOR = ';';
//	private static final String END_COLOUR = PREFIX + SUFFIX;

	private static final String WARN_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_YELLOW + SUFFIX;
	private static final String INFO_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_GREEN + SUFFIX;
	private static final String FATAL_COLOUR = PREFIX + BRIGHT + SEPARATOR + FOREGROUND_RED + SUFFIX;
	private static final String COLOUR_END = PREFIX + "0m";
//	private static final String ERROR_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_RED + SUFFIX;
	private static final String DEBUG_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_CYAN + SUFFIX;
//	private static final String TRACE_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_BLUE + SUFFIX;

	/**
	 * 所在的类名
	 */
	private String className;

	/**
	 * 包装这个 logger
	 */
	private Logger logger;

	/**
	 * 过滤器，是否要日志服务
	 */
	private final static Filter filter = new Filter() {
		@Override
		public boolean isLoggable(LogRecord record) {
			return (record.getMessage() == null || record.getMessage().contains("no log")) ? false : true;
		}
	};

	/**
	 * 获取自定义的 logger。这是外界调用本类最主要的方法。例如： private static final LogHelper LOGGER =
	 * LogHelper.getLog(SqlProvider.class);
	 * 
	 * @param clazz 被日志记录的类
	 * @return 日志管理器
	 */
	public static LogHelper getLog(Class<?> clazz) {
		return new LogHelper(clazz); // 如果没有，则新建一个并保存到缓存中
	}

	/**
	 * 打印日志，支持 {0}、{1}...的占位符
	 * 
	 * @param level 日志级别
	 * @param msg   日志信息
	 */
	public void logMsg(Level level, String msg) {
		logger.logp(level, className, getMethodName(), msg);
	}

	/**
	 * 打印日志，支持 {0}、{1}...的占位符
	 * 
	 * @param level  日志级别
	 * @param msgTpl 日志信息模版
	 * @param params 日志信息参数
	 */
	public void logMsg(Level level, String msgTpl, Object... params) {
		logger.logp(level, className, getMethodName(), msgTpl, params);
	}

	/**
	 * 打印一个日志
	 * 
	 * @param msg 日志信息
	 */
	public void info(String msg) {
		logMsg(Level.INFO, DEBUG_COLOUR + msg + COLOUR_END);
	}

	/**
	 * 打印一个日志，支持 {0}、{1}...的占位符
	 * 
	 * @param msgTpl 信息语句之模板
	 * @param params 信息参数
	 */
	public void info(String msgTpl, Object... params) {
		logMsg(Level.INFO, DEBUG_COLOUR + msgTpl + COLOUR_END, params);
	}

	public void infoGreen(String msg) {
		info(INFO_COLOUR + msg + COLOUR_END);
	}

	public void infoYellow(String msg) {
		info(WARN_COLOUR + msg + COLOUR_END);
	}

	public void infoCYAN(String msg) {
		info(DEBUG_COLOUR + msg + COLOUR_END);
	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param msg 警告信息
	 */
	public void warning(String msg) {
		logMsg(Level.WARNING, FATAL_COLOUR + msg + COLOUR_END);
	}

	/**
	 * 打印一个日志（警告级别），支持 {0}、{1}...的占位符
	 * 
	 * @param msgTpl 信息语句之模板
	 * @param params 信息参数
	 */
	public void warning(String msgTpl, Object... params) {
		logMsg(Level.WARNING, FATAL_COLOUR + msgTpl + COLOUR_END, params);
	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param msg 警告信息
	 * @param ex  任意异常信息
	 */
	public void warning(Throwable ex, String msg) {
		logger.logp(Level.WARNING, className, getMethodName(), FATAL_COLOUR + msg + COLOUR_END, ex);
	}

	/**
	 * 打印一个日志（警告级别） ，支持 {0}、{1}...的占位符e.g: log.warning("脚本引擎 {0} 没有 {1}() 这个方法",
	 * "js", "foo");
	 * 
	 * @param e      任意异常信息
	 * @param msg    警告信息
	 * @param params 信息参数
	 */
	public void warning(Throwable e, String msg, Object... params) {
		for (int i = 0; i < params.length; i++) // jre 没有这个方法的重载，写一个吧
			msg = msg.replace("{" + i + "}", params[i].toString());

		warning(e, msg);
	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param ex 任意异常信息
	 */
	public void warning(Throwable e) {
		warning(e, e.getMessage());
	}

	/**
	 * 获取所在的方法，调用时候
	 * 
	 * @return 方法名称
	 */
	private String getMethodName() {
		StackTraceElement frame = null;

		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			String clzName = ste.getClassName();

			if (ste.isNativeMethod() || clzName.equals(Thread.class.getName()) || clzName.equals(getClass().getName()))
				continue;

			if (clzName.equals(className)) {
				frame = ste;
				break;
			}
		}

		if (frame != null) {// 超链接，跳到源码所在行数
			return String.format(".%s(%s:%s)", frame.getMethodName(), frame.getFileName(), frame.getLineNumber());
		} else {
			return null;
		}
	}

}
