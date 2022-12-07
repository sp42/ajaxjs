package com.ajaxjs.database_meta;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ajaxjs.data_service.model.DataSourceInfo;

/**
 * 生成数据库信息的 JSON，用于显示数据库文档
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public abstract class MakeDocController {
	private String jsonPath;
	
	@PostMapping
	public Boolean genJsonFile(@RequestBody DataSourceInfo ds) {
		
		DataBaseQuery.saveToDiskJson(null, getJsonPath());
		return true;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

}
