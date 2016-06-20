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
package com.ajaxjs.app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ajaxjs.json.AbstractJsEngine;
import com.ajaxjs.json.IEngine;
import com.ajaxjs.json.Json;
import com.ajaxjs.json.Rhino;
import com.ajaxjs.json.ToJavaType;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Util;

public class ConfigListener implements ServletContextListener {
	public static Map<String, Object> config;
	
	@Override
	public void contextInitialized(ServletContextEvent e) {
		System.out.println("ConfigListener 配置启动");
		ServletContext cxt = e.getServletContext();
		initLoggerFileHandler(cxt);
		
		if(config == null)config = new HashMap<>();
		cxt.setAttribute("_config", config);
		cxt.setAttribute("bigfoot", cxt.getContextPath() + "/bigfoot");
		config.put("LessUrlProcessor", new LessUrlProcessor());
		
		
		String _isEnableJSON_Config = cxt.getInitParameter("isEnableJSON_Config");
		if (_isEnableJSON_Config == null) {
			isEnableJSON_Config = false; // 默认情况
		} else {
			isEnableJSON_Config = new Boolean(_isEnableJSON_Config);
		}
		
		if(isEnableJSON_Config){
			loadJsonConfig();
		}
		System.out.println("配置启动完毕" + Init.ConsoleDiver);
	}
	
	public static final IEngine jsRuntime = new Rhino();// 主 JS runtime，其他 js 包都导进这里来
	public static boolean isEnableJSON_Config;			// 是否通过 JS 来定义配置文件
	
	/**
	 * 把日志文件保存到 WEB-INF/ 下面
	 */
	private static void initLoggerFileHandler(ServletContext cxt) {
		String loggerFile = cxt.getRealPath("/") + "META-INF" + File.separator + "logger" + File.separator + "log.txt";
		LogHelper.addHanlder(LogHelper.fileHandlerFactory(loggerFile));
	}
	
	/**
	 * 加载配置
	 */
	private static void loadJsonConfig() {
		jsRuntime.eval(AbstractJsEngine.baseJavaScriptCode);
		jsRuntime.load(new String[] {
			Util.getClassFolder_FilePath(Init.class, "JSON_Tree.js"), 
			Init.srcFolder + "site_config.js", // 加载配置文件
		});
		
		// 保存全局信息，无论 JSON 配置文件里面嵌套多少层，到这里都扁平化每一条配置
		config = ((ToJavaType)jsRuntime).eval_return_Map("JSON_Tree.util.flat(bf_Config);");
		
		System.out.println(("加载配置信息如下：" + System.getProperty("line.separator")
				+ Json.format(com.ajaxjs.json.Json.stringify(config))
				+ Init.ConsoleDiver));
	}
	
	/**
	 * 修改了配置之后，更新。即时出现效果。
	 */
	public static void updateConfig() {
		config = ((ToJavaType)jsRuntime).eval_return_Map("JSON_Tree.util.flat(bf_Config);");
//		context.setAttribute("global_config", config); // 重新保存 config 对象
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
	}
}
