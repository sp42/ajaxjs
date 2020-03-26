package com.ajaxjs.cms.app.attachment;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.app.CommonConstant;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.logger.LogHelper;


/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/attachment")
public class AttachmentController extends BaseController<Attachement> {
	private static final LogHelper LOGGER = LogHelper.getLog(AttachmentController.class);

	@Resource("AttachmentService")
	private AttachmentService service;

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catalogId,
			ModelAndView mv) {
		LOGGER.info("附件列表-前台");
		prepareData(mv);
		return page(mv, service.findPagedList(catalogId, start, limit, CommonConstant.ON_LINE, true));
	}



	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Attachement entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Attachement entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new Attachement());
	}

	@Override
	public IBaseService<Attachement> getService() {
		return service;
	}
}
