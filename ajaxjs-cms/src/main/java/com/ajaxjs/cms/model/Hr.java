package com.ajaxjs.cms.model;

import com.ajaxjs.framework.BaseModel;

public class Hr extends BaseModel {
	private static final long serialVersionUID = 8487052415935713977L;

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private String content;

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	private Integer status;

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	private Long uid;

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getUid() {
		return uid;
	}

	private Integer createByUser;

	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}

	public Integer getCreateByUser() {
		return createByUser;
	}

	private java.util.Date createDate;

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	public java.util.Date getCreateDate() {
		return createDate;
	}

	private java.util.Date updateDate;

	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}

	public java.util.Date getUpdateDate() {
		return updateDate;
	}

	private Integer deleted;

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getDeleted() {
		return deleted;
	}

	private Integer catalog;

	public void setCatalog(Integer catalog) {
		this.catalog = catalog;
	}

	public Integer getCatalog() {
		return catalog;
	}
	
	private Long pid;
	
	public void setPid(Long pid){
		this.pid=pid;
	}
	public Long getPid(){
		return pid;
	}
}
