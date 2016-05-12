package com.ajaxjs.javatools;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class Tools {
	/**
	 * 信息日志
	 * 
	 * @param msg
	 *            日志信息
	 * @param cls
	 *            所在的类
	 */
	public static void info(String msg, Class<?> cls) {
		Logger.getLogger(cls.getName()).info(cls.getName() + "--" + msg);
	}

	/**
	 * 警告日志
	 * 
	 * @param msg
	 *            警告信息
	 * @param cls
	 *            所在的类
	 */
	public static void warning(String msg, Class<?> cls) {
		Logger.getLogger(cls.getName()).warning(cls.getName() + "--" + msg);
	}
	
	/**
	 * 获取所有系统变量 
	 */
    public static void getSysProp(){  
        Properties props = System.getProperties(); 
        for(Entry<Object,Object> res : props.entrySet()){
        	 System.out.println(res.getKey()+"="+res.getValue());
        }  
    } 
    
	/** 
     * Java 执行控制台命令,返回执行的输入结果 
     * e.g:System.out.println(executeCommond("ipconfig"));  
     * @param cmd 
     * @return 
     */  
	public static String executeCommond(String cmd) {
		String ret = null;
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(p != null)
			try (InputStreamReader ins = new InputStreamReader(p.getInputStream())) {
				LineNumberReader input = new LineNumberReader(ins);
				String line;
				while ((line = input.readLine()) != null)
					ret += line + "<br>";
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		return ret;
	}
	
	/**
	 * 开发过程中经常需要获取当前正在执行的方法名
	 * 日志打印工具类的实现都依赖于
	 * @return
	 */
	public static StackTraceElement getCallerInfo() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		StackTraceElement callerElement = elements[3];
		return callerElement;
	}
}
