package com.ajaxjs.weixin.open_account.model;

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
