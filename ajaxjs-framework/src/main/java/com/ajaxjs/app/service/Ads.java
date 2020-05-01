package com.ajaxjs.app.service;

import com.ajaxjs.framework.BaseModel;

public class Ads extends BaseModel {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建者 id
	 */
	private Integer createByUser;

	/**
	 * 设置创建者 id
	 * 
	 * @param createByUser 创建者 id
	 */
	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}

	/**
	 * 获取创建者 id
	 * 
	 * @return 创建者 id
	 */
	public Integer getCreateByUser() {
		return createByUser;
	}



	private String catalogName;

	/**
	 * 分类 id
	 */
	private Long catalogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catalogId 分类 id
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

	/**
	 * 链接
	 */
	private String link;

	/**
	 * 设置链接
	 * 
	 * @param link 链接
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * 获取链接
	 * 
	 * @return 链接
	 */
	public String getLink() {
		return link;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * 广告图片
	 */
	private String cover;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
}