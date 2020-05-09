package com.ajaxjs.weixin.web.model;

import java.util.Calendar;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class JsApiTicket {
	private String jsApiTicket; // 票据

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
