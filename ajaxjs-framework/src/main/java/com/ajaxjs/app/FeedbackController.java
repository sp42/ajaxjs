package com.ajaxjs.app;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.filter.BeanValidator;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.captcha.CaptchaFilter;

@Bean
@Path("/admin/feedback")
public class FeedbackController extends BaseController<Feedback> {
	private static final LogHelper LOGGER = LogHelper.getLog(FeedbackController.class);

	@Resource("FeedbackService")
	private FeedbackService service;

	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String adminList(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		LOGGER.info("留言反馈列表");
		page(mv, service.findPagedList(catalogId, start, limit, CommonConstant.OFF_LINE, true));
		return admin("feedback-admin-list");
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		setInfo(id, mv);
		return admin("feedback-edit");
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Feedback entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Feedback());
	}

	//////////////////// 前台 ///////////////////
	@POST
	@Path("/feedback")
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class, BeanValidator.class })
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Feedback entity) {
		// 外界用户提交反馈，可能是游客
		return super.create(entity);
	}

	@Override
	public IBaseService<Feedback> getService() {
		return service;
	}
}
