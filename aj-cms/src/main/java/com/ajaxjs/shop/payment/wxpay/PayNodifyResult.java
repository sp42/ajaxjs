package com.ajaxjs.shop.payment.wxpay;

/**
 * 支付之后，验证的结果
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class PayNodifyResult {
	/**
	 * 返回状态码
	 */
	private String return_code;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	/**
	 * 返回信息
	 */
	private String return_msg;
}
