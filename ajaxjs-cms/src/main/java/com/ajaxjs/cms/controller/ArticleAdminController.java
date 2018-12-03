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

import com.ajaxjs.cms.service.ArticleService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.dao.PageResult;

@Path("/admin/article")
@Bean(value = "AdminArticleController")
public class ArticleAdminController extends CommonController<Map<String, Object>, Long> implements CommonEntryAdminController<Map<String, Object>, Long> {

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
		prepareData(model);
		PageResult<Map<String, Object>> pageResult = service.findPagedListByCatalogId(catalogId, start, limit);
		model.put("PageResult", pageResult);

		return jsp_perfix + "/common-entity/article-list";
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		model.put("catelogId", ConfigService.getValueAsInt("data.articleCatalog_Id"));
		super.createUI(model);

		return jsp_perfix + "/common-entity/article";
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) {
		model.put("catelogId", ConfigService.getValueAsInt("data.articleCatalog_Id"));
		info(id, model, _id -> service.findById(_id));
		editUI(model);

		return jsp_perfix + "/common-entity/article";
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity, ModelAndView model) {
		return create(entity, model, e -> service.create(e));
	}

	@PUT
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Map<String, Object> entity, ModelAndView model) {
		return update(id, entity, model, e -> service.update(e));
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		return delete(id, new HashMap<String, Object>(), model, e -> service.delete(e));
	}

	@Override
	public String list(int start, int limit, ModelAndView model) {
		return null;
	}
}
