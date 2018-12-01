package com.ajaxjs.cms.model;

import com.ajaxjs.framework.BaseModel;

public class Attachment extends BaseModel {
	private static final long serialVersionUID = 1L;

	/**
	 * 附件名称
	 */
	private String name;

	/**
	 * 设置附件名称
	 * 
	 * @param name 附件名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取附件名称
	 * 
	 * @return 附件名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 文件大小
	 */
	private Integer fileSize;

	/**
	 * 设置文件大小
	 * 
	 * @param fileSize 文件大小
	 */
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 获取文件大小
	 * 
	 * @return 文件大小
	 */
	public Integer getFileSize() {
		return fileSize;
	}

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 设置内容
	 * 
	 * @param content 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

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
	 * 创建日期
	 */
	private java.util.Date createDate;

	/**
	 * 设置创建日期
	 * 
	 * @param createDate 创建日期
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取创建日期
	 * 
	 * @return 创建日期
	 */
	public java.util.Date getCreateDate() {
		return createDate;
	}

	/**
	 * 修改日期
	 */
	private java.util.Date updateDate;

	/**
	 * 设置修改日期
	 * 
	 * @param updateDate 修改日期
	 */
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 获取修改日期
	 * 
	 * @return 修改日期
	 */
	public java.util.Date getUpdateDate() {
		return updateDate;
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

	/**
	 * 该图片属于哪个实体？这里给出实体的 uuid
	 */
	private String owner;

	/**
	 * 设置该图片属于哪个实体？这里给出实体的 uuid
	 * 
	 * @param owner 该图片属于哪个实体？这里给出实体的 uuid
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 获取该图片属于哪个实体？这里给出实体的 uuid
	 * 
	 * @return 该图片属于哪个实体？这里给出实体的 uuid
	 */
	public String getOwner() {
		return owner;
	}

}
