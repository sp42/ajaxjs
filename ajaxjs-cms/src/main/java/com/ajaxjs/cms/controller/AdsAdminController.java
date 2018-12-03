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
import com.ajaxjs.orm.dao.PageResult;

@Path("/admin/ads")
@Bean("AdsAdminController")
public class AdsAdminController extends CommonController<Ads, Long> implements CommonEntryAdminController<Ads, Long> {
	@Override
	public void prepareData(ModelAndView model) {
		model.put("uiName", "广告");
		model.put("tableName", "ads");
	}
	
	@Resource("AdsService")
	private AdsService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("catalogId") int catalogId, @QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		if (catalogId == 0)
			catalogId = service.getDomainCatelogId(); // 不指定实体的子分类

		prepareData(model);

		PageResult<Ads> adsPage = service.findPagedListByCatelogId(catalogId, start, limit);

		model.put("PageResult", adsPage);
		model.put("domainCatalog_Id", service.getDomainCatelogId());
		return jsp_perfix + "/common-entity/ads-list";
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) {
		model.put("domainCatalog_Id", service.getDomainCatelogId());
		info(id, model, _id -> service.findById(id));
		editUI(model);
		return jsp_perfix +"/common-entity/ads";
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView model) {
		model.put("domainCatalog_Id", service.getDomainCatelogId());
		super.createUI(model);
		return jsp_perfix +"/entity/ads";
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Ads entity, ModelAndView model) {
		return create(entity, model, e -> service.create(e));
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Override
	public String update(@PathParam("id") Long id, Ads entity, ModelAndView model) {
		return update(id, entity, model, e -> service.update(e));
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		return delete(id, new Ads(), model, e -> service.delete(e));
	}

	@Override
	public String list(int start, int limit, ModelAndView model) {
		return null;
	}

}
