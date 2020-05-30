package com.ajaxjs.app;

import com.ajaxjs.framework.BaseModel;

/**
 * 产品
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Product extends BaseModel {
	private static final long serialVersionUID = -363640356538309564L;

	/**
	 * 分类 id
	 */
	private Long catalogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catelogId
	 */
	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Long getCatalogId() {
		return catalogId;
	}

	private String catalogName;

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * 商品品牌
	 */
	private String brand;

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
	 * 设置商品品牌
	 * 
	 * @param brand
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * 获取商品品牌
	 * 
	 * @return 商品品牌
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * 副标题
	 */
	private String subTitle;

	/**
	 * 设置副标题
	 * 
	 * @param subtitle
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * 获取副标题
	 * 
	 * @return 副标题
	 */
	public String getSubTitle() {
		return subTitle;
	}

}
