package com.ajaxjs.user.oauth;

import org.junit.Test;

public class TestWeibo {

	@Test
	public void weibo() {
		WeiboOauthController c = new WeiboOauthController();
		c.callback("ca48a644b89e990efb859765ec66d44c", "register");
	}
}
