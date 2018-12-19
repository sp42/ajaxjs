package com.ajaxjs.framework;

import java.util.Date;
import java.util.HashMap;

public class EntityMap extends HashMap<String, Object> implements IBaseBean {
	private static final long serialVersionUID = -6572963539115171049L;

	@Override
	public Long getId() {
		Object id = get("id");
		return (Long) id;
	}

	@Override
	public void setId(Long id) {
		put("id", id);
	}

	@Override
	public Date getCreateDate() {
		Object d = get("createDate");
		return (Date) d;
	}

	@Override
	public void setCreateDate(Date createDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public Date getUpdateDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUpdateDate(Date updateDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getUid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUid(Long uid) {
		// TODO Auto-generated method stub

	}

}
