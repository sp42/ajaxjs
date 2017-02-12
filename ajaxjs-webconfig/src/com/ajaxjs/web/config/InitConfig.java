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
package com.ajaxjs.web.config;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ajaxjs.Init;
import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.json.JSON;
import com.ajaxjs.util.json.JsLib;
import com.ajaxjs.util.json.JsonHelper;

/**
 * 配置加载器
 * @author Frank
 *
 */
@WebListener 
public class InitConfig implements ServletContextListener {
	/**
	 * JSON 配置
	 */
	public static final JsonConfig allConfig = new JsonConfig();
	
	/**
	 * 保存配置的 引擎
	 */
	public static final ScriptEngine jsRuntime = JSON.engineFactory();
	
	static {
		System.out.println("Ajaxjs-webconfig 配置启动");
		// 加载基础 js
		String code = new StreamUtil().setIn(InitConfig.class.getResourceAsStream("JSON_Tree.js")).byteStream2stringStream().close().getContent();
		
		try {
			jsRuntime.eval(JsLib.baseJavaScriptCode);
			jsRuntime.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		loadJsonConfig();

		System.out.println("配置启动完毕" + Init.ConsoleDiver);
	}
	
	/**
	 * 根据配置名称读取配置值。
	 * @param configName
	 * @return
	 */
	public String getAsString(String configName) {
		return allConfig.getHash().get(configName).toString();
	}
	
	/**
	 * 根据配置名称读取配置值。
	 * @param configName
	 * @return
	 */
	public int getAsInnt(String configName) {
		return (int)allConfig.getHash().get(configName);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent e) {
		ServletContext cxt = e.getServletContext();
		
		cxt.setAttribute("all_config", allConfig.getHash()); // 所有配置保存在这里
//		cxt.setAttribute("PageUtil", new PageUtil()); // 一些页面实用的函数
		
//		eclipse 不能删除
		// 把日志文件保存到 META-INF/ 下面
//		String loggerFile = cxt.getRealPath("/") + "META-INF" + File.separator + "logger" + File.separator + "log.txt";
//		LogHelper.addHanlder(LogHelper.fileHandlerFactory(loggerFile));
	}
	
	/**
	 * 加载配置
	 */
	private static void loadJsonConfig() {
		allConfig.setJsonPath(Init.srcFolder + "site_config.js");
		allConfig.setJsRuntime(jsRuntime);
		allConfig.loadJSON(); // 加载配置文件
		
		if(allConfig.isLoaded()) {
			updateConfig();
			System.out.println(("加载配置信息如下：" + System.getProperty("line.separator") + JsonHelper.format(JsonHelper.stringify(allConfig.getHash())) + Init.ConsoleDiver));
		}
	}
	
	/**
	 * 保存全局信息，无论 JSON 配置文件里面嵌套多少层，到这里都扁平化每一条配置
	 * 修改了配置之后，更新。即时出现效果。
	 */
	@SuppressWarnings("unchecked")
	public static void updateConfig() {
		if(!allConfig.isLoaded()) {
			return; // 没有 site_config.js 文件，退出
		}
		
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
	public void contextDestroyed(ServletContextEvent e) {}
}