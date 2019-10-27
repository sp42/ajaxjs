package com.ajaxjs.shop.model;

import com.ajaxjs.framework.BaseModel;

public class UserAddress extends BaseModel {
	private static final long serialVersionUID = 1L;

	private String username;

	private String userIdName;
	
	/**
	 * 收货人手机
	 */
	private String mobile;

	/**
	 * 设置收货人手机
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取收货人手机
	 * 
	 * @return 收货人手机
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 收货人固话
	 */
	private String phone;

	/**
	 * 设置收货人固话
	 * 
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取收货人固话
	 * 
	 * @return 收货人固话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 设置省份
	 * 
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 获取省份
	 * 
	 * @return 省份
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 设置城市
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 获取城市
	 * 
	 * @return 城市
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 区县
	 */
	private String district;

	/**
	 * 设置区县
	 * 
	 * @param district
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * 获取区县
	 * 
	 * @return 区县
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * 收货地址
	 */
	private String address;

	/**
	 * 设置收货地址
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取收货地址
	 * 
	 * @return 收货地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 邮编
	 */
	private String zopCode;

	/**
	 * 设置邮编
	 * 
	 * @param zopCode
	 */
	public void setZopCode(String zopCode) {
		this.zopCode = zopCode;
	}

	/**
	 * 获取邮编
	 * 
	 * @return 邮编
	 */
	public String getZopCode() {
		return zopCode;
	}

	/**
	 * 数据字典：状态
	 */
	private Integer status;

	/**
	 * 设置数据字典：状态
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取数据字典：状态
	 * 
	 * @return 数据字典：状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 创建者 id
	 */
	private Integer createByUser;

	/**
	 * 设置创建者 id
	 * 
	 * @param createByUser
	 */
	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}

	/**
	 * 获取创建者 id
	 * 
	 * @return 创建者 id
	 */
	public Integer getCreateByUser() {
		return createByUser;
	}

	/**
	 * 是否已删除 1=已删除；0/null；未删除
	 */
	private Integer deleted;

	/**
	 * 设置是否已删除 1=已删除；0/null；未删除
	 * 
	 * @param deleted
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	/**
	 * 获取是否已删除 1=已删除；0/null；未删除
	 * 
	 * @return 是否已删除 1=已删除；0/null；未删除
	 */
	public Integer getDeleted() {
		return deleted;
	}

	/**
	 * 分类 id
	 */
	private Integer catelogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catelogId
	 */
	public void setCatelogId(Integer catelogId) {
		this.catelogId = catelogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Integer getCatelogId() {
		return catelogId;
	}

	/**
	 * 是否默认的地址1=默认
	 */
	private Boolean isDefault;

	/**
	 * 设置是否默认的地址1=默认
	 * 
	 * @param isDefault
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取是否默认的地址1=默认
	 * 
	 * @return 是否默认的地址1=默认
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserIdName() {
		return userIdName;
	}

	public void setUserIdName(String userIdName) {
		this.userIdName = userIdName;
	}

	private Long userId;
}