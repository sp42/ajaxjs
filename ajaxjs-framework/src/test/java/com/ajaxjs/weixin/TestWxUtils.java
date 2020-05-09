package com.ajaxjs.weixin;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.weixin.open_account.WxWebUtils;
import com.ajaxjs.weixin.open_account.model.AccessToken;
import com.ajaxjs.weixin.web.model.JsApiTicket;

public class TestWxUtils {
	@Test
	public void testGetAccessToken() {
		ConfigService.load("D:\\project\\leidong\\WebContent\\META-INF\\site_config.json");

		String appId = ConfigService.getValueAsString("wx_open.appId");
		String appSecret = ConfigService.getValueAsString("wx_open.appSecret");
		
		AccessToken accessToken = WxWebUtils.getAccessToken(appId, appSecret);
		assertNotNull(accessToken);
	}
	
//	@Test
	public void testGetJsApiTicket() {
		JsApiTicket jsApiTicket;
//		jsApiTicket = WxUtils.getJsApiTicket("19_EHb0hC-V1ZzfMEuBg_h_UkkJ1_KulTcrYK-iocS5kZVuU3KCeXTDwW3RzIvVErUvSslovcvjizCQeVmDUrtRtijHYFcwROeHkFCT8jogw5dTVp4hSyWK8LlrjldcHNRn-ByHKSIbM_ZP_Y-QQCSaAAACOE");
//		assertNotNull(jsApiTicket);
		jsApiTicket = WxWebUtils.getJsApiTicket("wx6f5129b94baba822", "3bdde43746c46f5b4200060e105dfabd");
		assertNotNull(jsApiTicket);
		// HoagFKDcsGMVCIY2vOjf9nSGJAqc4xRTTYaX8D3rjxRPy1g5m948YqxgTOjp9zzFud6uwIwZIyQS14Ut-NcHLQ
	}

//	@Test
	public void testGenerateSignature() {
		 Map<String, String> map = WxWebUtils.generateSignature("http://qq.com", "HoagFKDcsGMVCIY2vOjf9nSGJAqc4xRTTYaX8D3rjxRPy1g5m948YqxgTOjp9zzFud6uwIwZIyQS14Ut-NcHLQ");
		 assertNotNull(map);
	}
}
 