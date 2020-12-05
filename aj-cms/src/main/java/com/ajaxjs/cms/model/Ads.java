package com.ajaxjs.cms.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ajaxjs.framework.BaseModel;

/**
 * 广告
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Ads extends BaseModel {
	private static final long serialVersionUID = 676703531602780709L;

	@NotBlank(message = "名称不能为空")
	@Size(min = 2, max = 255, message = "长度应该介于3和255之间")
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
	@NotBlank(message = "链接不能为空")
	@Size(min = 10, max = 255, message = "长度应该介于3和255之间")
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