package com.ajaxjs.config;

import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.map.JsonHelper;

public class TestCofnig {
	public static void main(String[] args) {
		JsonHelper.parseMap(FileHelper.openAsText(
				"C:\\sp42\\dev\\eclipse-workspace-new2\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\myblog\\WEB-INF\\classes\\site_config.json"));
	}
}
