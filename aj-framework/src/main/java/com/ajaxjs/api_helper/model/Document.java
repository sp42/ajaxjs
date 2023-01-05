package com.ajaxjs.api_helper.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;

public class Document extends BaseModel implements IBaseModel, Constant {
	private Long catalogId;

	private String description;

	private HttpMethod httpMethod;

	private String url;

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
