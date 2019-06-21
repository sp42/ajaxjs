package cn.nike;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.app.ArticleService;
import com.ajaxjs.cms.app.catalog.Catalog;
import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.cms.app.catalog.CatalogServiceImpl;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean()
@Path("/news")
public class NewsController extends BaseController<Map<String, Object>> {
	@Resource("ArticleService")
	private ArticleService service;

	@GET
	@Path("/api")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catelogId,
			ModelAndView model) {
		return toJson(service.findPagedListByCatelogId(catelogId, start, limit));
	}

	@GET
	@Path("/catelogList")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String getCatelogList(ModelAndView model) {
		int catalogId = ConfigService.getValueAsInt("data.articleCatalog_Id");
		CatalogService catelogService = new CatalogServiceImpl();
		List<Catalog> catelogs = catelogService.findAllListByParentId(catalogId, false);

		return toJson(catelogs);
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	public String newsInfo(@PathParam("id") Long id, ModelAndView mv) {
		mv.put("info", service.findById(id));
		return "/news/newsInfo";
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}
