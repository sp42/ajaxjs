package com.github.chaijunkun.wechat.common.callback.xml.event;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class SubscribeEvent extends BaseEvent {
	
	/** 事件KEY值，qrscene_为前缀，后面为二维码的参数值 */
	@JacksonXmlProperty(localName = "EventKey")
	@JacksonXmlCData
	private String eventKey;
	
	/** 二维码的ticket，可用来换取二维码图片 */
	@JacksonXmlProperty(localName = "Ticket")
	@JacksonXmlCData
	private String ticket;

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

	/**
	 * 获取二维码的ticket
	 * @return 二维码的ticket
	 */
	public String getTicket() {
		return ticket;
	}

	/**
	 * 设置二维码的ticket
	 * @param ticket 二维码的ticket
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
}
