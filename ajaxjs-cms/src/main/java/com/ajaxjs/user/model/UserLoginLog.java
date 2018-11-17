package com.ajaxjs.user.model;

import com.ajaxjs.framework.BaseModel;

public class UserLoginLog extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户 id
	 */
	private Integer userId;
	
	/**
	 * 设置用户 id
	 * @param userId     
	 *            用户 id
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 获取用户 id
	 * @return 用户 id
	 */	
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 数据字典：第三方类型
	 */
	private Integer loginType;
	
	/**
	 * 设置数据字典：第三方类型
	 * @param loginType     
	 *            数据字典：第三方类型
	 */
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	
	/**
	 * 获取数据字典：第三方类型
	 * @return 数据字典：第三方类型
	 */	
	public Integer getLoginType() {
		return loginType;
	}
	
	/**
	 * 登录 ip
	 */
	private String ip;
	
	/**
	 * 设置登录 ip
	 * @param ip     
	 *            登录 ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * 获取登录 ip
	 * @return 登录 ip
	 */	
	public String getIp() {
		return ip;
	}
	
	/**
	 * 登录时间
	 */
	private java.util.Date createDate;
	
	/**
	 * 设置登录时间
	 * @param createDate     
	 *            登录时间
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * 获取登录时间
	 * @return 登录时间
	 */	
	public java.util.Date getCreateDate() {
		return createDate;
	}
	
}

