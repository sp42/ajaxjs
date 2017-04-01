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

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
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
	private String packageName;				// 所在的包名
	private Logger logger;					// 包装这个 logger
	private static int max_size = 1; 		// 最大文件大小，1 = 一兆
	private static int max_number = 10;
//	private static String logFilePath = "%t/log%g.log"; // 日志文件，在系统环境变量 temp 目录下 logX.log，其中 X 会自增。
	private static Map<String, LogHelper> cache = new HashMap<>();// 缓存
	
	/**
	 * 日志格式
	 */
	private final static Formatter myFormatter = new Formatter() {
		private static final String tpl = "\n%s %s : %s.%s  %s";

		@Override
		public String format(LogRecord record) {
			return String.format(tpl, new Date(), record.getLevel(), record.getLoggerName(),
					record.getSourceMethodName(), record.getMessage());
		}
	};
	
	/**
	 * 过滤器，是否要日志服务
	 */
	private final static Filter filter = new Filter() {
		@Override
		public boolean isLoggable(LogRecord record) {
			return record.getMessage() == null || record.getMessage().contains("no log") ? false : true;
		}
	};

	/**
	 * 创建一个日志类
	 * 
	 * @param clazz
	 *            当前日志记录的那个类
	 */
	public LogHelper(Class<?> clazz) {
		packageName = clazz.getName().trim();
		logger = Logger.getLogger(packageName);
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
		String key = clazz == null ? "root" : clazz.getName();

		if (cache.containsKey(key)) {// 从缓存中查找，
			return cache.get(key);
		} else {
			LogHelper logger = new LogHelper(clazz); // 如果没有，则新建一个并保存到缓存中
			cache.put(key, logger);
			return logger;
		}
	}

	/**
	 * 创建日志的文件保存器，WARNING 级别的信息才保存。
	 * 
	 * @param logFilePath
	 *            日志路径
	 * @return 文件保存器
	 */
	public static FileHandler fileHandlerFactory(String logFilePath) {
		FileHandler logHandler = null;
		
		try {
			logHandler = new FileHandler(logFilePath, 1024 * max_size, max_number, true);
			logHandler.setLevel(Level.WARNING); // WARNING 级别的或以上才记录
			logHandler.setFormatter(myFormatter);
		} catch (IOException e) {
			// 如果文件目录不存在会报错
			File file = new File(logFilePath);
			if (!file.exists() && !file.getParentFile().exists()) {
				System.out.printf("日志文件夹 %s 不存在，现在创建！", logFilePath);
				if (file.getParentFile().mkdirs()) {
					System.out.println(logFilePath + " 创建成功");
					return fileHandlerFactory(logFilePath); // 重新调用一次
				}
			} else {
				System.err.printf("日志文件路径%s错误，请检查！", logFilePath);
				e.printStackTrace();
			}
		}
		
		return logHandler;
	}
	
	/**
	 * 为后续的 logger 加入 handler
	 * 
	 * @param handler
	 *            接口为 handler 的处理器
	 */
	public static void addHanlder(Handler handler) {
		for (String key : cache.keySet()) { // 每个都加上
			cache.get(key).getLogger().addHandler(handler);
		}
	}

	/**
	 * 打印一个日志
	 * 
	 * @param msg
	 *            日志信息
	 */
	public void info(String msg) {
		logger.logp(Level.INFO, packageName, getMethodName(), msg);
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
		logger.logp(Level.INFO, packageName, getMethodName(), tpl, params);

	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param msg
	 *            警告信息
	 */
	public void warning(String msg) {
		logger.logp(Level.WARNING, packageName, getMethodName(), msg);
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
		logger.logp(Level.WARNING, packageName.trim(), getMethodName(), tpl, params);
	}

	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param ex
	 *            任意异常信息
	 */
	public void warning(Throwable ex) {
		logger.logp(Level.WARNING, packageName.trim(), getMethodName(), ex.getMessage(), ex);
	}
	
	/**
	 * 打印一个日志（警告级别）
	 * 
	 * @param msg
	 *            警告信息
	 * @param ex
	 *            任意异常信息
	 */
	public void warning(String msg, Throwable ex) {
		logger.logp(Level.WARNING, packageName.trim(), getMethodName(), msg, ex);
	}

	/**
	 * 获取所在的方法，调用时候
	 * 
	 * @return 方法名称
	 */
	private String getMethodName() {
		StackTraceElement frame = null;
		
		// get thread by class name
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			String clzName = ste.getClassName();
			
			if (ste.isNativeMethod() || clzName.equals(Thread.class.getName()) || clzName.equals(this.getClass().getName()))
				 continue;  
	            
            if (clzName.equals(packageName)) {
            	frame = ste;// 会有两个？
                break;
            }
        }
//		
//		String location = "类名："+frame.getClassName() + "\n函数名：" + frame.getMethodName()  
//			        + "\n文件名：" + frame.getFileName() + "\n行号："  
//			        + frame.getLineNumber() + "";  
//			        System.out.println(location);   
		
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
