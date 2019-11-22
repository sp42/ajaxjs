package com.ajaxjs.shop.model;

import com.ajaxjs.framework.BaseModel;

public class GroupBuyer extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 团购id
	 */
	private Integer groupId;
	
	/**
	 * 设置团购id
	 * @param groupId  
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * 获取团购id
	 * @return 团购id
	 */	
	public Integer getGroupId() {
		return groupId;
	}
	
	/**
	 * 主订单id
	 */
	private Integer orderId;
	
	/**
	 * 设置主订单id
	 * @param orderId  
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * 获取主订单id
	 * @return 主订单id
	 */	
	public Integer getOrderId() {
		return orderId;
	}
	
	/**
	 * 团购明细id
	 */
	private Integer itemId;
	
	/**
	 * 设置团购明细id
	 * @param itemId  
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * 获取团购明细id
	 * @return 团购明细id
	 */	
	public Integer getItemId() {
		return itemId;
	}
	
	/**
	 * 团购数量
	 */
	private Integer groupNumber;
	
	/**
	 * 设置团购数量
	 * @param groupNumber  
	 */
	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	/**
	 * 获取团购数量
	 * @return 团购数量
	 */	
	public Integer getGroupNumber() {
		return groupNumber;
	}
	
	/**
	 * 团购单价
	 */
	private java.math.BigDecimal groupPrice;
	
	/**
	 * 设置团购单价
	 * @param groupPrice  
	 */
	public void setGroupPrice(java.math.BigDecimal groupPrice) {
		this.groupPrice = groupPrice;
	}
	
	/**
	 * 获取团购单价
	 * @return 团购单价
	 */	
	public java.math.BigDecimal getGroupPrice() {
		return groupPrice;
	}
	
	/**
	 * 团购金额
	 */
	private java.math.BigDecimal groupAmount;
	
	/**
	 * 设置团购金额
	 * @param groupAmount  
	 */
	public void setGroupAmount(java.math.BigDecimal groupAmount) {
		this.groupAmount = groupAmount;
	}
	
	/**
	 * 获取团购金额
	 * @return 团购金额
	 */	
	public java.math.BigDecimal getGroupAmount() {
		return groupAmount;
	}
}