package com.ajaxjs.sql.util.geo;

/**
 * 坐标
 * 
 * @author xinzhang
 *
 */
public class LocationPoint {
	/**
	 * 经度
	 */
	private Double longitude;

	/**
	 * 维度
	 */
	private Double latitude;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}
