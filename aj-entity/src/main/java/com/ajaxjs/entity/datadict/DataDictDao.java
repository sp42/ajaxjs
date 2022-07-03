package com.ajaxjs.entity.datadict;

import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;

public interface DataDictDao extends IDataService<DataDict> {
	
	public static final DataDictDao DataDictDAO =  new Caller("drone", "aip_recognition_task").bind(DataDictDao.class, DataDict.class);
}
