package com.ajaxjs.wechat.applet.model;

import com.ajaxjs.framework.ClientAccessFullInfo;

/**
 * 微信小程序服务端配置
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class WeChatAppletConfig extends ClientAccessFullInfo {
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
