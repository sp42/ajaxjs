package com.github.chaijunkun.wechat.common.callback.xml.msg;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 文本消息
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class TextMsg extends BaseMsg {
	
	/** 文本消息内容 */
	@JacksonXmlProperty(localName = "Content")
	@JacksonXmlCData
	private String content;

	/**
	 * 获取文本消息内容
	 * @return 文本消息内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置文本消息内容
	 * @param content 文本消息内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
}
