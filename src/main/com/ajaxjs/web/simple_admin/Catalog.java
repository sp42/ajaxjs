package com.ajaxjs.web.simple_admin;

import com.ajaxjs.framework.BaseModel;

public class Catalog extends BaseModel {

	private static final long serialVersionUID = 1240063407205735949L;
	
	private Integer parentId;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
