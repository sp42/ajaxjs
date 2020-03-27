package com.ajaxjs.user.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.app.CommonConstant;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.util.logger.LogHelper;

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
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		LOGGER.info("全局操作日志");
		
		mv.put("LoginType", UserDict.LOGIN_TYPE);
		page(mv, service.findPagedList(start, limit, BaseService::betweenCreateDate), CommonConstant.UI_ADMIN);

		return jsp("user/global-log-list");
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

}
