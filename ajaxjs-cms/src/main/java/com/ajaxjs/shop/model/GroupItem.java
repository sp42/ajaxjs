package com.ajaxjs.shop.model;

import com.ajaxjs.framework.BaseModel;

public class GroupItem extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 团购id
	 */
	private Long groupId;
	
	/**
	 * 设置团购id
	 * @param groupId  
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * 获取团购id
	 * @return 团购id
	 */	
	public Long getGroupId() {
		return groupId;
	}
	
	/**
	 * 商品id
	 */
	private Long goodsId;
	
	/**
	 * 设置商品id
	 * @param goodsId  
	 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	
	/**
	 * 获取商品id
	 * @return 商品id
	 */	
	public Long getGoodsId() {
		return goodsId;
	}
	
	/**
	 * 商品规格id
	 */
	private Long goodsFormatId;
	
	/**
	 * 设置商品规格id
	 * @param goodsFormatId  
	 */
	public void setGoodsFormatId(Long goodsFormatId) {
		this.goodsFormatId = goodsFormatId;
	}
	
	/**
	 * 获取商品规格id
	 * @return 商品规格id
	 */	
	public Long getGoodsFormatId() {
		return goodsFormatId;
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