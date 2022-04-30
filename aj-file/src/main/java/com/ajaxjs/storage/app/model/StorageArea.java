package com.ajaxjs.storage.app.model;

/**
 * @TableName("ufs_storage_area")
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class StorageArea {
	/**
	 * 存储区 ID
	 */
	private Long id;

	/**
	 * 服务器名称
	 */
	private String name;

	/**
	 * 文件服务器
	 */
	private long fileServerId;

	/**
	 * 索引服务器
	 */
	private long indexServerId;

	/**
	 * 索引服务器
	 */
	private String orgIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFileServerId() {
		return fileServerId;
	}

	public void setFileServerId(long fileServerId) {
		this.fileServerId = fileServerId;
	}

	public long getIndexServerId() {
		return indexServerId;
	}

	public void setIndexServerId(long indexServerId) {
		this.indexServerId = indexServerId;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	
	

}
