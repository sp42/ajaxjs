package com.ajaxjs.config;

import java.util.List;
import java.util.Map;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.js.json.JSONParser;
import com.ajaxjs.util.io.FileUtil;

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
