package com.ajaxjs.jxc.base.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 税率
 */
public class TaxRate extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Integer rate;

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
}