package com.ajaxjs.framework;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface IBaseBean extends Serializable {
	public Long getId();

	public void setId(Long id);

	public Date getCreateDate();

	public void setCreateDate(Date createDate);

	public Date getUpdateDate();

	public void setUpdateDate(Date updateDate);

	public Long getUid();

	public void setUid(Long uid);

}
