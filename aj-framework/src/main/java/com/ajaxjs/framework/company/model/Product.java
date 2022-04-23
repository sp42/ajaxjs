package com.ajaxjs.framework.company.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.shop.model.Goods;

/**
 * 产品
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Product extends BaseModel {
	private Goods[] goods;

	/**
	 * 商家
	 */
	private Long sellerId;

	/**
	 * 简介
	 */
	private String intro;

	/**
	 * 封面
	 */
	private String cover;

	/**
	 * 产地 id
	 */
	private Integer areaId;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	/**
	 * 评分
	 */
	private int score;

	/**
	 * 封面价格
	 */
	private String coverPrice;

	/**
	 * 设置封面价格
	 * 
	 * @param coverPrice
	 */
	public void setCoverPrice(String coverPrice) {
		this.coverPrice = coverPrice;
	}

	/**
	 * 获取封面价格
	 * 
	 * @return 封面价格
	 */
	public String getCoverPrice() {
		return coverPrice;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	/**
	 * 分类 id
	 */
	private Long catelogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catelogId
	 */
	public void setCatelogId(Long catelogId) {
		this.catelogId = catelogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Long getCatelogId() {
		return catelogId;
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
	private String title;

	/**
	 * 设置副标题
	 * 
	 * @param subtitle
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取副标题
	 * 
	 * @return 副标题
	 */
	public String getTitle() {
		return title;
	}

	public Goods[] getGoods() {
		return goods;
	}

	public void setGoods(Goods[] goods) {
		this.goods = goods;
	}

}
