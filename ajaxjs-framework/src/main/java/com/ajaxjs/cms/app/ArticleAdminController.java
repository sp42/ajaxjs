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

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.net.http.PicDownload;
import com.ajaxjs.orm.thirdparty.SnowflakeIdWorker;

@Bean
@Path("/admin/article")
public class ArticleAdminController extends BaseController<Map<String, Object>> {
	@Resource("ArticleService")
	private ArticleService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, @QueryParam("catalogId") int catelogId, ModelAndView mv) {
		listPaged(start, limit, mv, (s, l) -> service.findPagedListByCatelogId(	catelogId, start, limit));
		return adminListCMS();
	}

	@GET
	@Path("listJson")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String listJson(@QueryParam("start") int start, @QueryParam("limit") int limit, @QueryParam("catalogId") int catelogId, ModelAndView mv) {
		return pagedListJson(listPaged(start, limit, mv, (s, l) -> service.findPagedListByCatelogId(catelogId, start, limit)));
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		super.createUI(mv);
		return editUI_CMS();
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/{id}")
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		super.editUI(id, mv);
		return editUI_CMS();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id) {
		return delete(id, new HashMap<String, Object>());
	}

	@Override
	public void prepareData(ModelAndView mv) {
		mv.put(domainCatalog_Id, service.getDomainCatelogId());
		super.prepareData(mv);
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	/**
	 * 下载远程图片到本地服务器
	 * 
	 * @param start
	 * @param limit
	 * @param catelogId
	 * @param mv
	 * @return
	 */
	@POST
	@Path("downAllPics")
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
