package com.ajaxjs.shop.model;

import java.math.BigDecimal;

import com.ajaxjs.framework.BaseModel;

public class OrderItem extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private String formatName;
	
	/**
	 * 主订单号 id
	 */
	private Long orderId;
	
	/**
	 * 设置主订单号 id
	 * @param orderId  
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * 获取主订单号 id
	 * @return 主订单号 id
	 */	
	public Long getOrderId() {
		return orderId;
	}
	
	/**
	 * 买家 id
	 */
	private Long buyerId;
	
	/**
	 * 设置买家 id
	 * @param buyerId  
	 */
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	
	/**
	 * 获取买家 id
	 * @return 买家 id
	 */	
	public Long getBuyerId() {
		return buyerId;
	}
	
	/**
	 * 商品id外键
	 */
	private Long goodsId;
	
	/**
	 * 设置商品id外键
	 * @param goodsId  
	 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	
	/**
	 * 获取商品id外键
	 * @return 商品id外键
	 */	
	public Long getGoodsId() {
		return goodsId;
	}
	
	/**
	 * 商品规格 id
	 */
	private Long goodsFormatId;
	
	/**
	 * 设置商品规格 id
	 * @param goodsFormatId  
	 */
	public void setGoodsFormatId(Long goodsFormatId) {
		this.goodsFormatId = goodsFormatId;
	}
	
	/**
	 * 获取商品规格 id
	 * @return 商品规格 id
	 */	
	public Long getGoodsFormatId() {
		return goodsFormatId;
	}
	
	/**
	 * 商品数量
	 */
	private Integer goodsNumber;
	
	/**
	 * 设置商品数量
	 * @param goodsNumber  
	 */
	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	
	/**
	 * 获取商品数量
	 * @return 商品数量
	 */	
	public Integer getGoodsNumber() {
		return goodsNumber;
	}
	
	/**
	 * 商品单价
	 */
	private BigDecimal goodsPrice;
	
	/**
	 * 设置商品单价
	 * @param goodsPrice  
	 */
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	
	/**
	 * 获取商品单价
	 * @return 商品单价
	 */	
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	
	/**
	 * 该商品单项总费用
	 */
	private BigDecimal goodsAmount;
	
	/**
	 * 设置该商品单项总费用
	 * @param goodsAmount  
	 */
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	
	/**
	 * 获取该商品单项总费用
	 * @return 该商品单项总费用
	 */	
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}
	
	/**
	 * 卖家备货状态：0=备货中
	 */
	private Integer status;
	
	/**
	 * 设置卖家备货状态：0=备货中
	 * @param status  
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 获取卖家备货状态：0=备货中
	 * @return 卖家备货状态：0=备货中
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
	 * @param createByUser  
	 */
	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}
	
	/**
	 * 获取创建者 id
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
	 * @param deleted  
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * 获取是否已删除 1=已删除；0/null；未删除
	 * @return 是否已删除 1=已删除；0/null；未删除
	 */	
	public Integer getDeleted() {
		return deleted;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}
	
}