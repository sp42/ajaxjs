package com.ajaxjs.app.service.section;

import com.ajaxjs.framework.BaseModel;

/**
 * 栏目实体，多对多表中的实体
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Section extends BaseModel {
	private static final long serialVersionUID = 2730165882935237248L;

	/**
	 * 分类 id
	 */
	private Integer catalogId;

	private Long entityUid;

	private Long entityId;

	private Integer typeId;

	public Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Long getEntityUid() {
		return entityUid;
	}

	public void setEntityUid(Long entityUid) {
		this.entityUid = entityUid;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	private String cover;
}
