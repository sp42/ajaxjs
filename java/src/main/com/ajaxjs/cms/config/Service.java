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
package com.ajaxjs.cms.config;

import java.io.IOException;
import java.util.Map;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.IO.text;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.app.App;
import com.ajaxjs.json.Json;
import com.ajaxjs.json.Rhino;

/**
 * JSON 配置保存器
 * @author Frank Cheung
 *
 */
public class Service extends Rhino {
	private static final LogHelper LOGGER = LogHelper.getLog(Service.class);

	/**
	 * 
	 * @param request
	 * @throws Exception
	 */
	void save(String jsonFileFullPath, Map<String, String> map) throws Exception {
		try { // 加载 JSON 并且移除 jsonFile
			load(jsonFileFullPath);
		} catch (Exception e) {
			LOGGER.warning("读取{0}失败！", jsonFileFullPath);
		}
		map.remove("jsonFile");// 不需要路径
		
		// 可以json.str() 的 变量名
		String topVarName = map.get("topVarName");
		if(StringUtil.isEmptyString(topVarName))throw new IllegalArgumentException("没有  topVarName 参数！");
		map.remove("topVarName");
		
		save(map);
		
		String JSON_as_String = eval("JSON.stringify(" + topVarName + ");", String.class);
		
		if (JSON_as_String != null) {
			String fileBody = topVarName + " = " + JSON_as_String + ";";

			fileBody = Json.format(fileBody);
//			LOGGER.info(fileBody);
//			LOGGER.info(jsonFileFullPath);
			try {
				text.save2file(jsonFileFullPath, fileBody);
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("更新配置失败，不能保存配置！");
			}
			
			App.updateConfig();
		} else {
			// 更新配置失败，不能序列化配置！
		}
	}
	
	/**
	 * 写入内存，覆盖
	 * 
	 * @param hash
	 */
	private void save(Map<String, String> hash) {
		String jsCode = "";
		for (String key : hash.keySet()) {
			jsCode = key + " = '" + hash.get(key) + "';"; // 全部保存为 String。TODO 支持其他类型

			LOGGER.info("代码 {0} 写入内存中，覆盖原有配置", jsCode);
			
			App.jsRuntime.eval(jsCode); // 两个 js runtime
			eval(jsCode);
		}
	}
}
