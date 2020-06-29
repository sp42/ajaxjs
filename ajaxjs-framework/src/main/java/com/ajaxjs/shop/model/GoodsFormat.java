package com.ajaxjs.shop.model;

import com.ajaxjs.sql.orm.BaseModel;

public class GoodsFormat extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商品id外键
	 */
	private String goodsId;
	
	/**
	 * 设置商品id外键
	 * @param goodsId  
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	
	/**
	 * 获取商品id外键
	 * @return 商品id外键
	 */	
	public String getGoodsId() {
		return goodsId;
	}
	
	/**
	 * 商品标签
	 */
	private String label;
	
	/**
	 * 设置商品标签
	 * @param label  
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * 获取商品标签
	 * @return 商品标签
	 */	
	public String getLabel() {
		return label;
	}
	
	/**
	 * 价格
	 */
	private java.math.BigDecimal price;
	
	/**
	 * 设置价格
	 * @param price  
	 */
	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}
	
	/**
	 * 获取价格
	 * @return 价格
	 */	
	public java.math.BigDecimal getPrice() {
		return price;
	}
	

	/**
	 * 分类 id
	 */
	private Integer catalogId;
	
	/**
	 * 设置分类 id
	 * @param catelogId  
	 */
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}
	
	/**
	 * 获取分类 id
	 * @return 分类 id
	 */	
	public Integer getCatalogId() {
		return catalogId;
	}
	
}