package com.ajaxjs.cms.controller;

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

import com.ajaxjs.app.CommonConstant;
import com.ajaxjs.cms.CommonEntityService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Path("/topic")
public class TopicController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(TopicController.class);
	
	public static CommonEntityService service = new CommonEntityService("entity_topic", "data.topicCatalog_Id", "专题", "topic");

	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(CATALOG_ID) int catalogId, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		return page(mv, service.findPagedList(catalogId, start, limit, CommonConstant.ON_LINE, true));
	}

	@GET
	@Path("listJson")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String listJson(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		return toJson(list(catalogId, start, limit, mv));
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	@Override
	public void prepareData(ModelAndView mv) {
		mv.put("domainCatalog_Id", service.getDomainCatalogId());
		super.prepareData(mv);
	}

	//////////////////// 后台 ///////////////////

	@GET
	@Path("/admin/topic/list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String adminList(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		LOGGER.info("专题后台列表");
		return page(mv, service.findPagedList(catalogId, start, limit, CommonConstant.OFF_LINE, false), true);
	}

	@GET
	@Path("/admin/topic")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView mv) {
		return super.createUI(mv);
	}

	@POST
	@Path("/admin/topic")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/topic/{id}")
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		return editUI(mv, service.findById(id));
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/topic/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path("/admin/topic/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new HashMap<String, Object>());
	}
}
