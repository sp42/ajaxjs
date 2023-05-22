package com.github.chaijunkun.wechat.common.callback.xml.msg;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import com.github.chaijunkun.wechat.common.callback.xml.CommonXML;

/**
 * 基础消息定义
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class BaseMsg extends CommonXML {
	
	/** 消息id */
	@JacksonXmlProperty(localName = "MsgId")
	private Long msgId;

	/**
	 * 获取消息id
	 * @return 消息id
	 */
	public Long getMsgId() {
		return msgId;
	}

	/**
	 * 设置消息id
	 * @param msgId 消息id
	 */
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	
}
