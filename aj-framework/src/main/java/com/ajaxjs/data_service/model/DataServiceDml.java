package com.ajaxjs.data_service.model;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * DML（data manipulation language）是数据操纵语言 这里就是每个 SQL 操作的配置，最小单位的颗粒度
 */
public class DataServiceDml extends HashMap<String, Object> {
	private static final long serialVersionUID = -5282291095726784463L;

	/**
	 * 配置持久化为 JSON，这是反序列化的操作，变为特定的类型更符合语义
	 *
	 * @param map
	 */
	public DataServiceDml(Map<String, Object> map) {
		super(map);
	}

	/**
	 * 是否启用
	 *
	 * @return
	 */
	public boolean isEnable() {
		return (boolean) get("enable");
	}

	/**
	 * 获取目录
	 *
	 * @return
	 */
	public String getDir() {
		return (String) get("dir");
	}

	/**
	 * 获取 SQL
	 *
	 * @return
	 */
	public String getSql() {
		return (String) get("sql");
	}

	/**
	 * 是否支持 SQL WHERE 条件查询
	 * 
	 * @return
	 */
	public boolean isQuerySearch() {
		return is("isQuerySearch");
	}

	public boolean isAddUuid() {
		return is("addUuid");
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isCreateOrSave() {
		return is("createOrSave");
	}

	/**
	 * 是否支持 创建修改时间
	 * 
	 * @return
	 */
	public boolean isAutoDate() {
		return is("autoDate");
	}

	/**
	 * 是否更新修改日期
	 * 
	 * @return
	 */
	public boolean isUpdateDate() {
		return is("isUpdateDate");
	}

	/**
	 * 是否物理删除
	 *
	 * @return
	 */
	public boolean isPhysicallyDelete() {
		return is("isPhysicallyDelete");
	}

	private boolean is(String key) {
		return get(key) != null && (boolean) get(key);
	}

	/**
	 * GET/POST...除了这种区分，还应允许有下级的分类，例如 GET 下可分为 info/list
	 */
	private String type;

	/**
	 * 当 isEmbed = false 时需要使用多个数据源
	 */
	private DataSource dataSource;

	private DataServiceEntity tableInfo;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataServiceEntity getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(DataServiceEntity tableInfo) {
		this.tableInfo = tableInfo;
	}
}
