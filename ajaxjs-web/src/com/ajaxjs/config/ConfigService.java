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

import java.util.List;
import java.util.Map;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.js.json.JSONParser;
import com.ajaxjs.util.io.FileUtil;

/**
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class ConfigService extends Config {
	private List<Map<String, Object>> list;
	
	private static final long serialVersionUID = 1L;

	/**
	 * 加载配置
	 */
	public void load() {
		setJsonStr(FileUtil.openAsText(getJsonPath()));
		Map<String, Object> map = JSONParser.parseMap(getJsonStr());
		putAll(map);

		setLoaded(true);
	}
	
	public void loadList() {
		setJsonStr(FileUtil.openAsText(getJsonPath()));
		setList(JSONParser.parseList(getJsonStr()));

		setLoaded(true);
	}

	/**
	 * 保存配置
	 */
	public void save() {
		String jsonStr = JsonHelper.stringifyMap(this);
		setJsonStr(jsonStr);
		
		// 保存文件
		new FileUtil().setFilePath(getJsonPath()).setContent(jsonStr).save();
	}
	/**
	 * @return the list
	 */
	public List<Map<String, Object>> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
}
