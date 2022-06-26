package com.ajaxjs.api_helper.model;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;

public class Document extends BaseModel implements IBaseModel, Constant {
	private Long catalogId;

	private String description;

	private HttpMethod httpMethod;

	private String url;
}
