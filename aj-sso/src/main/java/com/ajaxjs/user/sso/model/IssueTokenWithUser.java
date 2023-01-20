package com.ajaxjs.user.sso.model;

import com.ajaxjs.user.User;

/**
 * 带有用户的
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class IssueTokenWithUser extends IssueToken {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
