package com.ajaxjs.cms.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseBean;

public class Hr extends BaseModel implements IBaseBean {
	private static final long serialVersionUID = 8487052415935713977L;

	private Integer catalog;

	public void setCatalog(Integer catalog) {
		this.catalog = catalog;
	}

	public Integer getCatalog() {
		return catalog;
	}

	private Long pid;

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getPid() {
		return pid;
	}
}
