package com.ajaxjs.weixin.open_account.model;

import java.util.Calendar;

/**
 * 腾讯凭证通用的属性
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseModel {
	/**
	 * 错误代码
	 */
	private int errcode;

	/**
	 * 错误信息
	 */
	private String errmsg;

	/**
	 * 凭证有效时间，单位：秒
	 */
	private int expires_in;

	/**
	 * 超时时间，基于 expiresIn 计算
	 */
	private Calendar expiresDate;

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public Calendar getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Calendar expiresDate) {
		this.expiresDate = expiresDate;
	}
}
