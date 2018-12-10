 package com.ajaxjs.cms.controller;

import java.util.HashMap;
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

import com.ajaxjs.cms.service.HrService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/hr")
public class HrAdminController extends CommonController<Map<String, Object>, Long> implements CommonEntryAdminController<Map<String, Object>, Long> {
	@Resource("HrService")
	private HrService service;
	
	{
		setUiName("招聘");
		setTableName("hr");
	}

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, @QueryParam("catalogId") int catalogId, ModelAndView mv) {
		if (catalogId == 0) {
			catalogId = ConfigService.getValueAsInt("data.hrCatalog_Id"); // 不指定实体的子分类
		}

		mv.put("catelogId", ConfigService.getValueAsInt("data.hrCatalog_Id"));
		
		final int _catalogId = catalogId;
		super.list(start, limit, mv, (s, l) -> service.findPagedListByCatalogId(_catalogId, start, limit));
		return adminList_CMS();
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		model.put("catelogId", ConfigService.getValueAsInt("data.hrCatalog_Id"));
		super.createUI(model);

		return infoUI_CMS();
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		mv.put("catelogId", ConfigService.getValueAsInt("data.hrCatalog_Id"));
		editUI(id, mv, service);

		return infoUI_CMS();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return create(entity, service);
	}

	@PUT
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Map<String, Object> entity) {
		return update(id, entity, service);
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id) {
		return delete(id, new HashMap<String, Object>(), service);
	}

	@Override
	public String list(int start, int limit, ModelAndView mv) {
		return null;
	}
}
