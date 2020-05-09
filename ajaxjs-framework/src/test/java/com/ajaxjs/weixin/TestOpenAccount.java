package com.ajaxjs.weixin;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.weixin.open_account.TokenMgr;
import com.ajaxjs.weixin.open_account.WxWebUtils;

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

//	@Test
	public void testGenerateSignature() {
		 Map<String, String> map = WxWebUtils.generateSignature("http://qq.com", "HoagFKDcsGMVCIY2vOjf9nSGJAqc4xRTTYaX8D3rjxRPy1g5m948YqxgTOjp9zzFud6uwIwZIyQS14Ut-NcHLQ");
		 assertNotNull(map);
	}
}
 