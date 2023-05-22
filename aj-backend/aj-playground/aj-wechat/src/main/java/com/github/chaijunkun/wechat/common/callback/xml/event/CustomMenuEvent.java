package com.github.chaijunkun.wechat.common.callback.xml.event;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 自定义菜单事件
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class CustomMenuEvent extends BaseEvent {
	
	/** 事件KEY值，与自定义菜单接口中KEY值对应 */
	@JacksonXmlProperty(localName = "EventKey")
	@JacksonXmlCData
	private String eventKey;

	/**
	 * 获取事件KEY值
	 * @return 事件KEY值
	 */
	public String getEventKey() {
		return eventKey;
	}

	/**
	 * 设置事件KEY值
	 * @param eventKey 事件KEY值
	 */
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	
}
