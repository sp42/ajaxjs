package com.ajaxjs.app.catalog;

import com.ajaxjs.sql.orm.BaseModel;

/**
 * 类别
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Catalog extends BaseModel {
	private static final long serialVersionUID = 7052077804355019403L;

	/**
	 * 父亲 id
	 */
	private Integer pid;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	/**
	 * 路径
	 */
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
