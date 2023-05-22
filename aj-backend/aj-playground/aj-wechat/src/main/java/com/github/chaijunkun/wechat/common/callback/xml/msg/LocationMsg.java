package com.github.chaijunkun.wechat.common.callback.xml.msg;

import java.math.BigDecimal;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 地理位置消息
 * @author chaijunkun
 * @since 2016年8月29日
 */
public class LocationMsg extends BaseMsg {
	
	/** 地理位置维度 */
	@JacksonXmlProperty(localName = "Location_X")
	private BigDecimal locationX;
	
	/** 地理位置经度 */
	@JacksonXmlProperty(localName = "Location_Y")
	private BigDecimal locationY;
	
	/** 地图缩放大小 */
	@JacksonXmlProperty(localName = "Scale")
	private Integer scale;
	
	/** 地理位置信息 */
	@JacksonXmlProperty(localName = "Label")
	@JacksonXmlCData
	private String label;

	/**
	 * 获取地理位置维度
	 * @return 地理位置维度
	 */
	public BigDecimal getLocationX() {
		return locationX;
	}

	/**
	 * 设置地理位置维度
	 * @param locationX 地理位置维度
	 */
	public void setLocationX(BigDecimal locationX) {
		this.locationX = locationX;
	}

	/**
	 * 获取地理位置经度
	 * @return 地理位置经度
	 */
	public BigDecimal getLocationY() {
		return locationY;
	}

	/**
	 * 设置地理位置经度
	 * @param locationY 地理位置经度
	 */
	public void setLocationY(BigDecimal locationY) {
		this.locationY = locationY;
	}

	/**
	 * 获取地图缩放大小
	 * @return 地图缩放大小
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * 设置地图缩放大小
	 * @param scale 地图缩放大小
	 */
	public void setScale(Integer scale) {
		this.scale = scale;
	}

	/**
	 * 获取地理位置信息
	 * @return 地理位置信息
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 设置地理位置信息
	 * @param label 地理位置信息
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
}
