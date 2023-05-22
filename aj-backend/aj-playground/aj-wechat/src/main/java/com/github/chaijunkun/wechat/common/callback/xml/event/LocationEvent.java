package com.github.chaijunkun.wechat.common.callback.xml.event;

import java.math.BigDecimal;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 上报地理位置事件
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class LocationEvent extends BaseEvent {
	
	/** 地理位置纬度 */
	@JacksonXmlProperty(localName = "Latitude")
	private BigDecimal latitude;
	
	/** 地理位置经度 */
	@JacksonXmlProperty(localName = "Longitude")
	private BigDecimal longitude;
	
	/** 地理位置精度 */
	@JacksonXmlProperty(localName = "Precision")
	private BigDecimal precision;

	/**
	 * 获取地理位置纬度
	 * @return 地理位置纬度
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}

	/**
	 * 设置地理位置纬度
	 * @param latitude 地理位置纬度
	 */
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	/**
	 * 获取地理位置经度
	 * @return 地理位置经度
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}

	/**
	 * 设置地理位置经度
	 * @param longitude 地理位置经度
	 */
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	/**
	 * 获取地理位置精度
	 * @return 地理位置精度
	 */
	public BigDecimal getPrecision() {
		return precision;
	}

	/**
	 * 设置地理位置精度
	 * @param precision 地理位置精度
	 */
	public void setPrecision(BigDecimal precision) {
		this.precision = precision;
	}
	
}
