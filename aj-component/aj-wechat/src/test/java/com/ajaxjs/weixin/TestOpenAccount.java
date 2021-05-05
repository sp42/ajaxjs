package com.ajaxjs.weixin;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.weixin.open_account.TokenMgr;

public class TestOpenAccount {		
	@Test
	public void testGetAccessToken2() {
		ConfigService.load("D:\\project\\leidong\\WebContent\\META-INF\\site_config.json");
		
		String appId = ConfigService.getValueAsString("wx_open.appId");
		String appSecret = ConfigService.getValueAsString("wx_open.appSecret");
		
		TokenMgr s = new TokenMgr(appId, appSecret);
		System.out.println(s.getToken());
		assertNotNull(s.getToken());
		
		System.out.println(s.getTicket());
		assertNotNull(s.getTicket());
	}	

}
 