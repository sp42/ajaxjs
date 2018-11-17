package com.ajaxjs.user.model;

import com.ajaxjs.framework.BaseModel;

public class UserOauth extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户信息外键
	 */
	private Integer userId;
	
	/**
	 * 设置用户信息外键
	 * @param userId     
	 *            用户信息外键
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 获取用户信息外键
	 * @return 用户信息外键
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
	 * 第三方id
	 */
	private String oauthId;
	
	/**
	 * 设置第三方id
	 * @param oauthId     
	 *            第三方id
	 */
	public void setOauthId(String oauthId) {
		this.oauthId = oauthId;
	}
	
	/**
	 * 获取第三方id
	 * @return 第三方id
	 */	
	public String getOauthId() {
		return oauthId;
	}
	
	/**
	 * TOKEN
	 */
	private String oauthAccessToken;
	
	/**
	 * 设置TOKEN
	 * @param oauthAccessToken     
	 *            TOKEN
	 */
	public void setOauthAccessToken(String oauthAccessToken) {
		this.oauthAccessToken = oauthAccessToken;
	}
	
	/**
	 * 获取TOKEN
	 * @return TOKEN
	 */	
	public String getOauthAccessToken() {
		return oauthAccessToken;
	}
	
	/**
	 * Token过期时间
	 */
	private String oauthExpires;
	
	/**
	 * 设置Token过期时间
	 * @param oauthExpires     
	 *            Token过期时间
	 */
	public void setOauthExpires(String oauthExpires) {
		this.oauthExpires = oauthExpires;
	}
	
	/**
	 * 获取Token过期时间
	 * @return Token过期时间
	 */	
	public String getOauthExpires() {
		return oauthExpires;
	}
	
	/**
	 * 数据字典：状态
	 */
	private Integer status;
	
	/**
	 * 设置数据字典：状态
	 * @param status     
	 *            数据字典：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 获取数据字典：状态
	 * @return 数据字典：状态
	 */	
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * 创建日期
	 */
	private java.util.Date createDate;
	
	/**
	 * 设置创建日期
	 * @param createDate     
	 *            创建日期
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * 获取创建日期
	 * @return 创建日期
	 */	
	public java.util.Date getCreateDate() {
		return createDate;
	}
	
	/**
	 * 修改日期
	 */
	private java.util.Date updateDate;
	
	/**
	 * 设置修改日期
	 * @param updateDate     
	 *            修改日期
	 */
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	
	/**
	 * 获取修改日期
	 * @return 修改日期
	 */	
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	
	/**
	 * 是否已删除
	 */
	private Integer deleted;
	
	/**
	 * 设置是否已删除
	 * @param deleted     
	 *            是否已删除
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * 获取是否已删除
	 * @return 是否已删除
	 */	
	public Integer getDeleted() {
		return deleted;
	}
	
}

