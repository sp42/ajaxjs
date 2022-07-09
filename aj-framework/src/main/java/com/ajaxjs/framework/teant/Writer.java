package com.ajaxjs.framework.teant;

import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.model.DataServiceConstant.CRUD;
import com.ajaxjs.data_service.plugin.IPlugin;

public class Writer implements IPlugin {
	@Override
	public boolean before(CRUD type, ServiceContext ctx) {
		if (type == CRUD.CREATE) {
			
		} 
		
		return true;
	}
}
