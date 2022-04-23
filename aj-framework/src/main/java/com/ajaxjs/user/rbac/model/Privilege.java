package com.ajaxjs.user.rbac.model;

import com.ajaxjs.framework.BaseModel;

public class Privilege extends BaseModel {
	/**
	 * 父 id
	 */
	private Long parentId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
