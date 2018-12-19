package com.ajaxjs.cms.domain;

import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseBean;

public class DomainEntity extends BaseModel implements IBaseBean {
	private static final long serialVersionUID = 4485536546231872045L;

	Map<String, Object> rawData;

	private Integer catelog;

	public Integer getCatelog() {
		return catelog;
	}

	public void setCatelog(Integer catelog) {
		this.catelog = catelog;
	}

	public String getCatelogName() {
		return catelogName;
	}

	public void setCatelogName(String catelogName) {
		this.catelogName = catelogName;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	private String catelogName;

	private String intro;

	private Integer status;
}
