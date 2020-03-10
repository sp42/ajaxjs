package com.ajaxjs.shop.dep;

import java.util.Date;

import com.ajaxjs.shop.model.Goods;

/**
 * 团购
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Group extends Goods {
	private static final long serialVersionUID = 1L;

	/**
	 * 成团人数
	 */
	private Integer minimumPerson;
	
	/**
	 * 当前参团人数
	 */
	private Integer currentPerson;

	/**
	 * 开始时间
	 */
	private Date beginTime;

	/**
	 * 设置开始时间
	 * 
	 * @param beginTime
	 */
	public void setBeginTime(java.util.Date beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * 获取开始时间
	 * 
	 * @return 开始时间
	 */
	public java.util.Date getBeginTime() {
		return beginTime;
	}

	/**
	 * 结束时间
	 */
	private java.util.Date endTime;

	/**
	 * 设置结束时间
	 * 
	 * @param endTime
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取结束时间
	 * 
	 * @return 结束时间
	 */
	public java.util.Date getEndTime() {
		return endTime;
	}

	/**
	 * 最大买家数
	 */
	private Integer maxNum;

	/**
	 * 设置最大买家数
	 * 
	 * @param maxNum
	 */
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	/**
	 * 获取最大买家数
	 * 
	 * @return 最大买家数
	 */
	public Integer getMaxNum() {
		return maxNum;
	}

	/**
	 * 买家起团金额
	 */
	private java.math.BigDecimal buyerAmount;

	/**
	 * 设置买家起团金额
	 * 
	 * @param buyerAmount
	 */
	public void setBuyerAmount(java.math.BigDecimal buyerAmount) {
		this.buyerAmount = buyerAmount;
	}

	/**
	 * 获取买家起团金额
	 * 
	 * @return 买家起团金额
	 */
	public java.math.BigDecimal getBuyerAmount() {
		return buyerAmount;
	}

	/**
	 * 最低开团金额
	 */
	private java.math.BigDecimal minAmount;

	/**
	 * 设置最低开团金额
	 * 
	 * @param minAmount
	 */
	public void setMinAmount(java.math.BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	/**
	 * 获取最低开团金额
	 * 
	 * @return 最低开团金额
	 */
	public java.math.BigDecimal getMinAmount() {
		return minAmount;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getMaxGoodsNumber() {
		return maxGoodsNumber;
	}

	public void setMaxGoodsNumber(Integer maxGoodsNumber) {
		this.maxGoodsNumber = maxGoodsNumber;
	}

	public Integer getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(Integer soldNumber) {
		this.soldNumber = soldNumber;
	}

	private Date deliveryTime;

	private Integer maxGoodsNumber;
	
	private Integer soldNumber;

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getMinimumPerson() {
		return minimumPerson;
	}

	public void setMinimumPerson(Integer minimumPerson) {
		this.minimumPerson = minimumPerson;
	}

	public Integer getCurrentPerson() {
		return currentPerson;
	}

	public void setCurrentPerson(Integer currentPerson) {
		this.currentPerson = currentPerson;
	}

	private Long goodsId;
}