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

import javax.script.ScriptException;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.json.JsonHelper;

/**
 * 加入 Node。每次请求都会调用，所以要规避静态资源的
 * @author frank
 *
 */
public class NodeListener implements ServletRequestListener {	
	/**
	 * 是否已经加载 site_stru.js
	 */
	public static boolean isInited;
	
	@Override
	public void requestInitialized(ServletRequestEvent e) {
		HttpServletRequest request = (HttpServletRequest) e.getServletRequest();
		
		if(!ConfigListener.isJSON_Config_loaded) {
			System.err.println(" 系统配置未加载，该服务依赖 系统配置");
			return;
		}

		String uri = request.getRequestURI();
		if(StringUtil.regMatch("\\.jpg|\\.gif|\\.png|\\.icon|\\.htm|\\.css|\\.js[^p]", uri) != null) {
			return;
		}
		if(!isInited) {
			System.out.println("初始化 NodeListener");
			new JsonHelper(ConfigListener.jsRuntime).load(Init.srcFolder + "site_stru.js"); // 加载 Web 目录文件

			try {
				ConfigListener.jsRuntime.eval("bf.AppStru.init();");
			} catch (ScriptException e1) {
				e1.printStackTrace();
				return;
			}
			
			isInited = true;
			System.out.println("---------------------------------" + System.getProperty("line.separator") + "加载 site_stru.js 成功" + Init.ConsoleDiver);
		}
		
		request.setAttribute("PageNode", new NodeProcessor(request.getContextPath(), uri));
	}

	@Override
	public void requestDestroyed(ServletRequestEvent e) {
	}
	
}
