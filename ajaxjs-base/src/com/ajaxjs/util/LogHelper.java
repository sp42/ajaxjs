/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 自定义日志工具类，封装了 Java 自带的日志类 java.util.logging.Logger。
 * @author frank
 *
 */
public class LogHelper {
	private String className;				// 所在的类名
	private Logger logger;					// 包装这个 logger

//	private static String logFilePath = "%t/log%g.log"; // 日志文件，在系统环境变量 temp 目录下 logX.log，其中 X 会自增。
	private static Map<String, LogHelper> cache = new HashMap<>();// 缓存
	
	
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
	 * 创建一个日志类
	 * 
	 * @param clazz
	 *            当前日志记录的那个类
	 */
	public LogHelper(Class<?> clazz) {
		className = clazz.getName().trim();
		logger = Logger.getLogger(className);
// 经常会 eclipse 下报错，原因未知
//		logger.addHandler(fileHandlerFactory(logFilePath));// 初始化保存到磁盤的處理器
		logger.setFilter(filter);
	}
	
	/**
	 * 获取自定义的 logger。这是外界调用本类最主要的方法。例如：
	 * private static final LogHelper LOGGER = LogHelper.getLog(SqlProvider.class);
	 * @param clazz
	 *            被日志记录的类
	 * @return 日志管理器
	 */
	public static LogHelper getLog(Class<?> clazz) {
		String key = clazz == null ? "root" : clazz.getName().trim();

		if (cache.containsKey(key)) {// 从缓存中查找，
			return cache.get(key);
		} else {
			LogHelper logger = new LogHelper(clazz); // 如果没有，则新建一个并保存到缓存中
			cache.put(key, logger);
			
			return logger;
		}
	}
		
	/**
	 * 为后续的 logger 加入 handler
	 * 
	 * @param handler
	 *            接口为 handler 的处理器
	 */
	public static void addHanlder(Handler handler) {
		for (String key : cache.keySet())  // 每个都加上
			cache.get(key).getLogger().addHandler(handler);
	}

	/**
	 * 打印一个日志
	 * 
	 * @param msg
	 *            日志信息
	 */
	public void info(String msg) {
		logger.logp(Level.INFO, className, getMethodName(), msg);
	}

	/**
	 * 打印一个日志
	 * 
	 * @param tpl
	 *            信息语句之模板
	 * @param params
	 *            信息参数
	 */
	public void info(String tpl, Object... params) {
		logger.logp(Level.INFO, className, getMethodName(), tpl, params);

	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param msg
	 *            警告信息
	 */
	public void warning(String msg) {
		logger.logp(Level.WARNING, className, getMethodName(), msg);
	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param tpl
	 *            信息语句之模板
	 * @param params
	 *            信息参数
	 */
	public void warning(String tpl, Object... params) {
		logger.logp(Level.WARNING, className, getMethodName(), tpl, params);
	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param ex
	 *            任意异常信息
	 */
	public void warning(Throwable ex) {
		logger.logp(Level.WARNING, className, getMethodName(), ex.getMessage(), ex);
	}
	
	/**
	 * 打印一个日志（警告级别）
	 * e.g: log.warning("脚本引擎 {0} 没有 {1}() 这个方法", "js", "foo");
	 * @param msg
	 *            警告信息
	 * @param ex
	 *            任意异常信息
	 */
	public void warning(Throwable ex, String msg) {
		logger.logp(Level.WARNING, className, getMethodName(), msg, ex);
	}
	
	public void warning(Throwable ex, String msg, Object... params) {
		for(int i = 0; i < params.length; i++) { // jre 没有这个方法的重载，写一个吧
			msg = msg.replace("{" + i +"}", params[i].toString());
		}
		logger.logp(Level.WARNING, className, getMethodName(), msg, ex);
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
 
		
		if(frame != null) {// 超链接，跳到源码所在行数
			return String.format(".%s(%s:%s)", frame.getMethodName(), frame.getFileName(), frame.getLineNumber());
		}else{
			return null;
		}
	}

	/**
	 * 获取原生的 logger
	 * 
	 * @return 原生的 logger
	 */
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * 控制台支持字符串 format
	 * @deprecated
	 * @param msg
	 *            插入的内容
	 */
	public static void sysConsole(String msg) {
		System.console().format("%S", msg); // or System.out.printf("%s.%s(%s:%s)%n", ..., ...);
	}

}
