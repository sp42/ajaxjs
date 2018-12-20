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

import com.ajaxjs.cms.ArticleService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/admin/article")
@Bean
public class ArticleAdminController extends BaseController<EntityMap>{
	@Resource("ArticleService")
	private ArticleService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, @QueryParam("catalogId") int catalogId, ModelAndView model) {
		if (catalogId == 0) {
			catalogId = ConfigService.getValueAsInt("data.articleCatalog_Id"); // 不指定实体的子分类
		}

		model.put("catelogId", ConfigService.getValueAsInt("data.articleCatalog_Id"));

		final int _catalogId = catalogId;
		super.list(start, limit, model, (s, l) -> service.findPagedListByCatalogId(_catalogId, start, limit));
		return adminList_CMS();
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		model.put("catelogId", ConfigService.getValueAsInt("data.articleCatalog_Id"));
		super.createUI(model);

		return infoUI_CMS();
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) {
		model.put("catelogId", ConfigService.getValueAsInt("data.articleCatalog_Id"));
		editUI(id, model, service);

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

}
