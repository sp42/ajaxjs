package com.ajaxjs.shop.controller;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.DataDictService;
import com.ajaxjs.cms.SectionList;
import com.ajaxjs.cms.app.catalog.Catalog;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.service.SectionService;

@Path("/admin/section")
@Bean
public class SectionAdminController extends BaseController<SectionList> {
	
	@Resource("SectionService")
	private SectionService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("sectionId") int sectionId, @QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		
		return toJson(service.findSectionListBySectionId(start, limit, sectionId));
	}

	@Override
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		return show405;
	}

	@GET
	public String jsp(ModelAndView mv) {
		mv.put("entryIdNameMap",  DataDictService.Entry_IdName);
		mv.put("entryIdNameJson", toJson(DataDictService.Entry_IdName, false));
		prepareData(mv);
		return jsp("section-list");
	}
	
	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("sections")
	@Produces(MediaType.APPLICATION_JSON)
	public String sections() {
		List<Catalog> c = service.findSectionCatalog();
		return toJson(c);
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(SectionList entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, SectionList entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new SectionList());
	}

	@Override
	public IBaseService<SectionList> getService() {
		return service;
	}
}
