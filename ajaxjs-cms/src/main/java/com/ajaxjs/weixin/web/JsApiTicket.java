package com.ajaxjs.weixin.web;

import java.util.Calendar;

public class JsApiTicket {
	private String ticket; // 票据

	private int expiresIn; // 凭证有效时间，单位：秒


	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	private Calendar expiresDate; // 超时时间

	public Calendar getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Calendar expiresDate) {
		this.expiresDate = expiresDate;
	}
}
