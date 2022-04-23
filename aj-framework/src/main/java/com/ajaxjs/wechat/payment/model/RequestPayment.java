package com.ajaxjs.wechat.payment.model;

/**
 * 小程序调起支付 wx.requestPayment() 所需的参数
 * 
 * @author Frank Cheung
 *
 */
public class RequestPayment {
	/**
	 * 时间戳
	 */
	private String timeStamp;

	/**
	 * 随机字符串
	 */
	private String nonceStr;

	/**
	 * 订单详情扩展字符串
	 */
	private String prepayIdPackage;

	/**
	 * 随机字符串
	 */
	private String signType = "RSA";

	/**
	 * 签名
	 */
	private String paySign;

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getPrepayIdPackage() {
		return prepayIdPackage;
	}

	public void setPrepayIdPackage(String prepayIdPackage) {
		this.prepayIdPackage = prepayIdPackage;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
}
