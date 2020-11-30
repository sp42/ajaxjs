package com.ajaxjs.jxc.base.model;

import javax.validation.constraints.NotNull;

import com.ajaxjs.framework.BaseModel;

/**
 * 银行账号
 */
public class Bank extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long owner;
	
	@NotNull
	private String account;

	private String branch;

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}
}