package com.ajaxjs.website.controller;

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

import com.ajaxjs.entity.service.TreeLikeService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.framework.filter.FrontEndOnlyCheck;
import com.ajaxjs.framework.filter.XslMaker;
import com.ajaxjs.net.http.PicDownload;
import com.ajaxjs.sql.SnowflakeIdWorker;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.user.filter.PrivilegeFilter;
import com.ajaxjs.user.role.RightConstant;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.MvcRequest;
import com.ajaxjs.web.mvc.filter.Authority;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.ajaxjs.website.service.ArticleService;
import com.ajaxjs.website.service.AttachmentService;

@Component
@Path("/cms/article")
public class ArticleController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(ArticleController.class);

	@Resource
	private TreeLikeService treeLikeService;

	@Resource
	private ArticleService service;

	@Override
	public ArticleService getService() {
		return service;
	}

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	@Authority(filter = PrivilegeFilter.class, value = RightConstant.ARTICLE_ONLINE)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit) {
		return page("article-list");
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

		BaseService.getNeighbor(mv, "entity_article", id);

		System.out.println(new AttachmentService().findByOwner((long) map.get("uid")));
		if (ConfigService.getValueAsBool("domain.article.attachmentDownload"))
			map.put("attachment", new AttachmentService().findByOwner((long) map.get("uid")));

		return output(mv, map, "jsp::article-info");
	}

	@Override
	public void prepareData(ModelAndView mv) {
		int catalogId = getService().getDomainCatalogId();
		Map<Long, BaseModel> map = TreeLikeService.idAskey(treeLikeService.getAllChildren(catalogId));
		mv.put("newsCatalogs", map);
//		System.out.println(map.get(""));
		mv.put(DOMAIN_CATALOG_ID, catalogId);

		super.prepareData(mv);
	}

	//////////////////// 后台 ///////////////////

	@GET
	@Path("/admin/{root}/list")
	@MvcFilter(filters = { DataBaseFilter.class, XslMaker.class })
	public String adminList(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, @QueryParam(CATALOG_ID) int catalogId, ModelAndView mv) {
		PageResult<Map<String, Object>> list = getService().list(catalogId, start, limit, CommonConstant.OFF_LINE);

		if (list.get(0).get("catalogId").getClass() == Integer.class) {
			// SQLite 配合 Map 不会出现期望的 Long。还是建议不要使用 Map
			for (Map<String, Object> map : list)
				map.put("catalogId", Long.parseLong(map.get("catalogId").toString()));
		}

		prepareData(mv);
		mv.put(XslMaker.XSL_TEMPLATE_PATH, jsp("website/article-xsl"));

		return autoOutput(list, mv, jsp("website/article-admin-list"));
	}

	@GET
	@Path("/admin/{root}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return jsp("website/article-edit");
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
		mv.put("isCreate", false);
		return output(mv, id, "jsp::website/article-edit");
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

//	@POST
//	@Path("/admin/{root}/{id}/uploadCover/")
//	@MvcFilter(filters = DataBaseFilter.class)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String uploadCover(MvcRequest req, @PathParam(ID) Long id) {
//		LOGGER.info("上传封面图片");
//
//		// 必须先有实体才能上传其图片
//		if (service.findById(id) == null)
//			throw new NullPointerException("请保存记录后再上传图片");
//
//		UploadFileInfo info = new UploadFileInfo();
//
//		// 约束上传
//		info.maxSingleFileSize = 512 * 1000;
//		info.allowExtFilenames = new String[] { "jpeg", "jpg", "png", "gif" };
//
//		String filename = "cover/" + SnowflakeIdWorker.getIdStr();
//
//		return AttachmentController.upload(req, _filename -> {
//			Map<String, Object> article = new HashMap<>(); // 保存字段
//			article.put("id", id);
//			article.put("cover", Encode.urlDecode(info.saveFileName));
//			service.update(article);
//		}, info, filename);
//	}

//	@POST
//	@Path("/admin/{root}/{id}/uploadContentImg/")
//	@MvcFilter(filters = DataBaseFilter.class)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String uploadContentImg(MvcRequest req, @PathParam(ID) Long id) {
//		LOGGER.info("上传正文图片");
//
//		UploadFileInfo info = new UploadFileInfo();
//		// 约束上传
//		info.maxSingleFileSize = 512 * 1000;
//		info.allowExtFilenames = new String[] { "jpeg", "jpg", "png", "gif" };
//
//		String filename = "contentImg/" + SnowflakeIdWorker.getIdStr();
//		return AttachmentController.upload(req, null, info, filename);
//	}

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