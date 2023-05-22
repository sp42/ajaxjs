package com.github.chaijunkun.wechat.common.callback.xml.msg;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 链接消息
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class LinkMsg extends BaseMsg {
	
	/** 消息标题 */
	@JacksonXmlProperty(localName = "Title")
	@JacksonXmlCData
	private String title;
	
	/** 消息描述 */
	@JacksonXmlProperty(localName = "Description")
	@JacksonXmlCData
	private String description;
	
	/** 消息链接 */
	@JacksonXmlProperty(localName = "Url")
	@JacksonXmlCData
	private String url;

	/**
	 * 获取消息标题
	 * @return 消息标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置消息标题
	 * @param title 消息标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取消息描述
	 * @return 消息描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置消息描述
	 * @param description 消息描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取消息链接
	 * @return 消息链接
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置消息链接
	 * @param url 消息链接
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
