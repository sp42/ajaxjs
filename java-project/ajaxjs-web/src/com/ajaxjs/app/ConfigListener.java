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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ajaxjs.framework.dao.MyBatis;
import com.ajaxjs.util.FileUtil;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.json.JSON;
import com.ajaxjs.util.json.JsLib;
import com.ajaxjs.util.json.JsonHelper;
import com.ajaxjs.web.PageUtil;
//import com.egdtv.crawler.service.Common;

public class ConfigListener implements ServletContextListener {
	/**
	 * 所有配置保存在这里
	 */
	public static Map<String, Object> config;
	
	@Override
	public void contextInitialized(ServletContextEvent e) {
		System.out.println("ConfigListener 配置启动");
		ServletContext cxt = e.getServletContext();
		initLoggerFileHandler(cxt);

		String _isEnableJSON_Config = cxt.getInitParameter("isEnableJSON_Config");
		if (_isEnableJSON_Config == null) {
			isEnableJSON_Config = false; // 默认情况
		} else {
			isEnableJSON_Config = new Boolean(_isEnableJSON_Config);
		}

		if (isEnableJSON_Config) {
			loadJsonConfig();
		}

		if (config == null)
			config = new HashMap<>();
		cxt.setAttribute("_config", config);
		cxt.setAttribute("bigfoot", cxt.getContextPath() + "/bigfoot");
		cxt.setAttribute("PageUtil", new PageUtil()); // 一些页面实用的函数
		config.put("LessUrlProcessor", new LessUrlProcessor());
		
		// 初始化数据库连接
		if(cxt.getInitParameter("DATABASE_TYPE") != null) {
			MyBatis.db_context_path = cxt.getInitParameter("DATABASE_TYPE");
			MyBatis.init();
		}
		
		
//		Common.load();
		
//		Event event = Reflect.newInstance("com.egdtv.crawler.App", Event.class);
//		event.onConfigLoaded();
		
//		boolean isUsingMySQL = false;// 是否使用 MySql

//		if (ConfigListener.config.containsKey("app_isUsingMySQL"))
//			isUsingMySQL = (boolean) ConfigListener.config.get("app_isUsingMySQL");
//
//		if (isUsingMySQL) {
//			return Helper.getDataSource("jdbc/mysql_test");
//		} else {
//			String str;
//			if (Init.isDebug)
//				str = Init.isMac ? "" : "jdbc/sqlite";
//			else
//				str = "jdbc/sqlite_deploy";
		System.out.println("配置启动完毕" + Init.ConsoleDiver);
	}
	
	public static final ScriptEngine jsRuntime = JSON.engineFatory();// 主 JS runtime，其他 js 包都导进这里来
	public static boolean isEnableJSON_Config;			// 是否通过 JS 来定义配置文件
	public static boolean isJSON_Config_loaded = false;
	
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
		
		String code = null;
		try {
			code = FileUtil.readText(Init.class.getResourceAsStream("JSON_Tree.js"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			jsRuntime.eval(JsLib.baseJavaScriptCode);
			jsRuntime.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
			return;
		}
		
		JsonHelper jh = new JsonHelper(jsRuntime);
		jh.load(new String[] {
//			Util.getClassFolder_FilePath(Init.class, "JSON_Tree.js"), 
			Init.srcFolder + "site_config.js", // 加载配置文件
		});
		
//		try {
//			System.out.println(ConfigListener.jsRuntime.eval("bf.AppStru.getNav();"));
//			
//		} catch (ScriptException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JSON.getMapArray(ConfigListener.jsRuntime, "bf.AppStru.getNav();");
		
		updateConfig();
		
		isJSON_Config_loaded =  true;
		
		System.out.println(("加载配置信息如下：" + System.getProperty("line.separator")
				+ JsonHelper.format(JsonHelper.stringify(config))
				+ Init.ConsoleDiver));
	}
	
	/**
	 * 保存全局信息，无论 JSON 配置文件里面嵌套多少层，到这里都扁平化每一条配置
	 * 修改了配置之后，更新。即时出现效果。
	 */
	@SuppressWarnings("unchecked")
	public static void updateConfig() {
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) jsRuntime.eval("JSON_Tree.util.flat(bf_Config);");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		if (map != null) { // 还是要转换一下
			config = new HashMap<>();
			for (String key : map.keySet()) {
				config.put(key, map.get(key));
			}
		}
		// context.setAttribute("global_config", config); // 重新保存 config 对象
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
	}
}
