package com.ajaxjs.cms.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 全局操作日志
 */
@Path("/admin/userGlobalLog")
public class GlobalLogController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(GlobalLogController.class);

	@TableName(value = "general_log", beanClass = Map.class)
	public static interface GlobalLogDao extends IBaseDao<Map<String, Object>> {
	}

	public static class GlobalLogService extends BaseService<Map<String, Object>> {
		public GlobalLogDao dao = new Repository().bind(GlobalLogDao.class);

		{
			setUiName("操作日志");
			setShortName("globalLog");
			setDao(dao);
		}
	}

	public static GlobalLogService service = new GlobalLogService();

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("全局操作日志");

		mv.put("LoginType", UserConstant.LOGIN_TYPE);

		return output(mv, service.findPagedList(start, limit, BaseService::betweenCreateDate), "jsp::user/global-log-list");
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

}
