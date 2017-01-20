package com.ajaxjs.framework.user;

import com.ajaxjs.framework.model.BaseModel;

public class User extends BaseModel {
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
