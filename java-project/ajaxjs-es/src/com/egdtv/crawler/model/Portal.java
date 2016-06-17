package com.egdtv.crawler.model;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.FieldDescription;

public class Portal extends BaseModel {
	@FieldDescription(doc="门户网址") 
	private String url;
	
	@FieldDescription(doc="true=要播放前才去抓源地址；false=不需要实时抓") 
	private boolean isLiveSource;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isLiveSource() {
		return isLiveSource;
	}

	public void setLiveSource(boolean isLiveSource) {
		this.isLiveSource = isLiveSource;
	}
}
