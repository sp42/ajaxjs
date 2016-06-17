package com.egdtv.crawler.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.ajaxjs.framework.model.Entity;

/**
 * 板块
 * @author frank
 *
 */
public class Section extends Entity {
	// 页面地址
		private String url;
 
		// 集数
		private int length;
		// 上次更新
		@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")  
		private Date lastUpdate;
		// 截图
		private String imgCut;
		// 是否运行中
		private boolean isRunning;
		// 当前状态
		private String status;
		// 当前记录
		private String itemId;
		// 爬虫 id
		private int crawlerId;
		// 爬虫 类型
		private int crawlerType;
		// 门户 id
		private int portalId;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
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

		public int getCrawlerId() {
			return crawlerId;
		}

		public void setCrawlerId(int crawlerId) {
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
}
