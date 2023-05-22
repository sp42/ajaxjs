package com.github.chaijunkun.wechat.common.callback.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 安全模式下发送的消息
 * @author chaijunkun
 * @since 2016年9月12日
 */
@JacksonXmlRootElement(localName="xml")
public class TxEncryptXML {
	
	/** 加密消息 */
	@JacksonXmlProperty(localName = "Encrypt")
	@JacksonXmlCData
	private String encrypt;
	
	/** 消息签名 */
	@JacksonXmlProperty(localName = "MsgSignature")
	@JacksonXmlCData
	private String msgSignature;

	/** 时间戳 */
	@JacksonXmlProperty(localName = "TimeStamp")
	private Long timeStamp;
	
	/** 随机数 */
	@JacksonXmlProperty(localName = "Nonce")
	private String nonce;

	/**
	 * 获取加密消息
	 * @return 加密消息
	 */
	public String getEncrypt() {
		return encrypt;
	}

	/**
	 * 设置加密消息
	 * @param encrypt 加密消息
	 */
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	/**
	 * 获取消息签名
	 * @return 消息签名
	 */
	public String getMsgSignature() {
		return msgSignature;
	}

	/**
	 * 设置消息签名
	 * @param msgSignature 消息签名
	 */
	public void setMsgSignature(String msgSignature) {
		this.msgSignature = msgSignature;
	}

	/**
	 * 获取时间戳
	 * @return 时间戳
	 */
	public Long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 设置时间戳
	 * @param timeStamp 时间戳
	 */
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * 获取随机数
	 * @return 随机数
	 */
	public String getNonce() {
		return nonce;
	}

	/**
	 * 设置随机数
	 * @param nonce 随机数
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
}
