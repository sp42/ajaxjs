package com.ajaxjs.weixin.open_account.model;

/**
 * JS API 凭证
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class JsApiTicket extends BaseModel {
	/**
	 * JS API 凭证
	 */
	private String ticket;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
