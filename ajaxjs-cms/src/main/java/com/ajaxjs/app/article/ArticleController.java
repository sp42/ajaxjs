package com.ajaxjs.app.article;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.ArticleService;
import com.ajaxjs.cms.DataDictService;
import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.cms.app.catalog.CatalogServiceImpl;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/article")
@Bean
public class ArticleController extends BaseController<Map<String, Object>> {
	@Resource("ArticleService")
	private ArticleService service;

	private CatalogService catelogService = new CatalogServiceImpl();

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String index(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catelogId, ModelAndView mv) {
		if (limit == 0)
			limit = 6;

		if (catelogId == 0)
			catelogId = ConfigService.getValueAsInt("data.articleCatalog_Id");

		mv.put(PageResult, service.findPagedListByCatelogId(catelogId, start, limit));
		prepareData(mv);
		return "/index.jsp";
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	public String newsInfo(@PathParam("id") Long id, ModelAndView mv) {
		prepareData(mv);
		mv.put("info", service.findById(id));
		BaseService.getNeighbor(mv, "entity_article", id);
		return "/index.jsp";
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	@Override
	public void prepareData(ModelAndView mv) {
		int catalogId = ConfigService.getValueAsInt("data.articleCatalog_Id");
		Map<Long, BaseModel> map = DataDictService.list_bean2map_id_as_key(catelogService.findByParentId(catalogId));
		mv.put("catalogs", map);
	}
}
