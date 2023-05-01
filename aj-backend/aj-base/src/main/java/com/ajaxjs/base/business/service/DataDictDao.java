package com.ajaxjs.base.business.service;

import java.util.List;

import com.ajaxjs.base.business.model.DataDict;


public interface DataDictDao  {
	public List<DataDict> getListByParentId(Long pid);

	public Integer getDepthById(Long id);

	public Boolean deleteChildren(String ids);

}
