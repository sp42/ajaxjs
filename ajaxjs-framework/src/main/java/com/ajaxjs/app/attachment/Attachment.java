package com.ajaxjs.app.attachment;

import com.ajaxjs.sql.orm.BaseModel;

/**
 * 通用附件
 */
public class Attachment extends BaseModel  {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 文件目录
	 */
	private String folder;
	
	/**
	 * 设置文件目录
	 
	 * @param folder  
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}
	
	/**
	 * 获取文件目录
	 
	 * @return 文件目录
	 */	
	public String getFolder() {
		return folder;
	}
	
	/**
	 * 文件大小（单位：字节）
	 */
	private Integer fileSize;
	
	/**
	 * 设置文件大小（单位：字节）
	 
	 * @param fileSize  
	 */
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	
	/**
	 * 获取文件大小（单位：字节）
	 
	 * @return 文件大小（单位：字节）
	 */	
	public Integer getFileSize() {
		return fileSize;
	}
	
	/**
	 * 创建者 id
	 */
	private Integer createByUser;
	
	/**
	 * 设置创建者 id
	 
	 * @param createByUser  
	 */
	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}
	
	/**
	 * 获取创建者 id
	 
	 * @return 创建者 id
	 */	
	public Integer getCreateByUser() {
		return createByUser;
	}
	
	/**
	 * 分类 id
	 */
	private Integer catalogId;
	
	/**
	 * 设置分类 id
	 
	 * @param catalogId  
	 */
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}
	
	/**
	 * 获取分类 id
	 
	 * @return 分类 id
	 */	
	public Integer getCatalogId() {
		return catalogId;
	}
	
	

	/**
	 * 该附件属于哪个实体？这里给出实体的 uuid
	 */
	private Long owner;

	/**
	 * 设置该附件属于哪个实体？这里给出实体的 uuid
	 * 
	 * @param owner 该附件属于哪个实体？这里给出实体的 uuid
	 */
	public void setOwner(Long owner) {
		this.owner = owner;
	}

	/**
	 * 获取该附件属于哪个实体？这里给出实体的 uuid
	 * 
	 * @return 该附件属于哪个实体？这里给出实体的 uuid
	 */
	public Long getOwner() {
		return owner;
	}
}