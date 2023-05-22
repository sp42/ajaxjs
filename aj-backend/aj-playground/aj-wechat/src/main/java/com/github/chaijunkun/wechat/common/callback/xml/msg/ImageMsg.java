package com.github.chaijunkun.wechat.common.callback.xml.msg;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 图片消息
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class ImageMsg extends MediaMsg {
	
	/** 图片链接 */
	@JacksonXmlProperty(localName = "PicUrl")
	@JacksonXmlCData
	private String picUrl;

	/**
	 * 获取图片链接
	 * @return 图片链接
	 */
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * 设置图片链接
	 * @param picUrl 图片链接
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
}
