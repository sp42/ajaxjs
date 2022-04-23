package com.ajaxjs.framework.shop.model;

import java.math.BigDecimal;

import com.ajaxjs.framework.BaseModel;

/**
 * 订单商品明细
 * 
 * @author Frank Cheung
 *
 */
public class OrderItem extends BaseModel {
	private String formatName;

	private Long sellerId;

	/**
	 * 主订单号 id
	 */
	private Long orderId;

	/**
	 * 
	 */
	private Long groupId;

	/**
	 * 设置主订单号 id
	 * 
	 * @param orderId
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * 获取主订单号 id
	 * 
	 * @return 主订单号 id
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * 商品id外键
	 */
	private Long goodsId;

	/**
	 * 设置商品id外键
	 * 
	 * @param goodsId
	 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * 获取商品id外键
	 * 
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
	 * 
	 * @param goodsFormatId
	 */
	public void setGoodsFormatId(Long goodsFormatId) {
		this.goodsFormatId = goodsFormatId;
	}

	/**
	 * 获取商品规格 id
	 * 
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
	 * 
	 * @param goodsNumber
	 */
	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	/**
	 * 获取商品数量
	 * 
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
	 * 
	 * @param goodsPrice
	 */
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	/**
	 * 获取商品单价
	 * 
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
	 * 
	 * @param goodsAmount
	 */
	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	/**
	 * 获取该商品单项总费用
	 * 
	 * @return 该商品单项总费用
	 */
	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}