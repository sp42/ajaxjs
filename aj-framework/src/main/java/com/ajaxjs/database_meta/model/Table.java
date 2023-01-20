package com.ajaxjs.database_meta.model;

import java.util.List;

import com.ajaxjs.framework.IBaseModel;

/**
 * 简单表信息
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class Table implements IBaseModel {
	private String uuid;

	/**
	 * 表名
	 */
	private String name;

	/**
	 * 
	 */
	private String comment;

	/**
	 * 表 CREATE TABLE SQL
	 */
	private String ddl;

	private List<Column> columns;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDdl() {
		return ddl;
	}

	public void setDdl(String ddl) {
		this.ddl = ddl;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
