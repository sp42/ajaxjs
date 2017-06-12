package com.ajaxjs.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFileHandler extends FileHandler {
	public LogFileHandler(String logFilePath, int max_size, int max_number) throws SecurityException, IOException  {
		super(logFilePath, 1024 * max_size, max_number, true);
		setLevel(Level.WARNING);// WARNING 级别的或以上才记录
		setFormatter(myFormatter);
	}
	
	/**
	 * 日志格式
	 */
	private final static Formatter myFormatter = new Formatter() {
		@Override
		public String format(LogRecord record) {
			return String.format(tpl, new Date(), record.getLevel(), record.getLoggerName(),
					record.getSourceMethodName(), record.getMessage());
		}
	};
	
	private static final String tpl = "\n%s %s : %s.%s  %s";
	private static int max_size = 1; 		// 最大文件大小，1 = 一兆
	private static int max_number = 10;
	
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
			logHandler = new LogFileHandler(logFilePath,  max_size, max_number);
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
}
