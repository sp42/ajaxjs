package com.github.chaijunkun.wechat.common.callback.xml.msg;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 语音消息
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class VoiceMsg extends MediaMsg {
	
	/** 语音格式，如amr，speex等 */
	@JacksonXmlProperty(localName = "Format")
	@JacksonXmlCData
	private String format;
	
	/** 语音识别后的文字 */
	@JacksonXmlProperty(localName = "Recognition")
	@JacksonXmlCData
	private String recognition;

	/**
	 * 获取语音格式
	 * @return 语音格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置语音格式
	 * @param format 语音格式
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * 获取语音识别后的文字
	 * @return 语音识别后的文字
	 */
	public String getRecognition() {
		return recognition;
	}

	/**
	 * 设置语音识别后的文字
	 * @param recognition 语音识别后的文字
	 */
	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}
	
}
