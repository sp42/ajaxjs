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
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ajaxjs.Init;
import com.ajaxjs.js.JsEngineWrapper;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

/**
 * 配置加载器
 * 
 * @author Frank
 *
 */
// @javax.servlet.annotation.WebListener
public class InitConfig implements ServletContextListener {
	private static final LogHelper LOGGER = LogHelper.getLog(InitConfig.class);
	
	private static final ScriptEngine jsEngine = JsEngineWrapper.engineFactory();

	/**
	 * JSON 配置
	 */
	public static final JsonConfig allConfig = new JsonConfig(jsEngine);

	private void init(){
//		LOGGER.info("AJAXJS-webconfig 启动配置中……请稍后");
		
		try {
			allConfig.load(JsonHelper.class, "base.js"); // 加载基础 js
			allConfig.load(InitConfig.class, "JSON_Tree.js");
			allConfig.load(Init.srcFolder + "site_config.js"); // 加载配置文件
			allConfig.setLoaded(true);
			
			updateConfig();
			
			String configJson = JsonHelper.format(JsonHelper.stringifyMap(allConfig.getHash()));
			LOGGER.info("加载配置信息如下：{0}配置加载完毕！", configJson + System.getProperty("line.separator"));
			
		} catch (Throwable e) {
			LOGGER.warning(e, "配置启动失败");
		}
	}
	

	/**
	 * 根据配置名称读取配置值。
	 * 
	 * @param configName
	 * @return
	 */
	public String getAsString(String configName) {
		return allConfig.getHash().get(configName).toString();
	}

	/**
	 * 根据配置名称读取配置值。
	 * 
	 * @param configName
	 * @return
	 */
	public int getAsInnt(String configName) {
		return (int) allConfig.getHash().get(configName);
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		ServletContext cxt = e.getServletContext();
		tomcatVersionDetect(cxt.getServerInfo());
		init();

		cxt.setAttribute("all_config", allConfig.getHash()); // 所有配置保存在这里
	}

	/**
	 * 检测是否 tomcat 以及版本
	 * 
	 * @param serverInfo
	 *            返回如 Tomcat/7
	 */
	private static void tomcatVersionDetect(String serverInfo) {
		String result = StringUtil.regMatch("(?<=Tomcat/)(\\d)", serverInfo);
		
		if (result != null) {
			try {
				if (Integer.parseInt(result) < 7) {
					throw new UnsupportedOperationException("不支持低于 Tomcat 7 以下的版本！");
				}

			} catch (Throwable e) {
				if (e instanceof UnsupportedOperationException) {
					throw e;
				}
				
				// 忽略其他异常，如正则的
				LOGGER.warning(e);
			}
		} else {
			// 不是 tomcat
		}
	}

	/**
	 * 保存全局信息，无论 JSON 配置文件里面嵌套多少层，到这里都扁平化每一条配置 修改了配置之后，更新。即时出现效果。
	 */
	public static void updateConfig() {
		if (!allConfig.isLoaded()) {
			return; // 没有 site_config.js 文件，退出
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> map = allConfig.eval("JSON_Tree.util.flat(bf_Config);", Map.class);

		allConfig.setHash(map);

		// if (map != null) { // 还是要转换一下
		// config = new HashMap<>();
		// for (String key : map.keySet()) {
		// config.put(key, map.get(key));
		// }
		// }
		// context.setAttribute("global_config", config); // 重新保存 config 对象
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
	}
}