package com.github.chaijunkun.wechat.common.callback.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 安全模式下收到的消息
 * @author chaijunkun
 * @since 2016年9月7日
 */
@JacksonXmlRootElement(localName="xml")
public class RxEncryptXML {
	
	/** 接收方微信号 */
	@JacksonXmlProperty(localName = "ToUserName")
	@JacksonXmlCData
	private String toUserName;
	
	/** 密文 */
	@JacksonXmlProperty(localName = "Encrypt")
	@JacksonXmlCData
	private String encrypt;

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
	 * 获取密文
	 * @return 密文
	 */
	public String getEncrypt() {
		return encrypt;
	}

	/**
	 * 设置密文
	 * @param encrypt 密文
	 */
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	
}
