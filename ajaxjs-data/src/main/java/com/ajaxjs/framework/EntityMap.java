package com.ajaxjs.framework;

import java.util.Date;
import java.util.HashMap;

public class EntityMap extends HashMap<String, Object> implements IBaseBean {
	private static final long serialVersionUID = -6572963539115171049L;

	@Override
	public Long getId() {
		Object id = get("id");
		if (id instanceof Integer)
			return ((Integer) id).longValue();

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
		put("createDate", createDate);
	}

	@Override
	public Date getUpdateDate() {
		Object d = get("updateDate");
		return (Date) d;
	}

	@Override
	public void setUpdateDate(Date updateDate) {
		put("updateDate", updateDate);
	}

	@Override
	public Long getUid() {
		Object uid = get("uid");
		return (Long) uid;
	}

	@Override
	public void setUid(Long uid) {
		put("uid", uid);
	}

}
