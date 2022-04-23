package com.ajaxjs.framework.shop.model;

import java.math.BigDecimal;

import com.ajaxjs.framework.BaseModel;

/**
 * 订单记录（是否有用？）
 * 
 * @author Frank Cheung
 *
 */
public class OrderPayLog extends BaseModel {
	/**
	 * 主订单id
	 */
	private Integer orderId;

	/**
	 * 设置主订单id
	 * 
	 * @param orderId
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * 获取主订单id
	 * 
	 * @return 主订单id
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * 主订单号
	 */
	private String orderNumber;

	/**
	 * 设置主订单号
	 * 
	 * @param orderNumber
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * 获取主订单号
	 * 
	 * @return 主订单号
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;

	/**
	 * 设置订单金额
	 * 
	 * @param orderAmount
	 */
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	/**
	 * 获取订单金额
	 * 
	 * @return 订单金额
	 */
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	/**
	 * 交易订单号
	 */
	private String outerTradeNo;

	/**
	 * 设置交易订单号
	 * 
	 * @param outerTradeNo
	 */
	public void setOuterTradeNo(String outerTradeNo) {
		this.outerTradeNo = outerTradeNo;
	}

	/**
	 * 获取交易订单号
	 * 
	 * @return 交易订单号
	 */
	public String getOuterTradeNo() {
		return outerTradeNo;
	}

	/**
	 * 分类 id
	 */
	private Integer catalogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catelogId
	 */
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Integer getCatalogId() {
		return catalogId;
	}

}