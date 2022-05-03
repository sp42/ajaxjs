package com.ajaxjs.storage.file.model;

import java.util.Date;

/**
 * 文件状态 在表 ufs_storage_file
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FileStatus {
	public static final String METAKEY_FILENAME = "filename";

	private Long id;

	/**
	 * 该文件所属的应用 ID
	 */
	private String appId;

	/**
	 * 该文件所属的用户(创建人)
	 */
	private String ownerId;

	/**
	 * 该文件所对应的存储块
	 */
	private long blockId;

	private String filename;

	/**
	 * @JsonIgnore
	 */
	private Long indexServerId;

	/**
	 * 文件的安全键，上传文件后自动产生，以后进行签名操作时需要该值
	 */
	private String secretKey;

	/**
	 * 文件的 HTTP Content-Type
	 */
	private String contentType;

	/**
	 * 文件大小
	 */
	private Long contentLength;

	/**
	 * 文件的内容 MD5 啥希
	 */
	private String contentMd5;

	/**
	 * 文件
	 */
	private AccessControl accessControl;

	/**
	 * 文件上传时间
	 */
	private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public long getBlockId() {
		return blockId;
	}

	public void setBlockId(long blockId) {
		this.blockId = blockId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getIndexServerId() {
		return indexServerId;
	}

	public void setIndexServerId(Long indexServerId) {
		this.indexServerId = indexServerId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentMd5() {
		return contentMd5;
	}

	public void setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
	}

	public AccessControl getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(AccessControl accessControl) {
		this.accessControl = accessControl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
