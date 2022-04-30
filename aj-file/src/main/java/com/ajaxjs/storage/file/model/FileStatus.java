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

	@ApiModelProperty(value = "该文件所对应的存储块", hidden = true)
	@TableField("block_id")
	private long blockId;

	@ApiModelProperty(value = "文件名")
	@TableField("file_name")
	private String filename;

	@TableField("index_server_id")
	@JsonIgnore
	private Long indexServerId;

	@ApiModelProperty("文件的安全键，上传文件后自动产生，以后进行签名操作时需要该值")
	@TableField
	private String secretKey;

	/**
	 * 文件的 HTTP Content-Type
	 */
	private String contentType;

	@ApiModelProperty("文件大小")
	@TableField
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
}
