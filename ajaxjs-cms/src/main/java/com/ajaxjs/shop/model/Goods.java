package com.ajaxjs.shop.model;

import com.ajaxjs.cms.app.Product;

public class Goods extends Product {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商家
	 */
	private Long sellerId;

	/**
	 * 标题价格
	 */
	private String titlePrice;

	/**
	 * 封面价格
	 */
	private String coverPrice;
	
	/**
	 * 设置封面价格
	 * @param coverPrice  
	 */
	public void setCoverPrice(String coverPrice) {
		this.coverPrice = coverPrice;
	}
	
	/**
	 * 获取封面价格
	 * @return 封面价格
	 */	
	public String getCoverPrice() {
		return coverPrice;
	}

	public String getTitlePrice() {
		return titlePrice;
	}

	public void setTitlePrice(String titlePrice) {
		this.titlePrice = titlePrice;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	
}