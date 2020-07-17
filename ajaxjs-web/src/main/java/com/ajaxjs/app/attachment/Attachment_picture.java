package com.ajaxjs.app.attachment;

import com.ajaxjs.app.Attachment;

public class Attachment_picture extends Attachment {
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
}
