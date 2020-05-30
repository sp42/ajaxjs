package com.ajaxjs.app;

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

import com.ajaxjs.app.attachment.AttachmentService;
import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.framework.filter.FrontEndOnlyCheck;
import com.ajaxjs.framework.filter.XslMaker;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.net.http.PicDownload;
import com.ajaxjs.orm.SnowflakeIdWorker;
import com.ajaxjs.util.logger.LogHelper;

@Bean
@Path("/article")
public class ArticleController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(ArticleController.class);

	@Resource("ArticleService")
	private ArticleService service;

	@Override
	public ArticleService getService() {
		return service;
	}

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
//	@Authority(filter = DataBaseFilter.class, value = 1)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		LOGGER.info("图文列表-前台");
		getService().showList(mv);
		prepareData(mv);
		return page(mv, getService().list(catalogId, start, limit, CommonConstant.ON_LINE), false);
	}

	@GET
	@Path("listJson")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String listJson(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		return toJson(getService().list(catalogId, start, limit, CommonConstant.ON_LINE));
	}

	@GET
	@Path(ID_INFO)
	@MvcFilter(filters = { DataBaseFilter.class, FrontEndOnlyCheck.class })
	public String getInfo(@PathParam(ID) Long id, ModelAndView mv) {
		LOGGER.info("图文详情-前台");
		Map<String, Object> map = getService().findById(id);
		prepareData(mv);
		mv.put(INFO, map);
		BaseService.getNeighbor(mv, "entity_article", id);
		getService().showInfo(mv, id);

		if (ConfigService.getValueAsBool("domain.article.attachmentDownload"))
			map.put("attachment", new AttachmentService().findByOwner((long) map.get("uid")));

		return info();
	}

	@Override
	public void prepareData(ModelAndView mv) {
		int catalogId = getService().getDomainCatalogId();
		Map<Long, BaseModel> map = CatalogService.list_bean2map_id_as_key(new CatalogService().findAllListByParentId(catalogId));
		mv.put("newsCatalogs", map);
		mv.put(DOMAIN_CATALOG_ID, catalogId);

		super.prepareData(mv);
	}

	//////////////////// 后台 ///////////////////

	@GET
	@Path("/admin/{root}/list")
	@MvcFilter(filters = { DataBaseFilter.class, XslMaker.class })
	public String adminList(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		PageResult<Map<String, Object>> list = getService().list(catalogId, start, limit, CommonConstant.OFF_LINE);

		return autoOutput(list, mv, page("article"));
	}

	@GET
	@Path("/admin/{root}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView mv) {
		return super.createUI(mv);
	}

	@POST
	@Path("/admin/{root}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/{root}/{id}")
	public String editUI(@PathParam(ID) Long id, ModelAndView mv) {
		return editUI(mv, getService().findById(id));
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/admin/{root}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path("/admin/{root}/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new HashMap<String, Object>());
	}

	// 下载远程图片到本地服务器
	@POST
	@Path("/admin/{root}/downAllPics")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String downAllPics(@NotNull @FormParam("pics") String pics, MvcRequest r) {
		String[] arr = pics.split("\\|");

		new PicDownload(arr, r.mappath("/images"), () -> SnowflakeIdWorker.getId() + "").start();

		return toJson(new Object() {
			@SuppressWarnings("unused")
			public String[] pics = arr;
		});
	}
}