package com.ajaxjs.app.service;

import com.ajaxjs.framework.BaseModel;

/**
 * 收藏
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SectionList extends BaseModel {
	private static final long serialVersionUID = 1L;

	private Long entryId;

	private Long userId;

	private Integer entryTypeId;

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	public Integer getEntryTypeId() {
		return entryTypeId;
	}

	public void setEntryTypeId(Integer entryTypeId) {
		this.entryTypeId = entryTypeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 分类 id
	 */
	private Integer catalogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catalogId 分类 id
	 */
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Integer getCatalogId() {
		return catalogId;
	}
}