package com.ajaxjs.shop.dep;

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

import com.ajaxjs.app.catalog.Catalog;
import com.ajaxjs.cms.SectionList;
import com.ajaxjs.cms.controller.DataDictController;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/admin/section")
@Bean
public class SectionController extends BaseController<SectionList> {
	
	@Resource("SectionService")
	private SectionService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("sectionId") int sectionId, @QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		
		return toJson(service.findSectionListBySectionId(start, limit, sectionId));
	}

	@GET
	public String jsp(ModelAndView mv) {
		mv.put("entryIdNameMap",  DataDictController.DataDictService.Entry_IdName);
		mv.put("entryIdNameJson", toJson(DataDictController.DataDictService.Entry_IdName, false));
		prepareData(mv);
		return page("section-list");
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
	@Path(idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, SectionList entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path(idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new SectionList());
	}

	@Override
	public IBaseService<SectionList> getService() {
		return service;
	}
}
