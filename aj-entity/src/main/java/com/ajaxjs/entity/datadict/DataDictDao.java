package com.ajaxjs.entity.datadict;

import java.util.List;

import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.data_service.sdk.KeyOfMapParams;

public interface DataDictDao {
	interface DataDictDAO extends IDataService<DataDict> {
		@KeyOfMapParams("pid")
		public List<DataDict> getListByParentId(Long pid);

		@KeyOfMapParams("id")
		public Integer getDepthById(Long id);

		@KeyOfMapParams("ids")
		public Boolean deleteChildren(String ids);

	}

	public static final DataDictDAO DataDictDAO = new Caller("cms", "sys_datadict").bind(DataDictDAO.class, DataDict.class);
}
