package com.egdtv.crawler.model;

import com.ajaxjs.framework.model.BaseModel;

public class Catalog extends BaseModel {
	private Integer parentId;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
}