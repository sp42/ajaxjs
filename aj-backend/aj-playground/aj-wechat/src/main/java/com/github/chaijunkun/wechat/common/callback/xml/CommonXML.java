package com.github.chaijunkun.wechat.common.callback.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 公用xml映射字段
 * @author chaijunkun
 * @since 2016年8月31日
 */
@JacksonXmlRootElement(localName="xml")
public class CommonXML {
	
	public static enum MsgType {
		/** 文本消息 */
		text("text"),
		/** 图片消息 */
		image("image"),
		/** 语音消息 */
		voice("voice"),
		/** 视频消息 */
		video("video"),
		/** 小视频消息 */
		shortvideo("shortvideo"),
		/** 地理位置消息 */
		location("location"),
		/** 链接消息 */
		link("link"),
		/** 事件消息 */
		event("event"),
		/** 未知类型 */
		unknown("unknown");
		
		/** 消息类型 */
		private String type;
		
		private MsgType(String type){
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
		public static MsgType getByType(String type){
			MsgType[] values = MsgType.values();
			for (MsgType value : values) {
				if (value.getType().equals(type)){
					return value;
				}
			}
			return MsgType.unknown;
		}
		
	}
	
	/** 接收方微信号 */
	@JacksonXmlProperty(localName = "ToUserName")
	@JacksonXmlCData
	private String toUserName;
	
	/** 发送方微信号，若为普通用户，则是一个OpenID */
	@JacksonXmlProperty(localName = "FromUserName")
	@JacksonXmlCData
	private String fromUserName;
	
	/** 消息创建时间 */
	@JacksonXmlProperty(localName = "CreateTime")
	private Long createTime;
	
	/**
	 * 消息类型
	 * @see MsgType
	 */
	@JacksonXmlProperty(localName = "MsgType")
	@JacksonXmlCData
	private String msgType;
	
	/**
	 * 获取接收方微信号
	 * @return 接收方微信号
	 */
	public String getToUserName() {
		return toUserName;
	}

	/**
	 * 设置接收方微信号
	 * @param toUserName 接收方微信号
	 */
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	/**
	 * 获取发送方微信号
	 * @return 发送方微信号
	 */
	public String getFromUserName() {
		return fromUserName;
	}

	/**
	 * 设置发送方微信号
	 * @param fromUserName 发送方微信号
	 */
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	/**
	 * 获取消息创建时间
	 * @return 消息创建时间
	 */
	public Long getCreateTime() {
		return createTime;
	}

	/**
	 * 设置消息创建时间
	 * @param createTime 消息创建时间
	 */
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

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

}
