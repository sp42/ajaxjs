package com.ajaxjs.zinc.model;

import java.util.Map;

import lombok.Data;

/**
 * Zinc API 返回的结果
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@Data
public class ZincResponse {
	/**
	 * 正常返回结果的信息
	 */
	private String message;

	/**
	 * 是否返回异常
	 */
	private Boolean hasError;

	/**
	 * 异常信息
	 */
	private String errMsg;

	/**
	 * 原始返回的 JSON
	 */
	private Map<String, Object> rawResult;
}
