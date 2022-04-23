package com.ajaxjs.framework;

import java.io.Serializable;

/**
 * 表示一个实体
 * 
 * @author Frank Cheung
 *
 */
public interface IBaseModel {
	/**
	 * 获取实体 id
	 * 
	 * @return id 标识
	 */
	public Serializable getId();

//	 不能 setter
//	public void setId(Serializable id);
}
