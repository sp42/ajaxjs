package com.ajaxjs.cms.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.domain.DomainBaseController;
import com.ajaxjs.cms.domain.DomainEntity;
import com.ajaxjs.cms.domain.DomainEntityService;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/hr")
public class HrAdminController extends DomainBaseController {
	DomainEntityService service = new DomainEntityService("entity_hr", "data.hrCatalog_Id", "招聘", "hr");

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, @QueryParam("catalogId") int catalogId, ModelAndView mv) {
		if (catalogId == 0) 
			catalogId = service.getDomainCatelogId(); // 不指定实体的子分类

		final int _catalogId = catalogId;
		listPaged(start, limit, mv, (s, l) -> service.findPagedListByCatelogId(_catalogId, start, limit));
		return domainEntityList;
	}

	@Override
	public void prepareData(ModelAndView mv) {
		mv.put("domainCatalog_Id", service.getDomainCatelogId());
		super.prepareData(mv);
	}

	@Override
	public IBaseService<DomainEntity> getService() {
		return service;
	}
}
