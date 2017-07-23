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



	public static void main(String[] args) {
		ConfigMgr.load();
		ConfigMgr.config.save();
	}
}
