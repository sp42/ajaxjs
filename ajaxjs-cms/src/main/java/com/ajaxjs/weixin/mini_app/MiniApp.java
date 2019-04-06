package com.ajaxjs.weixin.mini_app;

import com.ajaxjs.config.ConfigService;

/**
 * 小程序工具方法
 * 
 * @author Frank Cheung
 *
 */
public class MiniApp {
	/**
	 * 返回 App Id
	 * 
	 * @return
	 */
	public static String getAppId() {
		return ConfigService.getValueAsString("mini_program.AppId");
	}

	/**
	 * 返回 App 密钥
	 * 
	 * @return
	 */
	public static String getAppSecret() {
		return ConfigService.getValueAsString("mini_program.AppSecret");
	}

	/**
	 * 获取 Session Key
	 * 
	 * @return 加密的 Session Key
	 */
	public static String getSessionKey() {
		return ConfigService.getValueAsString("mini_program.SessionId_AesKey");
	}
}
