package com.egdtv.crawler.model;

import javax.validation.constraints.NotNull;

import com.ajaxjs.framework.model.Entity;
import com.ajaxjs.framework.model.FieldDescription;

/**
 * 采集基类
 * 
 * @author frank
 *
 */
public abstract class NetItem extends Entity {
	/**
	 * 年份
	 */
	private int year;
	
	/**
	 * 地区
	 */
	private int[] area;
	
	/**
	 * 语言
	 */
	private int[] language;
	
	private String[] keywords;
	
	@FieldDescription(doc="播放的源地址") 
	private String sourceUrl; // 源地址
	
//	@FieldDescription(doc="采集类型：文章、视频、音频") 
//	private String type;
	
	@FieldDescription(doc="采集的原页面地址") 
	private String ownerUrl;	// 页面地址
	
	@FieldDescription(doc="播放时长") 
	private String duration; // 时长
	
	@FieldDescription(doc="播放量，一共播放了多少次") 
	private int playHit; 	 // 播放量

	@FieldDescription(doc="在原网站推出的日期")
	private String publishDate; // 出版日期
	
	@FieldDescription(doc="元数据：电影的导演之类的信息保存在这里") 
	private String metaData;
	
	@FieldDescription(doc="其他参数，可保存属于被采集方的其他参数")
	private String misc;// 其他字段
	
	@FieldDescription(doc="true=要播放前才去抓源地址；false=不需要实时抓")
	private boolean isLiveSource;// 是否实时抓源

	// 门户 id
	@FieldDescription(doc="门户 id")
	@NotNull(message="portalId 不能为空")
	private int portalId;
	
	// 专辑 id
	@FieldDescription(doc="属于哪个专辑，关联到那个专辑的 id")
	private Integer albumId;

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getPortalId() {
		return portalId;
	}

	public void setPortalId(int portalId) {
		this.portalId = portalId;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public String getOwnerUrl() {
		return ownerUrl;
	}

	public void setOwnerUrl(String ownerUrl) {
		this.ownerUrl = ownerUrl;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public int getPlayHit() {
		return playHit;
	}

	public void setPlayHit(int playHit) {
		this.playHit = playHit;
	}

	public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}

	public boolean isLiveSource() {
		return isLiveSource;
	}

	public void setLiveSource(boolean isLiveSource) {
		this.isLiveSource = isLiveSource;
	}

	/**
	 * @return {@link #keywords}
	 */
	public String[] getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords {@link #keywords}
	 */
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return {@link #year}
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year {@link #year}
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return {@link #area}
	 */
	public int[] getArea() {
		return area;
	}

	/**
	 * @param area {@link #area}
	 */
	public void setArea(int[] area) {
		this.area = area;
	}

	/**
	 * @return {@link #language}
	 */
	public int[] getLanguage() {
		return language;
	}

	/**
	 * @param language {@link #language}
	 */
	public void setLanguage(int[] language) {
		this.language = language;
	}
}
