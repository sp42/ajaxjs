package com.ajaxjs.cms;

import com.ajaxjs.framework.BaseModel;

/**
 * 收藏
 * 
 * @author Frank Cheung
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
	private Integer catelogId;

	/**
	 * 设置分类 id
	 * 
	 * @param catelogId 分类 id
	 */
	public void setCatelogId(Integer catelogId) {
		this.catelogId = catelogId;
	}

	/**
	 * 获取分类 id
	 * 
	 * @return 分类 id
	 */
	public Integer getCatelogId() {
		return catelogId;
	}
}