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

import java.util.Map;

import com.ajaxjs.Version;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.Value;
import com.ajaxjs.util.collection.JsonStruTraveler;
import com.ajaxjs.util.io.FileUtil;

/**
 * 以 JSON 为存储格式的配置系统，在 JVM 中以 Map/List 结构保存 该类是单例。
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class ConfigService {
	/**
	 * 所有的配置保存在这个 congfig 中
	 */
	public static Config config;
	/**
	 * 所有的配置保存在这个 congfig 中（扁平化处理过的）
	 */
	public static Map<String, Object> flatConfig;

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

		flatConfig = JsonStruTraveler.flatMap(config);
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

	/**
	 * 读取配置并转换其为 布尔 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key
	 *            配置键值
	 * @return 配置内容
	 */
	public static boolean getValueAsBool(String key) {
		return Value.TypeConvert(flatConfig.get(key), boolean.class);
	}
	
	
	/**
	 * 读取配置并转换其为 int 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key
	 *            配置键值
	 * @return 配置内容
	 */
	public static int getValueAsInt(String key) {
		return Value.TypeConvert(flatConfig.get(key), int.class);
	}

	/**
	 * 读取配置并转换其为字符串类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key
	 *            配置键值
	 * @return 配置内容
	 */
	public static String getValueAsString(String key) {
		return Value.TypeConvert(flatConfig.get(key), String.class);
	}

}
