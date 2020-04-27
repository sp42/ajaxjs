package com.ajaxjs.shop.model;

import com.ajaxjs.app.Product;

public class Goods extends Product {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商家
	 */
	private Long sellerId;
	
	/**
	 * 简介
	 */
	private String intro;

	/**
	 * 标题价格
	 */
	private String titlePrice;
	
	/**
	 * 封面
	 */
	private String cover;
	
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
	
}