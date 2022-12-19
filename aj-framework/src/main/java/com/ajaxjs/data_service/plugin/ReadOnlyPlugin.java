package com.ajaxjs.data_service.plugin;

import com.ajaxjs.Version;
import com.ajaxjs.data_service.DataServiceConstant;
import com.ajaxjs.data_service.model.ServiceContext;

/**
 *
 */
public class ReadOnlyPlugin implements IPlugin {
	@Override
	public boolean before(DataServiceConstant.CRUD type, ServiceContext ctx) {
		if (Version.isDebug)
			return true;

		if (type == DataServiceConstant.CRUD.CREATE || type == DataServiceConstant.CRUD.DELETE || type == DataServiceConstant.CRUD.UPDATE) {
			ctx.setErrMsg("演示版本禁止所有写操作");

			return false;
		} else
			return true;
	}
}
