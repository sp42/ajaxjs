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

import com.ajaxjs.app.CommonConstant;
import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.cms.Ads;
import com.ajaxjs.cms.AdsService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.logger.LogHelper;

@Path("/admin/ads")
@Bean
public class AdsController extends BaseController<Ads> {
	private static final LogHelper LOGGER = LogHelper.getLog(AdsController.class);
	
	@Resource("AdsService")
	private AdsService service;
	
	@Resource("CatalogService")
	private CatalogService catalogService;
	
	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String adminList(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		LOGGER.info("广告列表");
		
		mv.put("catalogs", CatalogService.list_bean2map_id_as_key(catalogService.findAllListByParentId(service.getDomainCatalogId())));
		return page(mv, service.findPagedList(catalogId, start, limit, CommonConstant.OFF_LINE, false));
	}
 
	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		return editUI(mv, service.findById(id));
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return editUI();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Ads entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Ads entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Ads());
	}

	@Override
	public void prepareData(ModelAndView mv) {
		mv.put(domainCatalog_Id, service.getDomainCatalogId());
		super.prepareData(mv);
	}

	@Override
	public IBaseService<Ads> getService() {
		return service;
	}

}