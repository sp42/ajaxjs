package com.ajaxjs.jxc.base.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 
 */
public class Supplier extends BaseModel {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String 供应商联系人;

	/**
	 * 设置
	 * 
	 * @param 供应商联系人
	 */
	public void set供应商联系人(String 供应商联系人) {
		this.供应商联系人 = 供应商联系人;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String get供应商联系人() {
		return 供应商联系人;
	}

	/**
	 * 
	 */
	private String 供应商联系电话;

	/**
	 * 设置
	 * 
	 * @param 供应商联系电话
	 */
	public void set供应商联系电话(String 供应商联系电话) {
		this.供应商联系电话 = 供应商联系电话;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String get供应商联系电话() {
		return 供应商联系电话;
	}

	/**
	 * 
	 */
	private String 邮箱;

	/**
	 * 设置
	 * 
	 * @param 邮箱
	 */
	public void set邮箱(String 邮箱) {
		this.邮箱 = 邮箱;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String get邮箱() {
		return 邮箱;
	}

	/**
	 * 
	 */
	private Integer catalogId;

	/**
	 * 设置
	 * 
	 * @param catalogId
	 */
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public Integer getCatalogId() {
		return catalogId;
	}

	/**
	 * 接收供应商物料仓库
	 */
	private Integer 仓库;

	/**
	 * 设置接收供应商物料仓库
	 * 
	 * @param 仓库
	 */
	public void set仓库(Integer 仓库) {
		this.仓库 = 仓库;
	}

	/**
	 * 获取接收供应商物料仓库
	 * 
	 * @return 接收供应商物料仓库
	 */
	public Integer get仓库() {
		return 仓库;
	}

	/**
	 * 我方采购员
	 */
	private Long 采购员;
	
	private Long userId;

	/**
	 * 设置我方采购员
	 * 
	 * @param 采购员
	 */
	public void set采购员(Long 采购员) {
		this.采购员 = 采购员;
	}

	/**
	 * 获取我方采购员
	 * 
	 * @return 我方采购员
	 */
	public Long get采购员() {
		return 采购员;
	}

	/**
	 * 
	 */
	private Integer 银行码;

	/**
	 * 设置
	 * 
	 * @param 银行码
	 */
	public void set银行码(Integer 银行码) {
		this.银行码 = 银行码;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public Integer get银行码() {
		return 银行码;
	}

	/**
	 * 
	 */
	private String 货币;

	/**
	 * 设置
	 * 
	 * @param 货币
	 */
	public void set货币(String 货币) {
		this.货币 = 货币;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String get货币() {
		return 货币;
	}

	/**
	 * 
	 */
	private Long addressId;

	/**
	 * 设置
	 * 
	 * @param addressId
	 */
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public Long getAddressId() {
		return addressId;
	}

	/**
	 * 
	 */
	private Integer 税码;

	/**
	 * 设置
	 * 
	 * @param 税码
	 */
	public void set税码(Integer 税码) {
		this.税码 = 税码;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public Integer get税码() {
		return 税码;
	}

	/**
	 * 是否暂停付款
	 */
	private Boolean 付款暂停;

	/**
	 * 设置是否暂停付款
	 * 
	 * @param 付款暂停
	 */
	public void set付款暂停(Boolean 付款暂停) {
		this.付款暂停 = 付款暂停;
	}

	/**
	 * 获取是否暂停付款
	 * 
	 * @return 是否暂停付款
	 */
	public Boolean get付款暂停() {
		return 付款暂停;
	}

	/**
	 * 
	 */
	private String 暂停原因码;

	/**
	 * 设置
	 * 
	 * @param 暂停原因码
	 */
	public void set暂停原因码(String 暂停原因码) {
		this.暂停原因码 = 暂停原因码;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String get暂停原因码() {
		return 暂停原因码;
	}

	/**
	 * 
	 */
	private java.util.Date 暂停付款日期;

	/**
	 * 设置
	 * 
	 * @param 暂停付款日期
	 */
	public void set暂停付款日期(java.util.Date 暂停付款日期) {
		this.暂停付款日期 = 暂停付款日期;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public java.util.Date get暂停付款日期() {
		return 暂停付款日期;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}