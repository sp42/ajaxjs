package com.ajaxjs.cms.app.catelog;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseBean;

/**
 * 类别
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class Catelog extends BaseModel implements IBaseBean {

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

	/**
	 * 路径
	 */
	private String path;
}
