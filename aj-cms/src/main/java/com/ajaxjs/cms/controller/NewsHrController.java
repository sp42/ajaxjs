package com.ajaxjs.cms.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.common.TreeLikeService;
import com.ajaxjs.cms.common.AttachmentController.AttachmentService;
import com.ajaxjs.cms.service.NewsService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.QueryTools;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

@Component
@Path("/news")
public class NewsHrController extends BaseController<Map<String, Object>> {
	@Resource
	private NewsService service = new NewsService();

	@Resource
	private TreeLikeService treeLikeService = new TreeLikeService();

	@GET
	public String redirect() {
		return "redirect::news/";
	}

	@GET
	@Path("news")
	@MvcFilter(filters = DataBaseFilter.class)
	public String news(ModelAndView mv, @QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId) {
		mv.put("catalogs", treeLikeService.getAllChildrenAsMap(service.getDomainCatalogId()));
		mv.put("groupByMonth", service.groupByMonth(10));
		// mv.put("lastNews", service.findListTop(2));

		return autoOutput(service.list(catalogId, start, limit, CommonConstant.ON_LINE), mv, page("news-list"));
	}

	@GET
	@Path("news/" + ID_INFO)
	@MvcFilter(filters = { DataBaseFilter.class })
	public String newsInfo(@PathParam(ID) Long id, ModelAndView mv) {
		mv.put("catalogs", treeLikeService.getAllChildrenAsMap(service.getDomainCatalogId()));
		Map<String, Object> map = getService().findById(id);
		BaseService.getNeighbor(mv, "entity_article", id);

		if (ConfigService.getValueAsBool("domain.article.attachmentDownload"))
			map.put("attachment", new AttachmentService().findByOwner((long) map.get("uid")));

		return output(mv, map, page("news-info"));
	}

	@GET
	@Path("hr")
	@MvcFilter(filters = DataBaseFilter.class)
	public String index(ModelAndView mv) {
		mv.put("LIST", HrController.service.findList(QueryTools.setStatus(CommonConstant.ON_LINE)));
		return page("hr");
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}
}