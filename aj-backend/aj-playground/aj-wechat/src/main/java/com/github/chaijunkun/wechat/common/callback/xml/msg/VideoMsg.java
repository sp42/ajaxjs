package com.github.chaijunkun.wechat.common.callback.xml.msg;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 视频消息
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class VideoMsg extends MediaMsg {
	
	/** 视频消息缩略图的媒体id */
	@JacksonXmlProperty(localName = "ThumbMediaId")
	@JacksonXmlCData
	private String thumbMediaId;

	/**
	 * 获取视频消息缩略图的媒体id
	 * @return 视频消息缩略图的媒体id
	 */
	public String getThumbMediaId() {
		return thumbMediaId;
	}

	/**
	 * 设置视频消息缩略图的媒体id
	 * @param thumbMediaId 视频消息缩略图的媒体id
	 */
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	
}
