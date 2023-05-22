package com.github.chaijunkun.wechat.common.api.msgmgmt;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.chaijunkun.wechat.common.api.WeChatAPIParamWithToken;

/**
 * 发送模板消息参数
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class TemplateSendParam extends WeChatAPIParamWithToken {
	
	/**
	 * 模板数据
	 * @author chaijunkun
	 * @since 2016年9月6日
	 */
	public static class TemplateData {
		
		/** 模板值 */
		@JsonProperty(value = "value")
		private String value;
		
		/** 模板中该值使用的颜色 */
		@JsonProperty(value = "color")
		private String color;
		
		/**
		 * 模板数据默认构造方法
		 */
		public TemplateData() {
			super();
		}
		
		/**
		 * 带有默认值的模板数据构造方法
		 * @param value
		 */
		public TemplateData(String value) {
			super();
			this.value = value;
		}

		/**
		 * 同时设置默认值和默认颜色的模板数据构造方法
		 * @param value
		 * @param color
		 */
		public TemplateData(String value, String color) {
			super();
			this.value = value;
			this.color = color;
		}

		/**
		 * 获取模板值
		 * @return 模板值
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 设置模板值
		 * @param value 模板值
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * 获取模板中该值使用的颜色
		 * @return 模板中该值使用的颜色
		 */
		public String getColor() {
			return color;
		}

		/**
		 * 设置模板中该值使用的颜色
		 * @param color 模板中该值使用的颜色
		 */
		public void setColor(String color) {
			this.color = color;
		}
		
	}
	
	/** 接收消息的用户openid */
	@JsonProperty(value = "touser")
	private String toUser;
	
	/** 模板id */
	@JsonProperty(value = "template_id")
	private String templateId;
	
	/** 点开模板消息后打开的链接地址 */
	@JsonProperty(value = "url")
	private String url;
	
	/** 模板中的变量数据 */
	@JsonProperty(value = "data")
	private Map<String, TemplateData> data;

	/**
	 * 获取接收消息的用户openid
	 * @return 接收消息的用户openid
	 */
	public String getToUser() {
		return toUser;
	}

	/**
	 * 设置接收消息的用户openid
	 * @param toUser 接收消息的用户openid
	 */
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	/**
	 * 获取模板id
	 * @return 模板id
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * 设置模板id
	 * @param templateId 模板id
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * 获取点开模板消息后打开的链接地址
	 * @return 点开模板消息后打开的链接地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置点开模板消息后打开的链接地址
	 * @param url 点开模板消息后打开的链接地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取模板中的变量数据
	 * @return 模板中的变量数据
	 */
	public Map<String, TemplateData> getData() {
		return data;
	}

	/**
	 * 设置模板中的变量数据
	 * @param data 模板中的变量数据
	 */
	public void setData(Map<String, TemplateData> data) {
		this.data = data;
	}
	
}
