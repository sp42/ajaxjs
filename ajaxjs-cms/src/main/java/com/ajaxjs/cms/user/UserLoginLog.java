package com.ajaxjs.cms.user;

import com.ajaxjs.framework.BaseModel;

public class UserLoginLog extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户 id
	 */
	private Long userId;
	
	private String userName;
	
	/**
	 * 设置用户 id
	 
	 * @param userId  
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * 获取用户 id
	 
	 * @return 用户 id
	 */	
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * 数据字典：第三方类型
	 */
	private Integer loginType;
	
	/**
	 * 设置数据字典：第三方类型
	 
	 * @param loginType  
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}