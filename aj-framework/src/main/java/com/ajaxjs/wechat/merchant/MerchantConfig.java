package com.ajaxjs.wechat.merchant;

/**
 * 微信支付 商户配置
 * 
 * @author Frank Cheung
 *
 */
public class MerchantConfig {
	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 商户证书序列号
	 */
	private String mchSerialNo;

	/**
	 * V3 密钥
	 */
	private String apiV3Key;

	/**
	 * 商户私钥
	 */
	private String privateKey;

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchSerialNo() {
		return mchSerialNo;
	}

	public void setMchSerialNo(String mchSerialNo) {
		this.mchSerialNo = mchSerialNo;
	}

	public String getApiV3Key() {
		return apiV3Key;
	}

	public void setApiV3Key(String apiV3Key) {
		this.apiV3Key = apiV3Key;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}
