package com.ajaxjs.shop.model;

import java.math.BigDecimal;

import com.ajaxjs.sql.orm.BaseModel;

/**
 * 购物车
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Cart extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long groupId;
	
	private Long sellerId;	
	
	private Long goodsId;
	
	private String goodsFormat;
	
	private String goodsName;

	private BigDecimal price;
	
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
	 * 买家 id
	 */
	private Long userId;
	
	/**
	 * 设置买家 id
	 
	 * @param userId  
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * 获取买家 id
	 
	 * @return 买家 id
	 */	
	public Long getUserId() {
		return userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGoodsFormat() {
		return goodsFormat;
	}

	public void setGoodsFormat(String goodsFormat) {
		this.goodsFormat = goodsFormat;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	
}