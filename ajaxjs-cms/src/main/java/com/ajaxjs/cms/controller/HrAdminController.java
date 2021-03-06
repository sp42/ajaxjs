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
import com.ajaxjs.orm.dao.PageResult;

@Path("/admin/hr")
@Bean("HrAdminController")
public class HrAdminController extends CommonController<Map<String, Object>, Long> implements CommonEntryAdminController<Map<String, Object>, Long> {
	@Resource("HrService")
	private HrService service;
	
	@Override
	public void prepareData(ModelAndView model) {
		model.put("uiName", "招聘");
		model.put("tableName", "hr");
	}

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, @QueryParam("catalogId") int catalogId, ModelAndView model) {
		if (catalogId == 0) {
			catalogId = ConfigService.getValueAsInt("data.hrCatalog_Id"); // 不指定实体的子分类
		}

		model.put("catelogId", ConfigService.getValueAsInt("data.hrCatalog_Id"));
		prepareData(model);
		PageResult<Map<String, Object>> pageResult = service.findPagedListByCatalogId(catalogId, start, limit);
		model.put("PageResult", pageResult);

		return jsp_perfix + "/common-entity/hr-list";
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		model.put("catelogId", ConfigService.getValueAsInt("data.hrCatalog_Id"));
		super.createUI(model);

		return jsp_perfix + "/common-entity/hr";
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) {
		model.put("catelogId", ConfigService.getValueAsInt("data.hrCatalog_Id"));
		info(id, model, _id -> service.findById(id));
		super.editUI(model);

		return jsp_perfix + "/common-entity/hr";
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity, ModelAndView model) {
		return create(entity, model, _entity -> service.create(_entity));
	}

	@PUT
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Map<String, Object> entity, ModelAndView model) {
		return update(id, entity, model, _entity -> service.update(_entity));
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		return delete(id, new HashMap<String, Object>(), model, entry -> service.delete(entry));
	}

	@Override
	public String list(int start, int limit, ModelAndView model) {
		return null;
	}
}
