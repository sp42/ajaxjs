package com.ajaxjs.data_service.plugin;

import com.ajaxjs.data_service.DataServiceConstant;
import com.ajaxjs.data_service.model.ServiceContext;

/**
 * 系统日志，记录所有写的操作
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Log_DataServicePlugin implements IPlugin {
//	private static final LogHelper LOGGER = LogHelper.getLog(Log_DataServicePlugin.class);

	/**
	 * 是否读的操作都记录
	 */
	private Boolean logRead = false;

	@Override
	public void after(DataServiceConstant.CRUD type, ServiceContext ctx, Object result) {
		if ((type == DataServiceConstant.CRUD.INFO || type == DataServiceConstant.CRUD.LIST || type == DataServiceConstant.CRUD.PAGE_LIST) && !logRead)
			return;

		/* TODO 是否改为 DS 的 DAO 写法 */
//		try (Connection conn = ctx.getDatasource().getConnection()) {
//			ServiceLog log = new ServiceLog();
//			log.setName("读操作");
//			log.setSql(ctx.getSql());
//			log.setIp(WebHelper.getIp(ctx.getRequest()));
//			JdbcHelper.createBean(conn, log, "`ajaxjs`.`sys_log`");
//		} catch (SQLException e) {
//			LOGGER.warning(e);
//		}
	}

	public Boolean isLogRead() {
		return logRead;
	}

	public void setLogRead(Boolean logRead) {
		this.logRead = logRead;
	}
}
