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

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
// import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.Init;
import com.ajaxjs.js.JsEngineWrapper;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

/**
 * 加入 Node。每次请求都会调用，所以要规避静态资源的
 * @author frank
 *
 */
// @WebListener 
public class NodeListener implements ServletRequestListener {	
	private static final LogHelper LOGGER = LogHelper.getLog(NodeListener.class);
	
	/**
	 * 是否已经加载 site_stru.js
	 */
	public static boolean isInited;
	
	private static final String regexp = "\\.jpg|\\.gif|\\.png|\\.icon|\\.htm|\\.css|\\.js[^p]";
	
	@Override
	public void requestInitialized(ServletRequestEvent e) {
		HttpServletRequest request = (HttpServletRequest) e.getServletRequest();

		String uri = request.getRequestURI();
		
		if(StringUtil.regMatch(regexp, uri) != null) // 忽略静态资源
			return;
		
		if(!isInited) {
			if(!InitConfig.allConfig.isLoaded()) {
				LOGGER.warning("系统配置未加载，该服务依赖 系统配置");
				return;
			}
			
			LOGGER.info("初始化 NodeListener，加载网址树状结构中" + Init.ConsoleDiver);
			
			JsEngineWrapper js = new JsEngineWrapper(InitConfig.allConfig.getEngine());
			js.load(Init.srcFolder + "site_stru.js"); // 加载 Web 目录文件
			js.eval("bf.AppStru.init();");
			
			isInited = true;
			
			LOGGER.info(Init.ConsoleDiver + System.getProperty("line.separator") + "加载 site_stru.js 成功" + Init.ConsoleDiver);
		}
		
		request.setAttribute("PageNode", new NodeProcessor(request.getContextPath(), uri));
	}

	@Override
	public void requestDestroyed(ServletRequestEvent e) {
	}
}
