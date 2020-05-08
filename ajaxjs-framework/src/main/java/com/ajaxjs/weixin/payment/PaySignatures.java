package com.ajaxjs.weixin.payment;

import com.ajaxjs.config.ConfigService;

public final class PaySignatures implements PayConstant {

	/**
	 * 返回商户 id
	 * 
	 * @return
	 */
	public static String getMchId() {
		return ConfigService.getValueAsString("mini_program.MchId");
	}

	/**
	 * 返回商户密钥
	 * 
	 * @return
	 */
	public static String getMchSecretId() {
		return ConfigService.getValueAsString("mini_program.MchSecretId");
	}
}
