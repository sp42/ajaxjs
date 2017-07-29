package com.ajaxjs.cms.model;

import com.ajaxjs.framework.model.Entity;

/**
 * 用户留言、反馈
 * @author xinzhang
 *
 */
public class Feedback extends Entity {
	private static final long serialVersionUID = 9134895000164862118L;
	
	/**
	 * 用户联系方式
	 */
	private String contact;
	
	/**
	 * 用户提交时的 ip
	 */
	private String ip;

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
