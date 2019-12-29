package com.ajaxjs.cms.app;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.filter.FrontEndOnlyCheck;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.mvc.filter.XslMaker;
import com.ajaxjs.net.http.PicDownload;
import com.ajaxjs.orm.SnowflakeIdWorker;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Path("/news")
public class NewsController extends BaseController<Map<String, Object>> {
	
	private static final LogHelper LOGGER = LogHelper.getLog(NewsController.class);
	
	@Resource("autoWire:ioc.NewsService|NewsService")
	private NewsService service;

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
//	@Authority(filter = DataBaseFilter.class, value = 1)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catalogId, ModelAndView mv) {
		LOGGER.info("新闻列表-前台");
		service.showList(mv);
		return page(mv, service.list(catalogId, start, limit, CommonConstant.ON_LINE), false);
	}

	@GET
	@Path("listJson")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String listJson(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catalogId, ModelAndView mv) {
		return toJson(service.list(catalogId, start, limit, CommonConstant.ON_LINE));
	}

	@GET
	@Path(idInfo)
	@MvcFilter(filters = { DataBaseFilter.class, FrontEndOnlyCheck.class })
	public String getInfo(@PathParam(id) Long id, ModelAndView mv) {
		LOGGER.info("新闻详情-前台");
		
		prepareData(mv);
		mv.put(info, service.findById(id));
		BaseService.getNeighbor(mv, "entity_article", id);
		service.showInfo(mv, id);

		return info();
	}

	@Override
	public void prepareData(ModelAndView mv) {
		mv.put("domainCatalog_Id", service.getDomainCatalogId());
		super.prepareData(mv);
	}

	//////////////////// 后台 ///////////////////

	@GET
	@Path("/admin/news/list")
	@MvcFilter(filters = { DataBaseFilter.class, XslMaker.class })
	public String adminList(@QueryParam(start) int start, @QueryParam(limit) int limit, @QueryParam(catalogId) int catalogId, ModelAndView mv) {
		return page(mv, service.list(catalogId, start, limit, CommonConstant.OFF_LINE), true);
	}

	@GET
	@Path("/admin/news")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView mv) {
		return super.createUI(mv);
	}

	@POST
	@Path("/admin/news")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/news/{id}")
	@Override
	public String editUI(@PathParam(id) Long id, ModelAndView mv) {
		return super.editUI(id, mv);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/news/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path("/admin/news/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new HashMap<String, Object>());
	}

	// 下载远程图片到本地服务器
	@POST
	@Path("/admin/news/downAllPics")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String downAllPics(@NotNull @FormParam("pics") String pics, MvcRequest r) {
		System.out.println(pics);
		String[] arr = pics.split("\\|");

		new PicDownload(arr, r.mappath("/images"), () -> SnowflakeIdWorker.getId() + "").start();

		return toJson(new Object() {
			@SuppressWarnings("unused")
			public String[] pics = arr;
		});
	}
}
