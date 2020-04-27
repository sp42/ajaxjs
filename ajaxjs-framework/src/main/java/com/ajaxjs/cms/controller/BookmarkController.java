package com.ajaxjs.cms.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.SectionList;
import com.ajaxjs.cms.ShopBookmarkService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/bookmark")
public class BookmarkController extends BaseController<SectionList> {

	@Resource("ShopBookmarkService")
	private ShopBookmarkService service;

	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		mv.put("entryTypeIdNameMap", DataDictController.DataDictService.Entry_IdName);
		page(mv, service.findSectionListBySectionId(start, limit));
		
//		return info("user-bookmark");
		return "";
	}

	@GET
	@Path(ID_INFO)
	public String bookmark(@PathParam(ID) Long userId, ModelAndView mv) {
		mv.put("entryTypeIdNameMap", DataDictController.DataDictService.Entry_IdName);
		mv.put("userId", userId);
		
		return "user-bookmark";
	}


	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new SectionList());
	}
	
	@Override
	public IBaseService<SectionList> getService() {
		return service;
	}
}
