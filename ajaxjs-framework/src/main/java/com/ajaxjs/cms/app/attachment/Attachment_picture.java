package com.ajaxjs.cms.app.attachment;

import com.ajaxjs.framework.BaseModel;

public class Attachment_picture extends BaseModel {
	private static final long serialVersionUID = -5556112457279203513L;

	/**
	 * 用户浏览的地址，这是 vo 不用持久化
	 */
	private String urlRelativePath;

	/**
	 * 一张图片可能对应有多张尺寸的
	 */
	private Integer pid;

	/**
	 * 设置一张图片可能对应有多张尺寸的
	 * 
	 * @param pid 一张图片可能对应有多张尺寸的，pid 是原始图片id。没有多张则为 null
	 */
	public void setPid(Integer pid) {
		this.pid = pid;
	}

	/**
	 * 获取一张图片可能对应有多张尺寸的
	 * 
	 * @return 一张图片可能对应有多张尺寸的
	 */
	public Integer getPid() {
		return pid;
	}

	/**
	 * 图片名称
	 */
	private String name;

	/**
	 * 设置图片名称
	 * 
	 * @param name 图片名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取图片名称
	 * 
	 * @return 图片名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 图片简介
	 */
	private String content;

	/**
	 * 设置图片简介
	 * 
	 * @param content 图片简介
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取图片简介
	 * 
	 * @return 图片简介
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 图片路径
	 */
	private String path;

	/**
	 * 设置图片路径
	 * 
	 * @param path 图片路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取图片路径
	 * 
	 * @return 图片路径
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 文件大小（单位：字节）
	 */
	private Integer fileSize;

	/**
	 * 设置文件大小（单位：字节）
	 * 
	 * @param fileSize 文件大小（单位：字节）
	 */
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 获取文件大小（单位：字节）
	 * 
	 * @return 文件大小（单位：字节）
	 */
	public Integer getFileSize() {
		return fileSize;
	}

	/**
	 * 宽度
	 */
	private Integer picWidth;

	/**
	 * 设置宽度
	 * 
	 * @param picWidth 宽度
	 */
	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	/**
	 * 获取宽度
	 * 
	 * @return 宽度
	 */
	public Integer getPicWidth() {
		return picWidth;
	}

	private Integer index;

	/**
	 * 高度
	 */
	private Integer picHeight;

	/**
	 * 设置高度
	 * 
	 * @param picHeight 高度
	 */
	public void setPicHeight(Integer picHeight) {
		this.picHeight = picHeight;
	}

	/**
	 * 获取高度
	 * 
	 * @return 高度
	 */
	public Integer getPicHeight() {
		return picHeight;
	}

	/**
	 * 相册id
	 */
	private Integer albumId;

	/**
	 * 设置相册id
	 * 
	 * @param albumId 相册id
	 */
	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	/**
	 * 获取相册id
	 * 
	 * @return 相册id
	 */
	public Integer getAlbumId() {
		return albumId;
	}

	/**
	 * 该图片属于哪个实体？这里给出实体的 uuid
	 */
	private Long owner;

	/**
	 * 设置该图片属于哪个实体？这里给出实体的 uuid
	 * 
	 * @param owner 该图片属于哪个实体？这里给出实体的 uuid
	 */
	public void setOwner(Long owner) {
		this.owner = owner;
	}

	/**
	 * 获取该图片属于哪个实体？这里给出实体的 uuid
	 * 
	 * @return 该图片属于哪个实体？这里给出实体的 uuid
	 */
	public Long getOwner() {
		return owner;
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
	 * 唯一 id
	 */
	private Long uid;

	/**
	 * 设置唯一 id
	 * 
	 * @param uid 唯一 id，通过 uuid 生成不重复 id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * 获取唯一 id
	 * 
	 * @return 唯一 id
	 */
	public Long getUid() {
		return uid;
	}

	/**
	 * 创建者之id
	 */
	private Integer createByUser;

	/**
	 * 设置创建者之id
	 * 
	 * @param createByUser 创建者之id
	 */
	public void setCreateByUser(Integer createByUser) {
		this.createByUser = createByUser;
	}

	/**
	 * 获取创建者之id
	 * 
	 * @return 创建者之id
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

	public String getUrlRelativePath() {
		return urlRelativePath;
	}

	public void setUrlRelativePath(String urlRelativePath) {
		this.urlRelativePath = urlRelativePath;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	private int catalog;

}
