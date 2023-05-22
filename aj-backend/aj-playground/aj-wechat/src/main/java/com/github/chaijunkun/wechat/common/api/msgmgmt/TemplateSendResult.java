package com.github.chaijunkun.wechat.common.api.msgmgmt;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIRet;

/**
 * 发送模板消息返回值
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class TemplateSendResult extends WeChatAPIRet {
	
	private static final long serialVersionUID = -826143954840985069L;
	/** 发送成功的消息id */
	@JsonProperty(value = "msgid")
	private String msgId;

	/**
	 * 获取发送成功的消息id
	 * @return 发送成功的消息id
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * 设置发送成功的消息id
	 * @param msgId 发送成功的消息id
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
}
