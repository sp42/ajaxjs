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

import com.ajaxjs.Init;
import com.ajaxjs.js.JsonStruTraveler;

/**
 * 配置管理器，单例
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class ConfigMgr {
	private static ConfigService config;
	private static ConfigService stru;

	public static boolean load() {
		String jsonPath = Init.srcFolder + "site_config.json";
		config = new ConfigService();
		config.setJsonPath(jsonPath);
		config.load();

		stru = new ConfigService();
		stru.setJsonPath(Init.srcFolder + "site_stru.json");
		stru.loadList();

		List<Map<String, Object>> list = stru.getList();

		System.out.println(new JsonStruTraveler().find("menu/menu-1", list));

		return true;
	}
}
