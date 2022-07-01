package com.ajaxjs.attachment;

import java.util.Date;

/**
 * 附件
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Attachment {

	/**
	 * 主键 id，自增
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 简介、描述
	 */
	private String desc;

	/**
	 * 分类：null/0/1=普通图片、2=头像/封面图片、3=相册图片
	 */
	private Long type;

	/**
	 * 租户 id。0 = 不设租户
	 */
	private Long tenantId;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 文件大小（单位：字节）
	 */
	private Long fileSize;

	/**
	 * 该图片属于哪个实体？这里给出实体的 uid
	 */
	private Long ownerId;

	/**
	 * 数据字典：状态
	 */
	private Integer stat;

	/**
	 * 唯一 id，通过 uuid 生成不重复 id
	 */
	private Long uid;

	/**
	 * 创建者 id
	 */
	private Long createByUser;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 修改时间
	 */
	private Date updateDate;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getCreateByUser() {
		return createByUser;
	}

	public void setCreateByUser(Long createByUser) {
		this.createByUser = createByUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
