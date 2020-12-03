package com.ajaxjs.cms.section;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.common.Catalog;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

@Path("/admin/cms/section")
@Component
public class SectionController extends BaseController<Section> {
	@Resource
	private SectionService service;

//	@GET
//	@Path("list")
//	@MvcFilter(filters = DataBaseFilter.class)
//	public String list(@QueryParam("sectionId") int sectionId, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
//		return toJson(service.findListBySectionId(start, limit, sectionId));
//	}

	/**
	 * 把配置还原为 JSON 输出给前端
	 * 
	 * @param mv
	 * @return
	 */
	public static String getEntityProfile(ModelAndView mv) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) ConfigService.CONFIG.get("data");
		String json = toJson(map.get("entityProfile"), false);

		if (mv != null)
			mv.put("ENTITY_PROFILE", json);

		return json;
	}

	@GET
	public String jsp(ModelAndView mv) {
		getEntityProfile(mv);

//		mv.put("entryIdNameJson", toJson(SectionService.TYPE_NAME, false));

		prepareData(mv);
		return page("section-list");
	}

//	@GET
//	@MvcFilter(filters = DataBaseFilter.class)
//	@Path("sections")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String sections() {
//		List<Catalog> c = service.findSectionCatalog();
//		return toJson(c);
//	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Section entity) {
		return super.create(entity);
	}

	@PUT
	@Path(ID_INFO)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Section entity) {
		return super.update(id, entity);
	}

//	@GET
//	@MvcFilter(filters = DataBaseFilter.class)
//	@Path("/section/{id}")
//	public String getSectionList(@PathParam(ID) int sectionId, @QueryParam(START) int start, @QueryParam(LIMIT) int limit) {
//		return toJson(service.findListBySectionId(start, limit, sectionId));
//	}

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
		return delete(id, new Section());
	}

	@Override
	public IBaseService<Section> getService() {
		return service;
	}
}
