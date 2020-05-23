package com.ajaxjs.sms;

/**
 * 短信内容
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SmsMessage {
	/**
	 * 发送号码
	 */
	private String phoneNo;

	/**
	 * 值
	 */
	private String templateParam;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public boolean isSendOk() {
		return sendOk;
	}

	public void setSendOk(boolean sendOk) {
		this.sendOk = sendOk;
	}

	public String getTemplateParam() {
		return templateParam;
	}

	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
	}

	/**
	 * 短信签名
	 */
	private String signName;

	/**
	 * 模版id
	 */
	private String templateCode;

	/**
	 * 是否发送成功
	 */
	private boolean sendOk;

}
