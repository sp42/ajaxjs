package com.ajaxjs.app;

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
import com.ajaxjs.app.service.SectionList;
import com.ajaxjs.app.service.SectionService;
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
	public String list(@QueryParam("sectionId") int sectionId, @QueryParam(START) int start,
			@QueryParam(LIMIT) int limit, ModelAndView mv) {

		return toJson(service.findSectionListBySectionId(start, limit, sectionId));
	}

	@GET
	public String jsp(ModelAndView mv) {
		mv.put("entryIdNameMap", SectionService.TYPE_NAME);
		mv.put("entryIdNameJson", toJson(SectionService.TYPE_NAME, false));
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
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, SectionList entity) {
		return super.update(id, entity);
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/section/{id}")
	public String getSectionList(@PathParam(ID) int sectionId, @QueryParam(START) int start,
			@QueryParam(LIMIT) int limit) {
		return toJson(service.findSectionListBySectionId(sectionId, start, limit));
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/sub_section/{id}")
	public String getSubSection(@PathParam(ID) Long sectionId) {
		return "";
	}

	@DELETE
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new SectionList());
	}

	@Override
	public IBaseService<SectionList> getService() {
		return service;
	}
}
