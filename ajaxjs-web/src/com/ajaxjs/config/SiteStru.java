/**
 * Copyright Frank Cheung frank@ajaxjs.com
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

import java.util.ArrayList;
import java.util.Map;

/**
 * 网站的结构配置
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class SiteStru extends ArrayList<Map<String, Object>> {
	private static final long serialVersionUID = 1737463552429455684L;

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
	 * 当前节点
	 */
	private Map<String, Object> currentNode;

	public Map<String, Object> getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Map<String, Object> currentNode) {
		this.currentNode = currentNode;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}
}