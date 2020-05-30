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
package com.ajaxjs.framework.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.script.ScriptException;

import com.ajaxjs.Version;
import com.ajaxjs.jsonparser.JsEngineWrapper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.IoHelper;
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
	public static Config CONFIG;

	/**
	 * 所有的配置保存在这个 config 中（扁平化处理过的）
	 */
	public static Map<String, Object> FLAT_CONFIG;

	/**
	 * 加载 JSON 配置
	 * 
	 * @param jsonPath JSON 配置文件所在路径
	 */
	public static void load(String jsonPath) {
		if (!new File(jsonPath).exists()) {
			LOGGER.info("没有项目配置文件");
			return;
		}

		CONFIG = new Config();
		CONFIG.setJsonPath(jsonPath);
		CONFIG.setJsonStr(FileHelper.openAsText(jsonPath));
		CONFIG.putAll(JsonHelper.parseMap(CONFIG.getJsonStr()));
		CONFIG.setLoaded(true);

		FLAT_CONFIG = ListMap.flatMap(CONFIG);

		if (getValueAsBool("isForceProductEnv")) {
			LOGGER.infoGreen("强制为生产环境模式 isDebug=false");
			Version.isDebug = false;
		}

		LOGGER.infoGreen("加载 " + getValueAsString("clientShortName") + " 项目配置成功！All config loaded.");
	}

	/**
	 * 保存 JSON 配置
	 */
	public static void save() {
		String jsonStr = JsonHelper.toJson(CONFIG);
		CONFIG.setJsonStr(jsonStr);
		FileHelper.saveText(CONFIG.getJsonPath(), jsonStr);
	}

	/**
	 * 内部的获取方法
	 * 
	 * @param <T>         配置类型
	 * @param key         配置键值
	 * @param isNullValue 当配置为 null 时返回的值，相当于“默认值”
	 * @param vType       配置类型的引用
	 * @return 配置内容
	 */
	@SuppressWarnings("unchecked")
	private static <T> T get(String key, T isNullValue, Class<T> vType) {
		if (FLAT_CONFIG == null || !CONFIG.isLoaded())
			return isNullValue;

		Object v = FLAT_CONFIG.get(key);

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
	 * 简化版本
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static String get(String key) {
		return getValueAsString(key);
	}

	/**
	 * 简化版本
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static int getInt(String key) {
		return getValueAsInt(key);
	}
	
	/**
	 * 简化版本
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public static boolean getBol(String key) {
		return getValueAsBool(key);
	}

	/**
	 * 扁平化 JSON 的 key
	 * 
	 * @param namespace JSON 命名空间
	 * @return 扁平化 JSON 的 key
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
	 * 获取配置 JSON 说明文件。 该 json 不保存在 ajaxjs-base，因此单独运行 ajaxjs-base getSchemeJson()
	 * 会报错。 ConfigScheme.json 是放在 ajaxjs-framework 中保存的
	 * 
	 * @return 配置 JSON 说明文件
	 */
	public static String getSchemeJson() {
		try (InputStream in = ConfigService.class.getResourceAsStream("ConfigScheme.json");) {

			return IoHelper.byteStream2string(in);
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * load the json config file to the JS Runtime, and let the new values put into
	 * it, finally save this new json file
	 * 
	 * @param map A map that contains all new config
	 */
	public static void loadJSON_in_JS(Map<String, Object> map) {
		JsEngineWrapper js = new JsEngineWrapper();
		js.eval("allConfig = " + FileHelper.openAsText(CONFIG.getJsonPath()));

		map.forEach((k, v) -> {
			String jsKey = transform(k);
			String jsCode = "";

			if (v == null) {
				jsCode = String.format("allConfig%s = null;", jsKey);
			} else {
				// 获取原来的类型，再作适当的类型转换
				String type = null;

				try {
					Object obj = js.getEngine().eval("typeof allConfig" + jsKey);

					if (obj != null)
						type = obj.toString();
				} catch (ScriptException e) {
					// 可能没这个参数在配置里面
				}

				if ("undefined".equals(type) || type == null) {// 原 JSON 没这参数
					js.eval(findNode);
					js.eval("SCHEME_JSON = " + getSchemeJson());

					Object obj = js.eval(String.format("findNode(SCHEME_JSON, '%s'.split('.'))['type']", k));

					if (obj != null)
						type = obj.toString();
				}

				switch (type) {
				case "string":
					jsCode = String.format("allConfig%s = '%s';", jsKey, v);
					break;
				case "number":
				case "boolean":
				case "undefined": // 原 JSON 没这参数
					jsCode = String.format("allConfig%s = %s;", jsKey, v);
					break;
				case "object":
					jsCode = String.format("allConfig%s = '%s';", jsKey, v);
					break;
				default:
					LOGGER.info("未处理 js 类型： " + type);
				}
			}

			js.eval(jsCode);
		});

		String json = js.eval("JSON.stringify(allConfig, null, 2);", String.class);
		FileHelper.saveText(CONFIG.getJsonPath(), json);
	}

	/**
	 * 查找节点的 JavaScript 函数
	 */
	// @formatter:off
	private final static String findNode = 
		"function findNode(obj, queen) {\n" + 
		"			if(!queen.shift) {\n" + 
		"				return null;\n" + 
		"			}\n" + 
		"			var first = queen.shift();\n" + 
		" \n" + 
		"			\n" + 
		"			for(var i in obj) {\n" + 
		"				if(i === first) {\n" + 
		"					var target = obj[i];\n" + 
		"					\n" + 
		"					if(queen.length == 0) {\n" + 
		"						// 找到了\n" + 
		"						return target;\n" + 
		"					} else {\n" + 
		"						return arguments.callee(obj[i], queen);\n" + 
		"					}\n" + 
		"				}\n" + 
		"			}\n" + 
		"		}";
	// @formatter:on
}
