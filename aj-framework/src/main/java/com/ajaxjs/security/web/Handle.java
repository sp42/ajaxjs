package com.ajaxjs.security.web;

/**
 * 对于敏感字符串该如何处理
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public enum Handle {
	/**
	 * 转义字符串
	 */
	TYPE_ESCAPSE,

	/**
	 * 删除字符串
	 */
	TYPE_DELETE;
}
