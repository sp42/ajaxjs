package com.ajaxjs.cms.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.app.domain.DomainBaseAdminController;
import com.ajaxjs.cms.app.domain.DomainEntityService;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/hr")
public class HrAdminController extends DomainBaseAdminController {
	DomainEntityService service = new DomainEntityService("entity_hr", "data.hrCatalog_Id", "招聘", "hr");

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catelogId, ModelAndView mv) {
		if (catelogId == 0) 
			catelogId = service.getDomainCatelogId(); // 不指定实体的子分类

		final int _catelogId = catelogId;
		listPaged(start, limit, mv, (s, l) -> service.findPagedListByCatelogId(_catelogId, start, limit));
		return domainEntityList;
	}

	@Override
	public void prepareData(ModelAndView mv) {
		mv.put("domainCatalog_Id", service.getDomainCatelogId());
		super.prepareData(mv);
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
	
	@GET
	@Override
	@MvcFilter(filters = DataBaseFilter.class)
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return cms("hr");
	}
	
	@GET
	@Override
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return cms("hr");
	}
}
