package com.ajaxjs.user;

import java.util.Date;

import com.ajaxjs.framework.BaseModel;

/**
 * 用户模型
 * 
 * @author Frank Cheung
 *
 */
public class User extends BaseModel {
	/**
	 * 登录账号/用户名。另外注意区别 name 是昵称/姓名
	 */
	private String username;

	private String jobTitle;

	private String address;

	private Long locationProvince;

	private Long locationCity;

	private Long locationDistrict;

	public Long getLocationProvince() {
		return locationProvince;
	}

	public void setLocationProvince(Long locationProvince) {
		this.locationProvince = locationProvince;
	}

	public Long getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(Long locationCity) {
		this.locationCity = locationCity;
	}

	public Long getLocationDistrict() {
		return locationDistrict;
	}

	public void setLocationDistrict(Long locationDistrict) {
		this.locationDistrict = locationDistrict;
	}

	/**
	 * 设置登录账号/用户名
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取登录账号/用户名
	 * 
	 * @return 登录账号/用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 数据字典：性别
	 */
	private Integer gender;

	/**
	 * 出生日期
	 */
	private Date birthday;

	/**
	 * 设置出生日期
	 * 
	 * @param birthday
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * 获取出生日期
	 * 
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
	 * 
	 * @param avatar
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * 获取头像
	 * 
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
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取邮件
	 * 
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
	 * 
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取手机
	 * 
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
	 * 
	 * @param idCardNo
	 */
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	/**
	 * 获取身份证号码
	 * 
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
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取用户当前的状态
	 * 
	 * @return 用户当前的状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 区域 id
	 */
	private String location;

	/**
	 * 设置区域 id
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 获取区域 id
	 * 
	 * @return 区域 id
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 设置角色ID
	 * 
	 * @param role_id
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * 获取角色ID
	 * 
	 * @return 角色ID
	 */
	public Integer getRoleId() {
		return roleId;
	}

	/**
	 * 验证项
	 */
	private Integer verify;

	public static String getSexText(int sex) {
		if (sex == 1)
			return "男";
		else if (sex == 2)
			return "女";
		else
			return "未知";
	}

	public Integer getVerify() {
		return verify;
	}

	public void setVerify(Integer verify) {
		this.verify = verify;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
}