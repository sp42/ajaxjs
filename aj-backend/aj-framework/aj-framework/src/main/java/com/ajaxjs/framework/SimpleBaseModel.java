package com.ajaxjs.framework;

import java.util.Date;

/**
 * 基础字段
 *
 */
public abstract class SimpleBaseModel {
	/**
	 * 主键 id，自增
	 */
	public Long id;

	/**
	 * 名称
	 */
	public String name;

	/**
	 * 简介
	 */
	public String content;

	/**
	 * 数据字典：状态
	 */
	public Date stat;

	/**
	 * 扩展 JSON 字段
	 */
	public String extend;

	/**
	 * 创建人名称（可冗余的）
	 */
	public String creator;

	/**
	 * 创建人 id
	 */
	public Date creatorId;

	/**
	 * 创建日期
	 */
	public Date createDate;

	/**
	 * 修改人名称（可冗余的）
	 */
	public String updater;

	/**
	 * 修改人 id
	 */
	public Date updaterId;

	/**
	 * 修改日期
	 */
	public Date updateDate;
}
