package com.ajaxjs.auth.common;

import com.ajaxjs.auth.model.SystemModel.Systematic;
import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.data_service.sdk.KeyOfMapParams;

public interface AuthDao {
	interface SystematicDao extends IDataService<Systematic> {
		@KeyOfMapParams("name")
		Integer getLatestProcessVersion(String name);
	}

	public static final SystematicDao SystematicDAO = new Caller("systematic").bind(SystematicDao.class, Systematic.class);
}
