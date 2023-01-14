package com.ajaxjs.data_service.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;

import java.util.Map;

/**
 * 配置实体，映射数据库里面的记录
 *
 * @author Frank Cheung
 */
public class DataServiceEntity extends BaseModel implements IBaseModel {
	private String tableName;

	private String urlDir;

	private String urlRoot;

	/**
	 * 其他额外的配置信息
	 */
	private String config;

	private Long datasourceId;

	/**
	 * 主键生成策略 1=自增 2=雪花 3=uuid
	 */
	private Integer keyGen;

	/**
	 * key 字段名，value 字段注释
	 */
	private Map<String, String> fields;

	private Map<String, Object> data;

	/**
	 * 字段映射
	 */
	private DataServiceFieldsMapping fieldsMapping;

	public DataServiceFieldsMapping getFieldsMapping() {
		return fieldsMapping;
	}

	public void setFieldsMapping(DataServiceFieldsMapping fieldsMapping) {
		this.fieldsMapping = fieldsMapping;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUrlDir() {
		return urlDir;
	}

	public void setUrlDir(String urlDir) {
		this.urlDir = urlDir;
	}

	public Long getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(Long datasourceId) {
		this.datasourceId = datasourceId;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	private String json;

	public String getUrlRoot() {
		return urlRoot;
	}

	public void setUrlRoot(String urlRoot) {
		this.urlRoot = urlRoot;
	}

	public Integer getKeyGen() {
		return keyGen;
	}

	public void setKeyGen(Integer keyGen) {
		this.keyGen = keyGen;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
