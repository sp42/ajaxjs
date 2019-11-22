package com.ajaxjs.shop.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.DataDictService;
import com.ajaxjs.cms.SectionList;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.service.ShopBookmarkService;

@Bean
@Path("/admin/bookmark")
public class BookmarkAdminController extends BaseController<SectionList> {

	@Resource("ShopBookmarkService")
	private ShopBookmarkService service;

	@GET
	@Path(list)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		mv.put("entryTypeIdNameMap", DataDictService.Entry_IdName);
		listPaged(start, limit, mv, service::findSectionListBySectionId);
		
//		return info("user-bookmark");
		return "";
	}

	@GET
	@Path(idInfo)
	public String bookmark(@PathParam(id) Long userId, ModelAndView mv) {
		mv.put("entryTypeIdNameMap", DataDictService.Entry_IdName);
		mv.put("userId", userId);
		
		return "user-bookmark";
	}


	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new SectionList());
	}
	
	@Override
	public IBaseService<SectionList> getService() {
		return service;
	}
}
