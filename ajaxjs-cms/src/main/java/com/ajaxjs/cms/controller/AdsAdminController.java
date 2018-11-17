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
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.simpleApp.CommonEntryAdminController;

@Path("/admin/ads")
@Bean("AdsAdminController")
public class AdsAdminController extends CommonController<Ads, Long> implements CommonEntryAdminController<Ads, Long> {

	@Resource("AdsService")
	private AdsService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("catalogId") int catalogId, @QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		if (catalogId == 0)
			catalogId = getService().getDomainCatelogId(); // 不指定实体的子分类

		prepareData(model);

		PageResult<Ads> adsPage = getService().findPagedListByCatelogId(catalogId, getParam(), start, limit);

		model.put("PageResult", adsPage);
		model.put("domainCatalog_Id", getService().getDomainCatelogId());
		return "/test/test";
//		return jsp_perfix + "/common-entity/ads-list";
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) {
		model.put("domainCatalog_Id", getService().getDomainCatelogId());
		info(id, model);
		super.editUI(model);
		return "/test/test2";
//		return jsp_perfix +"/common-entity/ads";
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView model) {
		model.put("domainCatalog_Id", getService().getDomainCatelogId());
		super.createUI(model);
		return "/test/test2";
//		return jsp_perfix +"/entity/ads";
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Ads entity, ModelAndView model) {
		return super.create(entity, model);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Override
	public String update(@PathParam("id") Long id, Ads entity, ModelAndView model) {
		return super.update(id, entity, model);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		Ads ads = new Ads();
		ads.setId(id);
		return super.delete(ads, model);
	}

}
