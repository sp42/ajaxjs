package com.github.chaijunkun.wechat.common.callback.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 用来接收回调xml时分析类型的bean定义
 * @author chaijunkun
 * @since 2016年9月6日
 */
@JacksonXmlRootElement(localName="xml")
public class TypeAnalyzingBean {
	
	/** 消息类型 */
	@JacksonXmlProperty(localName = "MsgType")
	@JacksonXmlCData
	private String msgType;
	
	/** 事件类型 */
	@JacksonXmlProperty(localName = "Event")
	@JacksonXmlCData
	private String event;

	/**
	 * 获取消息类型
	 * @return 消息类型
	 */
	public String getMsgType() {
		return msgType;
	}

	/**
	 * 设置消息类型
	 * @param msgType 消息类型
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * 获取事件类型
	 * @return 事件类型
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * 设置事件类型
	 * @param event 事件类型
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	
}
