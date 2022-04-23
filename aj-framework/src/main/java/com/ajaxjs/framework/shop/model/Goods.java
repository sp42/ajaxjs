package com.ajaxjs.framework.shop.model;

import java.math.BigDecimal;

import com.ajaxjs.framework.BaseModel;

/**
 * 商品
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Goods extends BaseModel {
	/**
	 * 产品 id 外键
	 */
	private Long productId;

	/**
	 * 设置产品id外键
	 * 
	 * @param goodsId
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * 获取产品 id外键
	 * 
	 * @return 产品id外键
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * 商品标签
	 */
	private String label;

	/**
	 * 设置商品标签
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 获取商品标签
	 * 
	 * @return 商品标签
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * 设置价格
	 * 
	 * @param price
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取价格
	 * 
	 * @return 价格
	 */
	public BigDecimal getPrice() {
		return price;
	}
}