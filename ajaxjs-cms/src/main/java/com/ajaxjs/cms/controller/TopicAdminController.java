package com.ajaxjs.cms.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.domain.DomainBaseAdminController;
import com.ajaxjs.cms.domain.DomainEntityService;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/topic")
public class TopicAdminController extends DomainBaseAdminController {
	public static DomainEntityService service = new DomainEntityService("entity_topic", "data.topicCatalog_Id", "专题", "topic");

	@GET
	@Path(list)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(catalogId) int catalogId, @QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		listPaged(start, limit, mv, (s, l) -> service.findPagedListByCatelogId(catalogId, start, limit));
		return domainEntityList;
	}

	@GET
	@Path("listJson")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String listJson(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catalogId, ModelAndView mv) {
		return pagedListJson(listPaged(start, limit, mv, (s, l) -> service.findPagedListByCatelogId(catalogId, start, limit)));
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	@Override
	public void prepareData(ModelAndView mv) {
		mv.put("domainCatalog_Id", service.getDomainCatelogId());
		super.prepareData(mv);
	}
}
