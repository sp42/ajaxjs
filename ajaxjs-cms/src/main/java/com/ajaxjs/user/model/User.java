package com.ajaxjs.user.model;

import java.util.Date;

import com.ajaxjs.framework.BaseModel;

/**
 * 表示一个用户
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class User extends BaseModel {

	private static final long serialVersionUID = -6339873217631719987L;
	
	/**
	 * 用户姓名
	 */
	private String username;
	
	/**
	 * 设置用户姓名
	 * @param username     
	 *            用户姓名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 获取用户姓名
	 * @return 用户姓名
	 */	
	public String getUsername() {
		return username;
	}
	
	/**
	 * 数据字典：性别
	 */
	private Integer sex;
	
	/**
	 * 设置数据字典：性别
	 * @param sex     
	 *            数据字典：性别
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	/**
	 * 获取数据字典：性别
	 * @return 数据字典：性别
	 */	
	public Integer getSex() {
		return sex;
	}
	
	/**
	 * 出生日期
	 */
	private Date birthday;
	
	/**
	 * 设置出生日期
	 * @param birthday     
	 *            出生日期
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * 获取出生日期
	 * @return 出生日期
	 */	
	public Date getBirthday() {
		return birthday;
	}
	
	/**
	 * 头像
	 */
	private String avatar;
	
	/**
	 * 设置头像
	 * @param avatar     
	 *            头像，关联图片表
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	/**
	 * 获取头像
	 * @return 头像
	 */	
	public String getAvatar() {
		return avatar;
	}
	
	/**
	 * 邮件
	 */
	private String email;
	
	/**
	 * 设置邮件
	 * @param email     
	 *            邮件
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 获取邮件
	 * @return 邮件
	 */	
	public String getEmail() {
		return email;
	}
	
	/**
	 * 手机
	 */
	private String phone;
	
	/**
	 * 设置手机
	 * @param phone     
	 *            手机
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * 获取手机
	 * @return 手机
	 */	
	public String getPhone() {
		return phone;
	}
	
	/**
	 * 身份证号码
	 */
	private String idCardNo;
	
	/**
	 * 设置身份证号码
	 * @param idCardNo     
	 *            身份证号码
	 */
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	
	/**
	 * 获取身份证号码
	 * @return 身份证号码
	 */	
	public String getIdCardNo() {
		return idCardNo;
	}
	
	/**
	 * 用户当前的状态
	 */
	private Integer status;
	
	/**
	 * 设置用户当前的状态
	 * @param status     
	 *            用户当前的状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 获取用户当前的状态
	 * @return 用户当前的状态
	 */	
	public Integer getStatus() {
		return status;
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
	
	/**
	 * 个人简介
	 */
	private String content;
	
	/**
	 * 设置个人简介
	 * @param content     
	 *            个人简介
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 获取个人简介
	 * @return 个人简介
	 */	
	public String getContent() {
		return content;
	}
	
	/**
	 * 区域 id
	 */
	private Integer location;
	
	/**
	 * 设置区域 id
	 * @param location     
	 *            区域 id
	 */
	public void setLocation(Integer location) {
		this.location = location;
	}
	
	/**
	 * 获取区域 id
	 * @return 区域 id
	 */	
	public Integer getLocation() {
		return location;
	}
}

