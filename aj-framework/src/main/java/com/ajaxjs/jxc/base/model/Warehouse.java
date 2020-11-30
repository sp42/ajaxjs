package com.ajaxjs.jxc.base.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 仓库
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Warehouse extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long addressId;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
}
