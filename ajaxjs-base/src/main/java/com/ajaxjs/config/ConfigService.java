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

import java.io.File;
import java.util.Map;

import com.ajaxjs.Version;
import com.ajaxjs.jsonparser.JsEngineWrapper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.ListMap;

/**
 * 以 JSON 为存储格式的配置系统，在 JVM 中以 Map/List 结构保存 该类是单例。
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class ConfigService {
	private static final LogHelper LOGGER = LogHelper.getLog(ConfigService.class);

	/**
	 * 所有的配置保存在这个 config 中
	 */
	public static Config config;

	/**
	 * 所有的配置保存在这个 config 中（扁平化处理过的）
	 */
	public static Map<String, Object> flatConfig;

	/**
	 * 配置 json 文件的路径
	 */
	public static String jsonPath = Version.srcFolder + File.separator + "site_config.json";

	/**
	 * 配置 json 说明文件的路径
	 */
	public static String jsonSchemePath = Version.srcFolder + File.separator + "site_config_scheme.json";

	/**
	 * 加载 JSON 配置（默认路径）
	 */
	public static void load() {
		load(jsonPath);
	}

	/**
	 * 加载 JSON 配置
	 * 
	 * @param jsonPath JSON 配置文件所在路径
	 */
	public static void load(String jsonPath) {
		ConfigService.jsonPath = jsonPath; // 覆盖本地的

		config = new Config();
		config.setJsonPath(jsonPath);
		config.setJsonStr(FileHelper.openAsText(jsonPath));
		config.putAll(JsonHelper.parseMap(config.getJsonStr()));
		config.setLoaded(true);

//		if(config.get("isDebug") != null) 
//			Version.isDebug = (boolean)config.get("isDebug");

		flatConfig = ListMap.flatMap(config);
	}

	public static void main(String[] args) {
		JsonHelper.parseMap(FileHelper.openAsText(
				"C:\\sp42\\dev\\eclipse-workspace-new2\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\myblog\\WEB-INF\\classes\\site_config.json"));
	}

	/**
	 * 保存 JSON 配置
	 */
	public static void save() {
		String jsonStr = JsonHelper.toJson(config);
		config.setJsonStr(jsonStr);
		FileHelper.saveText(config.getJsonPath(), jsonStr);
	}

	/**
	 * 
	 * @param <T>
	 * @param key         配置键值
	 * @param isNullValue
	 * @param vType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T get(String key, T isNullValue, Class<T> vType) {
		if (flatConfig == null || !config.isLoaded())
			return isNullValue;

		Object v = flatConfig.get(key);

		if (v == null) {
			LOGGER.warning("没发现配置 " + key);
			return isNullValue;
		}

		return (T) v;
	}

	/**
	 * 读取配置并转换其为 布尔 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static boolean getValueAsBool(String key) {
		return get(key, false, boolean.class);
	}

	/**
	 * 读取配置并转换其为 int 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static int getValueAsInt(String key) {
		return get(key, 0, int.class);
	}

	/**
	 * 读取配置并转换其为 long 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static long getValueAsLong(String key) {
		return get(key, 0L, long.class);
	}

	/**
	 * 读取配置并转换其为字符串类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static String getValueAsString(String key) {
		return get(key, null, String.class);
	}

	/**
	 * 
	 * @param namespace
	 * @return
	 */
	public static String transform(String namespace) {
		String[] arr = namespace.split("\\.");

		if (arr.length < 1)
			return null;

		String[] arr2 = new String[arr.length];

		for (int i = 0; i < arr.length; i++)
			arr2[i] = "[\"" + arr[i] + "\"]";

		return String.join("", arr2);
	}

	/**
	 * load the json config file to the JS Runtime, and let the new values put into
	 * it, finally save this new json file
	 * 
	 * @param map A map that contains all new config
	 */
	public static void loadJSON_in_JS(Map<String, Object> map) {
		JsEngineWrapper js = new JsEngineWrapper();
		js.eval("allConfig = " + FileHelper.openAsText(ConfigService.jsonPath));

		map.forEach((k, v) -> {
			String jsKey = transform(k);
			String jsCode = "";

			if (v == null) {
				jsCode = String.format("allConfig%s = null;", jsKey);
			} else {
				// 获取原来的类型，再作适当的类型转换
				String type = js.eval("typeof allConfig" + jsKey, String.class);

				switch (type) {
				case "string":
					jsCode = String.format("allConfig%s = '%s';", jsKey, v);
					break;
				case "number":
				case "boolean":
					jsCode = String.format("allConfig%s = %s;", jsKey, v);
					break;
				case "object":
					jsCode = String.format("allConfig%s = '%s';", jsKey, v);
				default:
					LOGGER.info("未处理 js 类型： " + type);
				}
			}

			js.eval(jsCode);
		});

		String json = js.eval("JSON.stringify(allConfig);", String.class);
		FileHelper.saveText(ConfigService.jsonPath, json);
	}

}
