package com.ajaxjs.database_meta.model;

/**
 * 列信息
 * 
 * @author frank
 *
 */
public class Column {
	/**
	 * 类型
	 */
	private String name;

	/**
	 * 数据长度
	 */
	private Integer length;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 空值
	 */
	private String nullValue;

	/**
	 * 注释
	 */
	private String comment;

	/**
	 * 默认值
	 */
	private String defaultValue;

	/**
	 * 是否主键
	 */
	private Boolean isKey;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Boolean getIsKey() {
		return isKey;
	}

	public void setIsKey(Boolean isKey) {
		this.isKey = isKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
