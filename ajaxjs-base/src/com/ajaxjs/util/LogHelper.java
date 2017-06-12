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
	
	......

	/**
	 * 获取所在的方法，调用时候
	 * 
	 * @return 方法名称
	 */
	private String getMethodName() {
		StackTraceElement ste = null;
		
		// Thread.getCurrentThread().getStackTrace() 暴露了当前线程的运行栈信息
		for (StackTraceElement _ste : Thread.currentThread().getStackTrace()) {
			String clzName = _ste.getClassName();
			
			if (_ste.isNativeMethod() || clzName.equals(Thread.class.getName()) || clzName.equals(getClass().getName()))
				 continue;  // 过滤不要的类
	            
            if (clzName.equals(className)) {
            	ste = _ste;
                break;
            }
        }

		
		if(ste != null) {// 超链接，跳到源码所在行数
			return String.format(".%s(%s:%s)", ste.getMethodName(), ste.getFileName(), ste.getLineNumber());
		}else{
			return null;
		}
	}
    .....
}
