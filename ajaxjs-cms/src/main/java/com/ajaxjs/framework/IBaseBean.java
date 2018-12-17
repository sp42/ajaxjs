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

	/**
	 * 返回数据库之间生成的原始数据，如果你不想心中 model，这使用这个属性获取数据，适合读操作，或者跨表查询时用
	 * 
	 * @return
	 */
	public Map<String, Object> getData();
}
