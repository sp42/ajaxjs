package com.ajaxjs.framework.company.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 广告
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Ads extends BaseModel {

	private String name;

	/**
	 * 分类 id
	 */
	private Long catalogId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
}