package com.ajaxjs.web.config;

import java.io.File;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ajaxjs.Init;
import com.ajaxjs.framework.dao.MyBatis;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.json.JSON;
import com.ajaxjs.util.json.JsLib;
import com.ajaxjs.util.json.JsonHelper;

public class InitConfig implements ServletContextListener {
	
	/**
	 * JSON 配置
	 */
	private static final JsonConfig allConfig = new JsonConfig();
	/**
	 * 保存配置的 引擎
	 */
	public static final ScriptEngine jsRuntime = JSON.engineFactory();
	
	@Override
	public void contextInitialized(ServletContextEvent e) {
		System.out.println("Ajaxjs-webconfig 配置启动");
		
		ServletContext cxt = e.getServletContext();
		
		// 加载基础 js
		String code = new StreamUtil().setIn(InitConfig.class.getResourceAsStream("JSON_Tree.js")).byteStream2stringStream().close().getContent();
		
		try {
			jsRuntime.eval(JsLib.baseJavaScriptCode);
			jsRuntime.eval(code);
		} catch (ScriptException se) {
			se.printStackTrace();
			return;
		}
		
		loadJsonConfig();
		
		cxt.setAttribute("_config", allConfig.getHash()); // 所有配置保存在这里
//		cxt.setAttribute("PageUtil", new PageUtil()); // 一些页面实用的函数
		
		// 初始化数据库连接
		if(cxt.getInitParameter("DATABASE_TYPE") != null) {
			MyBatis.db_context_path = cxt.getInitParameter("DATABASE_TYPE");
			
			if(Init.isMac)
				MyBatis.db_context_path += "_mac";
				
			if(!Init.isDebug) { // 部署时读取的配置
				MyBatis.db_context_path += "_deploy";
			}
			
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
		
//		eclipse 不能删除
//		initLoggerFileHandler(cxt);
		
		System.out.println("配置启动完毕" + Init.ConsoleDiver);
	}
	
	/**
	 * 把日志文件保存到 META-INF/ 下面
	 */
	@SuppressWarnings("unused")
	private static void initLoggerFileHandler(ServletContext cxt) {
		String loggerFile = cxt.getRealPath("/") + "META-INF" + File.separator + "logger" + File.separator + "log.txt";
		LogHelper.addHanlder(LogHelper.fileHandlerFactory(loggerFile));
	}
	
	/**
	 * 加载配置
	 */
	private static void loadJsonConfig() {
		allConfig.setJsonPath(Init.srcFolder + "site_config.js");
		allConfig.setJsRuntime(jsRuntime);
		allConfig.loadJSON(); // 加载配置文件
		
		updateConfig();
		
		System.out.println(("加载配置信息如下：" + System.getProperty("line.separator") + JsonHelper.format(JsonHelper.stringify(allConfig.getHash())) + Init.ConsoleDiver));
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
		
		allConfig.setHash(map);
		
//		if (map != null) { // 还是要转换一下
//			config = new HashMap<>();
//			for (String key : map.keySet()) {
//				config.put(key, map.get(key));
//			}
//		}
		// context.setAttribute("global_config", config); // 重新保存 config 对象
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
	}
}