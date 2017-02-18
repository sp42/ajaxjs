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

import com.ajaxjs.js.JsEngineWrapper;


/**
 * 抽象一个单独的 json 配置文件
 * 
 * @author xinzhang
 *
 */
public class JsonConfig extends JsEngineWrapper {
	/**
	 * json 文件路径
	 */
	private String jsonPath;

	/**
	 * 是否加载成功
	 */
	private boolean isLoaded;

	/**
	 * 转换为 java 的 map
	 */
	private Map<String, Object> hash;

	public JsonConfig(ScriptEngine jsengine) {
		super(jsengine);
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public Map<String, Object> getHash() {
		return hash;
	}

	public void setHash(Map<String, Object> hash) {
		this.hash = hash;
	}
	

	/**
	 * 写入内存，覆盖
	 * 
	 * @param hash
	 */
	public void save(Map<String, String> hash) {
		String jsCode = "";

		for (String key : hash.keySet()) {
			jsCode = key + " = '" + hash.get(key) + "';"; // 全部保存为 String。TODO 支持其他类型

			eval(jsCode);
		}
	}
}
