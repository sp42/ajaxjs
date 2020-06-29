package com.ajaxjs.user.model;

import com.ajaxjs.sql.orm.BaseModel;

/**
 * 对应数据库口令表，普通密码登录
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserCommonAuth extends BaseModel {
	private static final long serialVersionUID = 8721396380768799894L;
	
	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 设置用户id
	 * 
	 * @param userId
	 *            用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 获取用户id
	 * 
	 * @return 用户id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取密码
	 * 
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 数据字典：登录类型
	 */
	private Integer loginType;

	/**
	 * 设置数据字典：登录类型
	 * 
	 * @param loginType
	 *            数据字典：登录类型
	 */
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	/**
	 * 获取数据字典：登录类型
	 * 
	 * @return 数据字典：登录类型
	 */
	public Integer getLoginType() {
		return loginType;
	}

	/**
	 * 手机号码是否已验证
	 */
	private Integer phone_verified;

	/**
	 * 设置手机号码是否已验证
	 * 
	 * @param phone_verified
	 *            手机号码是否已验证
	 */
	public void setPhone_verified(Integer phone_verified) {
		this.phone_verified = phone_verified;
	}

	/**
	 * 获取手机号码是否已验证
	 * 
	 * @return 手机号码是否已验证
	 */
	public Integer getPhone_verified() {
		return phone_verified;
	}

	/**
	 * 身份证是否已验证（是否实名认证）
	 */
	private String idCardNo_verified;

	/**
	 * 设置身份证是否已验证（是否实名认证）
	 * 
	 * @param idCardNo_verified
	 *            身份证是否已验证（是否实名认证）
	 */
	public void setIdCardNo_verified(String idCardNo_verified) {
		this.idCardNo_verified = idCardNo_verified;
	}

	/**
	 * 获取身份证是否已验证（是否实名认证）
	 * 
	 * @return 身份证是否已验证（是否实名认证）
	 */
	public String getIdCardNo_verified() {
		return idCardNo_verified;
	}

	/**
	 * 邮箱是否已验证
	 */
	private Integer email_verified;

	/**
	 * 设置邮箱是否已验证
	 * 
	 * @param email_verified
	 *            邮箱是否已验证
	 */
	public void setEmail_verified(Integer email_verified) {
		this.email_verified = email_verified;
	}

	/**
	 * 获取邮箱是否已验证
	 * 
	 * @return 邮箱是否已验证
	 */
	public Integer getEmail_verified() {
		return email_verified;
	}

	/**
	 * 注册ip
	 */
	private String registerIp;

	/**
	 * 设置注册ip
	 * 
	 * @param registerIp
	 *            注册ip
	 */
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	/**
	 * 获取注册ip
	 * 
	 * @return 注册ip
	 */
	public String getRegisterIp() {
		return registerIp;
	}

	/**
	 * 数据字典：注册时的登录类型
	 */
	private String registerType;

	/**
	 * 设置数据字典：注册时的登录类型
	 * 
	 * @param registerType
	 *            数据字典：注册时的登录类型
	 */
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	/**
	 * 获取数据字典：注册时的登录类型
	 * 
	 * @return 数据字典：注册时的登录类型
	 */
	public String getRegisterType() {
		return registerType;
	}

	/**
	 * 修改密码时间
	 */
	private java.util.Date updatePasswordDate;

	/**
	 * 设置修改密码时间
	 * 
	 * @param updatePasswordDate
	 *            修改密码时间
	 */
	public void setUpdatePasswordDate(java.util.Date updatePasswordDate) {
		this.updatePasswordDate = updatePasswordDate;
	}

	/**
	 * 获取修改密码时间
	 * 
	 * @return 修改密码时间
	 */
	public java.util.Date getUpdatePasswordDate() {
		return updatePasswordDate;
	}

	/**
	 * 口令状态
	 */
	private Integer status;

	/**
	 * 设置口令状态
	 * 
	 * @param status
	 *            口令状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取口令状态
	 * 
	 * @return 口令状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 唯一 id
	 */
	private Long uid;

	/**
	 * 设置唯一 id
	 * 
	 * @param uid
	 *            唯一 id，通过“雪花算法”生成不重复 id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * 获取唯一 id
	 * 
	 * @return 唯一 id
	 */
	public Long getUid() {
		return uid;
	}

	/**
	 * 是否已删除
	 */
	private Integer deleted;

	/**
	 * 设置是否已删除
	 * 
	 * @param deleted
	 *            是否已删除
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	/**
	 * 获取是否已删除
	 * 
	 * @return 是否已删除
	 */
	public Integer getDeleted() {
		return deleted;
	}

}
