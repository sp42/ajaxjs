package com.ajaxjs.data_service.plugin;

import com.ajaxjs.Version;
import com.ajaxjs.data_service.model.DataServiceConstant.CRUD;
import com.ajaxjs.data_service.model.ServiceContext;

/**
 *
 */
public class ReadOnlyPlugin implements IPlugin {
	@Override
	public boolean before(CRUD type, ServiceContext ctx) {
		if (Version.isDebug)
			return true;

		if (type == CRUD.CREATE || type == CRUD.DELETE || type == CRUD.UPDATE) {
			ctx.setErrMsg("演示版本禁止所有写操作");

			return false;
		} else
			return true;

	}
}
