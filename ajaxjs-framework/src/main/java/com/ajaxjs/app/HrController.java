package com.ajaxjs.app;

import java.util.HashMap;
import java.util.Map;

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
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.MapCRUDService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Path("/hr")
public class HrController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(HrController.class);

	public static MapCRUDService service = new MapCRUDService("entity_hr", "招聘", "hr");

	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(CATALOG_ID) int catalogId, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		LOGGER.info("招聘后台");
		mv.put(PAGE_RESULT, service.findPagedList(catalogId, start, limit, CommonConstant.ON_LINE, true));
		
		return admin("hr-admin-list");
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	//////////////////// 后台 ///////////////////

	@GET
	@Path("/admin/hr/list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String adminList(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		LOGGER.info("招聘后台列表");
		mv.put(PAGE_RESULT, service.findPagedList(0, start, limit, CommonConstant.OFF_LINE, false));
		prepareData(mv);
		
		return admin("hr-admin-list");
	}

	@GET
	@Path("/admin/hr")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		
		return admin("hr-edit");
	}

	@POST
	@Path("/admin/hr")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/hr/{id}")
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		editUI(mv, service.findById(id));
		
		return admin("hr-edit");
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/hr/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path("/admin/hr/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new HashMap<String, Object>());
	}
}
