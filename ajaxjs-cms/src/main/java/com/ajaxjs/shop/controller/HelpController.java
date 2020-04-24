
package com.ajaxjs.shop.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.app.ArticleService;
import com.ajaxjs.app.CommonConstant;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 
 * 控制器
 */
@Bean
@Path("/help")
public class HelpController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(HelpController.class);

	private static final int HELP_CATALOGID = 31;

	@Resource("ArticleService")
	private ArticleService service;

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(ModelAndView mv, @QueryParam(START) int start, @QueryParam(LIMIT) int limit) {
		LOGGER.info("帮助列表");
		return autoOutput(mv, service.list(HELP_CATALOGID, start, limit, CommonConstant.ON_LINE), page("help-list"));
	}

	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String info(ModelAndView mv, @PathParam(ID) Long id) {
		LOGGER.info("帮助详情");
		Map<String, Object> map = getService().findById(id);
		return autoOutput(mv, map, page("help-info"));
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}
