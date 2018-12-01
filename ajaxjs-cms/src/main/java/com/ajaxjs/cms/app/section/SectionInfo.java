package com.ajaxjs.cms.app.section;

import com.ajaxjs.framework.BaseModel;

/**
 * 栏目
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class SectionInfo extends BaseModel {
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private String path;
}
