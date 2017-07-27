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

import com.ajaxjs.Version;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.io.FileUtil;

/**
 * 以 JSON 为存储格式的配置系统，在 JVM 中以 Map/List 结构保存
 * 该类是单例。
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class ConfigService {
	/**
	 * 所有的配置保存在这个 congfig 中
	 */
	public static Config config;

	/**
	 * 配置 json 文件的路径
	 */
	public static String jsonPath = Version.srcFolder + "site_config.json";

	/**
	 * 加载 JSON 配置
	 */
	public static void load() {
		config = new Config();
		config.setJsonPath(jsonPath);
		config.setJsonStr(FileUtil.openAsText(jsonPath));
		config.putAll(JsonHelper.parseMap(config.getJsonStr()));
		config.setLoaded(true);
	}

	/**
	 * 保存 JSON 配置
	 */
	public void save() {
		String jsonStr = JsonHelper.stringifyMap(config);
		config.setJsonStr(jsonStr);

		// 保存文件
		new FileUtil().setFilePath(config.getJsonPath()).setContent(jsonStr).save();
	}

}
