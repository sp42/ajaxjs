package com.ajaxjs.cms.app.section;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.controller.CommonController;
import com.ajaxjs.cms.controller.CommonEntryAdminController;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/admin/section_info")
@Bean("SectionInfoAdminController")
public class SectionInfoController extends CommonController<SectionInfo, Long> implements CommonEntryAdminController<SectionInfo, Long> {
	@Resource("SectionInfoService")
	private SectionInfoService service;

	@GET
	@Override
	public String createUI(ModelAndView model) {
		prepareData(model);
		return jsp_perfix + "/system/Catelog";
	}

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return listJson(start, limit, model, (_start, _limit) -> service.findPagedList(_start, _limit));
	}
	
	@GET
	@Path("getListAndSubByParentId")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String getListAndSubByParentId(@QueryParam("parentId") int parentId, ModelAndView model) {
		return outputJson(service.getAllListByParentId(parentId), model);
	}

	@Override
	public String editUI(Long id, ModelAndView model) {
		return null;
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(SectionInfo entity, ModelAndView model) {
		return create(entity, model, _entity -> service.create(_entity));
	}

	@PUT
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, SectionInfo entity, ModelAndView model) {
		return update(id, entity, model, _entity -> service.update(_entity));
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		return delete(id, new SectionInfo(), model,  _entity -> service.delete(_entity));
	}

}
