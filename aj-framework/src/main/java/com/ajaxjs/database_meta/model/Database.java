package com.ajaxjs.database_meta.model;

import java.util.List;

import com.ajaxjs.framework.IBaseModel;

/**
 * 库，包含
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class Database implements IBaseModel {
	private String uuid;

	/**
	 * 库名
	 */
	private String name;

	/**
	 * 库里面所有的表名
	 */
	private List<String> tables;

	private List<Table> tableInfo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	public List<Table> getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(List<Table> tableInfo) {
		this.tableInfo = tableInfo;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
