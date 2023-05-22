package com.github.chaijunkun.wechat.common.callback.xml.event;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import com.github.chaijunkun.wechat.common.callback.xml.CommonXML;

/**
 * 基础时间
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class BaseEvent extends CommonXML {
	
	public static enum EventType{
		SubscribeEvent("subscribe"),
		ScanEvent("SCAN"),
		UnsubscribeEvent("unsubscribe"),
		LocationEvent("LOCATION"),
		CustomMenuEvent("CLICK"),
		unknown("unknown");
		/** 事件类型 */
		private String type;
		
		private EventType(String type){
			this.type = type;
		}
		
		/**
		 * 获取消息类型
		 * @return 消息类型
		 */
		public String getType() {
			return type;
		}
		
		/**
		 * 按类型名称获取类型枚举
		 * @param type 消息类型
		 * @return 类型枚举
		 */
		public static EventType getByType(String type){
			EventType[] values = EventType.values();
			for (EventType value : values) {
				if (value.getType().equals(type)){
					return value;
				}
			}
			return EventType.unknown;
		}
	}
	
	/**
	 * 事件类型
	 */
	@JacksonXmlProperty(localName = "Event")
	@JacksonXmlCData
	private String event;

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
