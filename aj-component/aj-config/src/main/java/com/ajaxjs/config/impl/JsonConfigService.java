package com.ajaxjs.config.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.inject.Inject;
import javax.script.ScriptException;
import javax.servlet.ServletContext;

import com.ajaxjs.Version;
import com.ajaxjs.config.Config;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.config.GetConfig;
import com.ajaxjs.jsonparser.JsEngineWrapper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.ListMap;

/**
 * 配置的 CRUD
 * 
 * @author BINGO
 *
 */
public class JsonConfigService extends Config implements ConfigService {
	private static final LogHelper LOGGER = LogHelper.getLog(JsonConfigService.class);

	@Override
	public void init(ServletContext ctx) {
		load(ctx.getRealPath("/META-INF/site_config.json"));

		if (!isLoaded())
			ctx.setAttribute("aj_allConfig", getConfig()); // 所有配置保存在这里
	}

	@Inject
	private GetConfig getCfg;

	@Override
	public void load(String cfgPath) {
		if (!new File(cfgPath).exists()) {
			LOGGER.info("没有[{0}]项目配置文件", cfgPath);
			return;
		}

		String jsonStr = FileHelper.openAsText(cfgPath);
		setConfig(JsonHelper.parseMap(jsonStr));
		setFlatConfig(ListMap.flatMap(getConfig()));
//		FLAT_CONFIG = ListMap.flatMap(CONFIG);
		setFilePath(cfgPath);
		setLoaded(true);

		if (getCfg.getBol("isForceProductEnv")) {
			LOGGER.infoGreen("强制为生产环境模式 isDebug=false");
			Version.isDebug = false;
		}

		LOGGER.infoGreen("加载[" + getCfg.get("clientShortName") + "]项目配置成功！All config loaded.");
	}

	@Override
	public String[] loadEdit() {
		String[] arr = new String[2];
		arr[0] = FileHelper.openAsText(getFilePath());
		arr[1] = getSchemeJson();

//		model.put("configJson", FileHelper.openAsText(ConfigService.jsonPath));
//		model.put("schemeJson", ConfigService.getSchemeJson());

		return arr;
	}

	/**
	 * 获取配置 JSON 说明文件。
	 * 
	 * @return 配置 JSON 说明文件
	 */
	private static String getSchemeJson() {
		try (InputStream in = JsonConfigService.class.getResourceAsStream("ConfigScheme.json");) {
			return StreamHelper.byteStream2string(in);
		} catch (IOException e) {
			LOGGER.warning(e);

			return null;
		}
	}

	@Override
	public void saveAllconfig(Map<String, Object> map, ServletContext ctx) {
		LOGGER.info("保存配置并且刷新配置");

		loadJSON_in_JS(map, getFilePath());
		load(getFilePath()); // 刷新配置

		if (ctx.getAttribute("aj_allConfig") != null)
			ctx.setAttribute("aj_allConfig", getConfig());
	}

	/**
	 * load the json config file to the JS Runtime, and let the new values put into
	 * it, finally save this new json file
	 * 
	 * @param map A map that contains all new config
	 */
	private static void loadJSON_in_JS(Map<String, Object> map, String jsonPath) {
		JsEngineWrapper js = new JsEngineWrapper();
		js.eval("allConfig = " + FileHelper.openAsText(jsonPath));

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
					js.eval(FIND_NODE);
					js.eval("SCHEME_JSON = " + getSchemeJson());

					Object obj = js.eval(String.format("findNode(SCHEME_JSON, '%s'.split('.'))['type']", k));

					if (obj != null)
						type = obj.toString();
				}

				switch (type) {
				case "number":
				case "boolean":
				case "undefined": // 原 JSON 没这参数
					jsCode = String.format("allConfig%s = %s;", jsKey, v);
					break;
				case "string":
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
		FileHelper.saveText(jsonPath, json);
	}

	/**
	 * 扁平化 JSON 的 key
	 * 
	 * @param namespace JSON 命名空间
	 * @return 扁平化 JSON 的 key
	 */
	private static String transform(String namespace) {
		String[] arr = namespace.split("\\.");

		if (arr.length < 1)
			return null;

		String[] arr2 = new String[arr.length];

		for (int i = 0; i < arr.length; i++)
			arr2[i] = "[\"" + arr[i] + "\"]";

		return String.join("", arr2);
	}
	
	/**
	 * 查找节点的 JavaScript 函数
	 */
	// @formatter:off
	private final static String FIND_NODE = 
		"function findNode(obj, queen) {\n" + 
		"			if(!queen.shift) {\n" + 
		"				return null;\n" + 
		"			}\n" + 
		"			var first = queen.shift();\n" + 
		" \n" + 
		"			for(var i in obj) {\n" + 
		"				if(i === first) {\n" + 
		"					var target = obj[i];\n" + 
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

	/**
	 * 保存 JSON 配置 这个方法好像没用
	 */
	public void save() {
		String jsonStr = JsonHelper.toJson(getConfig());
		FileHelper.saveText(getFilePath(), jsonStr);
	}
}
