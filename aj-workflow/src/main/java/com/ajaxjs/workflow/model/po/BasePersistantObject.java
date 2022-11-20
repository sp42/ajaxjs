package com.ajaxjs.workflow.model.po;

import java.util.Date;

import com.ajaxjs.framework.IBaseModel;

/**
 * 持久对象
 * 
 * PO 就是数据库中的记录，一个 PO 的数据结构对应着库中表的结构，表中的一条记录就是一个 PO 对象
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public abstract class BasePersistantObject implements IBaseModel {
	private Long id;

	/**
	 * 唯一 id
	 */
	private Long uid;

	/**
	 * 数据字典：状态
	 */
	private Integer stat;

//	private Status stat;
//
//	public Status getStat() {
//		return stat;
//	}
//
//	public void setStat(Status stat) {
//		this.stat = stat;
//	}

	private String name;

	private String content;

	/**
	 * 创建者 id
	 */
	private Long creator;

	/**
	 * 上一次更新人员 id
	 */
	private Long updator;

	/**
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 修改日期
	 */
	private Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

}
