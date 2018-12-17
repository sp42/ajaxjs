package com.ajaxjs.cms.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.model.Ads;
import com.ajaxjs.cms.service.AdsService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/admin/ads")
@Bean
public class AdsAdminController extends CommonController<Ads, Long> implements CommonEntryAdminController<Ads, Long> {
	{
		setUiName("广告");
		setTableName("ads");
	}

	@Resource("AdsService")
	private AdsService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("catalogId") int catalogId, @QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView mv) {
		if (catalogId == 0)
			catalogId = service.getDomainCatelogId(); // 不指定实体的子分类
		mv.put("domainCatalog_Id", catalogId);
		
		final int _catalogId = catalogId;
		super.list(start, limit, mv, (s, l) -> service.findPagedListByCatelogId(_catalogId, start, limit));
		return adminList_CMS();
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		mv.put("domainCatalog_Id", service.getDomainCatelogId());
		super.createUI(mv);

		return infoUI_CMS();
	}
	
	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/{id}")
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		mv.put("domainCatalog_Id", service.getDomainCatelogId());
		editUI(id, mv, service);
		return infoUI_CMS();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Ads entity) {
		return create(entity, service);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Ads entity) {
		return update(id, entity, service);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id) {
		return delete(id, new Ads(), service);
	} 

	@Override
	public String list(int start, int limit, ModelAndView model) {
		return null;
	}

}
