package com.ajaxjs.data_service.model;

import com.ajaxjs.framework.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Project extends BaseModel {
	/**
	 * 配置，JSON 格式
	 */
	private String config;
}