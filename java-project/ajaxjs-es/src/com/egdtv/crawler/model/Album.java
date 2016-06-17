package com.egdtv.crawler.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.ajaxjs.framework.model.Entity;
import com.ajaxjs.framework.model.EntityDescription;
import com.ajaxjs.framework.model.FieldDescription;

/**
 * 专辑
 * @author frank
 *
 */
@EntityDescription(doc="专辑，是为多笔记录之集合")
public class Album extends Entity {
	@FieldDescription(doc="采集的原页面地址") 
	@NotNull(message="采集的原页面地址不能为空")
	private String url;
	
	// 集数
	@FieldDescription(doc="专辑集数") 
	private Integer length;
	
	// 总播放量
	@FieldDescription(doc="专辑总播放量") 
	private Integer playHit;
	
	// 订阅量
	@FieldDescription(doc="专辑订阅量") 
	private Integer subscriptionAmount;
	
	@FieldDescription(doc="在原网站推出的日期") 
	private Date publishDate;
	
	@FieldDescription(doc="元数据：电影的年份之类的信息保存在这里") 
	private String metaData;
	
	// 上次更新
	@FieldDescription(doc="上次更新日期") 
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")  
	private Date lastUpdate;
	
	@FieldDescription(doc="上次更新记录之标题") 
	private String lastUpdateRecordName;
	
	// 截图
	@FieldDescription(doc="页面截图") 
	private String imgCut;
	
	// 是否运行中
	@FieldDescription(doc="爬虫是否运行中") 
	private boolean isRunning;
	
	// 当前状态
	@FieldDescription(doc="爬虫当前状态") 
	private String status;
	
	// 当前记录
	@FieldDescription(doc="爬虫当前采集中的那笔记录之标题或id") 
	private String itemId;
	
	// 爬虫 id
	@FieldDescription(doc="爬虫 id") 
	private Integer crawlerId;
	
	// 爬虫 类型
	@FieldDescription(doc="爬虫类型：0=视频、1=视频、2=音频")
	private int crawlerType;
	
	// 门户 id
	@FieldDescription(doc="门户 id")
	private int portalId;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String getImgCut() {
		return imgCut;
	}

	public void setImgCut(String imgCut) {
		this.imgCut = imgCut;
	}

	public Integer getCrawlerId() {
		return crawlerId;
	}

	public void setCrawlerId(Integer crawlerId) {
		this.crawlerId = crawlerId;
	}

	public int getPortalId() {
		return portalId;
	}

	public void setPortalId(int protalId) {
		this.portalId = protalId;
	}

	public int getCrawlerType() {
		return crawlerType;
	}

	public void setCrawlerType(int crawlerType) {
		this.crawlerType = crawlerType;
	}

	public Integer getPlayHit() {
		return playHit;
	}

	public void setPlayHit(Integer playHit) {
		this.playHit = playHit;
	}

	public String getLastUpdateRecordName() {
		return lastUpdateRecordName;
	}

	public void setLastUpdateRecordName(String lastUpdateRecordName) {
		this.lastUpdateRecordName = lastUpdateRecordName;
	}

	public Integer getSubscriptionAmount() {
		return subscriptionAmount;
	}

	public void setSubscriptionAmount(Integer subscriptionAmount) {
		this.subscriptionAmount = subscriptionAmount;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
}
