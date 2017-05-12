package com.ajaxjs.cms.model;

import com.ajaxjs.framework.model.BaseModel;

/**
 * 类别
 * @author xinzhang
 *
 */
public class Catalog extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 父亲 id
	 */
	private Integer parentId;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
}
