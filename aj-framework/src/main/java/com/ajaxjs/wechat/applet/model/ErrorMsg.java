package com.ajaxjs.wechat.applet.model;

/**
 * 异常信息
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ErrorMsg {
	/**
	 * 错误码
	 */
	private Integer errcode;

	/**
	 * 错误信息
	 */
	private String errmsg;

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
