package com.github.chaijunkun.wechat.common.callback.xml.msg;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 多媒体消息
 * @author chaijunkun
 * @since 2016年8月29日
 */
public abstract class MediaMsg extends BaseMsg {
	
	/**
	 * 多媒体消息媒体id，可以调用多媒体文件下载接口拉取数据
	 */
	
	@JacksonXmlProperty(localName = "MediaId")
	@JacksonXmlCData
	private String mediaId;

	/**
	 * 获取多媒体消息媒体id
	 * @return 多媒体消息媒体id
	 */
	public String getMediaId() {
		return mediaId;
	}

	/**
	 * 设置多媒体消息媒体id
	 * @param mediaId 多媒体消息媒体id
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
