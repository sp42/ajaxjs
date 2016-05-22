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
package com.ajaxjs.javascript;

import javax.servlet.jsp.PageContext;

import com.ajaxjs.app.App;
import com.ajaxjs.json.IEngine;

public class Cross{
	
	public static IEngine runtime; // 单例
	static{
		runtime = App.jsRuntime;
		runtime.load(Cross.class, "Js.js");

//		try {
//			runtime.call(View, "setObj", "sss", "sdsd");
//			runtime.eval("self = this;");
//			
//			runtime.eval("function write(str){" +
//						"pageContext.getOut().println(str);" +
//					"}");
//			runtime.eval("function setObj(globalVarName, obj){" +
//				"self[globalVarName] = obj;" +
//			"}");
//		} catch (ScriptException e) {
//			if(com.ajaxjs.core.Util.isEnableConsoleLog)e.printStackTrace();
//		}
	}

	public static void init(PageContext pageContext){
		Object View = runtime.get("View");
		
		Cross.runtime.call("setObj", Object.class, View, "pageContext", pageContext);
	}
}
