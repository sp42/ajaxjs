package com.ajaxjs.cms.dao;

import com.ajaxjs.framework.BaseModel;

public class Ads extends BaseModel {
	private static final long serialVersionUID = 1L;

	/**
	 * 数据字典：状态
	 */
	private Integer status;

	/**
	 * 设置数据字典：状态
	 * 
	 * @param status 数据字典：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取数据字典：状态
	 * 
	 * @return 数据字典：状态
	 */
	public Integer getStatus() {
		return status;
	}

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

	/**
	 * 是否已删除 1=已删除；0/null；未删除
	 */
	private Integer deleted;

	/**
	 * 设置是否已删除 1=已删除；0/null；未删除
	 * 
	 * @param deleted 是否已删除 1=已删除；0/null；未删除
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	/**
	 * 获取是否已删除 1=已删除；0/null；未删除
	 * 
	 * @return 是否已删除 1=已删除；0/null；未删除
	 */
	public Integer getDeleted() {
		return deleted;
	}

	private String catelogName;

	/**
	 * 分类 id
	 */
	private Integer catelog;

	/**
	 * 设置分类 id
	 * 
	 * @param catelogId 分类 id
	 */
	public void setCatelog(Integer catelogId) {
		this.catelog = catelogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Integer getCatelog() {
		return catelog;
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

	public String getCatelogName() {
		return catelogName;
	}

	public void setCatelogName(String catelogName) {
		this.catelogName = catelogName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * 广告图片
	 */
	private String img;
}