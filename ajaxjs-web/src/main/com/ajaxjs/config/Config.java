/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.config;

import java.util.HashMap;

/**
 * 配置 Model
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class Config extends HashMap<String, Object> {
	private static final long serialVersionUID = 93681331885687612L;

	/**
	 * json 文件路径
	 */
	private String jsonPath;

	/**
	 * json 字符串内容
	 */
	private String jsonStr;

	/**
	 * 是否加载成功
	 */
	private boolean isLoaded;

	/**
	 * @return the jsonPath
	 */
	public String getJsonPath() {
		return jsonPath;
	}

	/**
	 * @param jsonPath
	 *            the jsonPath to set
	 */
	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	/**
	 * @return the isLoaded
	 */
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * @param isLoaded
	 *            the isLoaded to set
	 */
	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	/**
	 * @return the jsonStr
	 */
	public String getJsonStr() {
		return jsonStr;
	}

	/**
	 * @param jsonStr
	 *            the jsonStr to set
	 */
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
}
