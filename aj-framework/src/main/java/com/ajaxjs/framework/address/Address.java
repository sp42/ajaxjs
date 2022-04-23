package com.ajaxjs.framework.address;

import com.ajaxjs.framework.BaseModel;

/**
 * 地址
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Address extends BaseModel {
	private String username;

	private String userIdName;

	/**
	 * 收货人手机
	 */
	private String mobile;

	/**
	 * 收货人固话
	 */
	private String phone;

	private Long locationProvince;

	private Long locationCity;

	private Long locationDistrict;

	/**
	 * 收货地址
	 */
	private String address;

	/**
	 * 邮编
	 */
	private String zipCode;

	/**
	 * 创建者 id
	 */
	private Integer createByUser;

	/**
	 * 用户 id
	 */
	private Long owner;

	/**
	 * 分类 id
	 */
	private Integer catelogId;

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
	 * 设置邮编
	 * 
	 * @param zipCode
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取邮编
	 * 
	 * @return 邮编
	 */
	public String getZipCode() {
		return zipCode;
	}

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

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
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

}