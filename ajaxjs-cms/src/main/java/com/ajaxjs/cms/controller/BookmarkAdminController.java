package com.ajaxjs.cms.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.SectionList;
import com.ajaxjs.cms.SectionListService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/bookmark")
public class BookmarkAdminController extends BaseController<SectionList> {
	@GET
	@Path("/list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		System.out.println("::::::::::::");
		listPaged(start, limit, mv, (s, l) -> SectionListService.dao.findPagedListByCatelogId(ConfigService.getValueAsInt("data.section.useBookmark_Catelog_Id"), start, limit));
		return info("user-bookmark");
	}

	@GET
	@Path("{id}/bookmark")
	public String bookmark(@PathParam("id") Long userId, ModelAndView mv) {
		mv.put("userId", userId);
		return info("user-bookmark");
	}

	@Override
	public IBaseService<SectionList> getService() {
		return null;
	}
}
