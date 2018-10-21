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
import com.ajaxjs.framework.dao.MockDataSource;
import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.js.JsEngineWrapper;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.js.JsonStruTraveler;
import com.ajaxjs.keyvalue.MappingValue;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 以 JSON 为存储格式的配置系统，在 JVM 中以 Map/List 结构保存 该类是单例。
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class ConfigService {
	private static final LogHelper LOGGER = LogHelper.getLog(SiteStruService.class);

	/**
	 * 所有的配置保存在这个 config 中
	 */
	public static Config config;
	/**
	 * 所有的配置保存在这个 config 中（扁平化处理过的）
	 */
	public static Map<String, Object> flatConfig;

	/**
	 * 获取配置
	 * 
	 * @return 所有的配置
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * 获取扁平化配置
	 * 
	 * @return 扁平化配置
	 */
	public Map<String, Object> getFlatConfig() {
		return flatConfig;
	}

	/**
	 * 配置 json 文件的路径
	 */
	public static String jsonPath = Version.srcFolder + "site_config.json";

	/**
	 * 配置 json 说明文件的路径
	 */
	public static String jsonSchemePath = Version.srcFolder + "site_config_scheme.json";

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
		config.setJsonStr(FileUtil.openAsText(jsonPath));
		config.putAll(JsonHelper.parseMap(config.getJsonStr()));
		config.setLoaded(true);

		flatConfig = JsonStruTraveler.flatMap(config);
	}

	/**
	 * 保存 JSON 配置
	 */
	public static void save() {
		String jsonStr = JsonHelper.stringifyMap(config);
		config.setJsonStr(jsonStr);

		// 保存文件
		new FileUtil().setFilePath(config.getJsonPath()).setContent(jsonStr).save();
	}

	/**
	 * 读取配置并转换其为 布尔 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static boolean getValueAsBool(String key) {
		if (flatConfig == null || !config.isLoaded())
			return false;

		Object v = flatConfig.get(key);

		if (v == null) {
			LOGGER.warning("没发现配置 " + key);
			return false;
		}

		return MappingValue.TypeConvert(flatConfig.get(key), boolean.class);
	}

	/**
	 * 读取配置并转换其为 int 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static int getValueAsInt(String key) {
		if (flatConfig == null || !config.isLoaded())
			return 0;

		// js number 在 java 里面为 double 转换一下
		Object number = flatConfig.get(key);

		if (number == null) {
			LOGGER.warning("没发现配置 " + key);
			return 0;
		}

		if (number instanceof Double)
			number = MappingValue.double2int((Double) number);

		return MappingValue.TypeConvert(number, int.class);
	}

	/**
	 * 读取配置并转换其为 long 类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static long getValueAsLong(String key) {
		if (flatConfig == null || !config.isLoaded())
			return 0L;

		// js number 在 java 里面为 double 转换一下
		Object number = flatConfig.get(key);

		if (number == null) {
			LOGGER.warning("没发现配置 " + key);
			return 0L;
		}

		if (number instanceof Double)
			number = MappingValue.double2long((Double) number);

		return MappingValue.TypeConvert(number, long.class);
	}

	/**
	 * 读取配置并转换其为字符串类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static String getValueAsString(String key) {
		if (flatConfig == null || !config.isLoaded())
			return null;

		Object v = flatConfig.get(key);

		if (v == null) {
			LOGGER.warning("没发现配置 " + key);
			return null;
		}

		return MappingValue.TypeConvert(v, String.class);
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

		for (int i = 0; i < arr.length; i++) {

			arr2[i] = "[\"" + arr[i] + "\"]";
		}

		return StringUtil.stringJoin(arr2, "");
	}

	/**
	 * load the json config file to the JS Runtime, and let the new values put into
	 * it, finally save this new json file
	 * 
	 * @param map A map that contains all new config
	 */
	public static void loadJSON_in_JS(Map<String, Object> map) {
		JsEngineWrapper js = new JsEngineWrapper();
		js.eval("allConfig = " + FileUtil.openAsText(ConfigService.jsonPath));

		for (String key : map.keySet()) {
			String jsKey = transform(key);
			String jsCode = "";

			if (map.get(key) == null) {
				jsCode = String.format("allConfig%s = null;", jsKey);
			} else {
				// 获取原来的类型，再作适当的类型转换
				String type = js.eval("typeof allConfig" + jsKey, String.class);

				switch (type) {
				case "string":
					jsCode = String.format("allConfig%s = '%s';", jsKey, map.get(key));
					break;
				case "number":
				case "boolean":
					jsCode = String.format("allConfig%s = %s;", jsKey, map.get(key));
					break;
				case "object":
					jsCode = String.format("allConfig%s = '%s';", jsKey, map.get(key));
				default:
					LOGGER.info("未处理 js 类型： " + type);
				}
			}

			js.eval(jsCode);
		}

		String json = js.eval("JSON.stringify(allConfig);", String.class);
		FileUtil.save(ConfigService.jsonPath, json);
	}

	/**
	 * 单测专用的初始化数据库连接方法
	 * 
	 * @param configFile JSON 配置文件
	 */
	public static void initTestConnection(String configFile) {
		ConfigService.load(configFile);
		JdbcConnection.setConnection(MockDataSource.getTestMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"), ConfigService.getValueAsString("testServer.mysql.user"),
				ConfigService.getValueAsString("testServer.mysql.password")));
		
		DataBaseFilter.isAutoClose = false;

	}
}
