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

import com.ajaxjs.cms.model.SectionInfo;
import com.ajaxjs.cms.service.section.SectionInfoService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.simpleApp.CommonEntryAdminController;

@Path("/admin/section_content")
@Bean("SectionContentAdminController")
public class SectionContentController extends CommonController<SectionInfo, Long> implements CommonEntryAdminController<SectionInfo, Long> {
	@Resource("SectionInfoService")
	private SectionInfoService service;

	@GET
	@Override
	public String createUI(ModelAndView model) {
		prepareData(model);
		return jsp_perfix_webinf + "/test";
	}

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return listJson(start, limit, model);
	}
	
	@GET
	@Path("getListAndSubByParentId")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String getListAndSubByParentId(@QueryParam("parentId") int parentId, ModelAndView model) {
		return outputListBeanAsJson(getService().getAllListByParentId(parentId));
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
		return super.create(entity, model);
	}

	@PUT
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, SectionInfo entity, ModelAndView model) {
		return super.update(id, entity, model);
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) throws ServiceException{
		SectionInfo catalog = new SectionInfo();
		catalog.setId(id);
		
		return super.delete(catalog, model);
	}

}
