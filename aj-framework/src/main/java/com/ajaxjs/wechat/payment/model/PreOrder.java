package com.ajaxjs.wechat.payment.model;

/**
 * JSAPI下单 请求对象
 * 
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_1.shtml
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class PreOrder {
	/**
	 * 应用 ID 
	 */
	private String appid;
	
	/**
	 * 直连商户号
	 */
	private String mchid;
	
	/**
	 * 商品描述
	 */
	private String description;
	
	/**
	 * 商户订单号
	 */
	private String out_trade_no;
	
	/**
	 * 通知地址
	 */
	private String notify_url;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	
}
