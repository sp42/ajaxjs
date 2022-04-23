package com.ajaxjs.framework.log;

import java.sql.Connection;
import java.sql.SQLException;

import com.ajaxjs.data_service.model.DataServiceConstant.CRUD;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 *
 */
public class Log_DataServicePlugin implements IPlugin {
	private static final LogHelper LOGGER = LogHelper.getLog(Log_DataServicePlugin.class);

	/**
	 * 是否读的操作都记录
	 */
	private Boolean logRead;

	@Override
	public boolean onRequest(CRUD type, ServiceContext ctx) {
		return true;
	}

	@Override
	public boolean before(CRUD type, ServiceContext ctx) {
		return true;
	}

	@Override
	public void after(CRUD type, ServiceContext ctx, Object result) {
		if ((type == CRUD.INFO || type == CRUD.LIST) && !logRead)
			return;

		try (Connection conn = ctx.getDatasource().getConnection()) {
			ServiceLog log = new ServiceLog();
			log.setName("读操作");
			log.setSql(ctx.getSql());
			log.setIp(WebHelper.getIp(ctx.getRequest()));
			JdbcHelper.createBean(conn, log, "sys_log");
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	public Boolean isLogRead() {
		return logRead;
	}

	public void setLogRead(Boolean logRead) {
		this.logRead = logRead;
	}
}
