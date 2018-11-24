package com.ajaxjs.mvc;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.controller.CommonController;
import com.ajaxjs.framework.News;

@Path("/news")
public class NewsController extends CommonController<News, Long> {

	@GET
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {

		return "";
	}

	@GET
	@Path("/{id}")
	public String getInfo(@PathParam("id") Long id, ModelAndView model)  {

		return "";
	}

	@POST
	public String create(News news, ModelAndView model)  {
		return "";
	}

	@PUT
	@Path("/{id}")
	public String update(@PathParam("id") Long id, News news, ModelAndView model)  {
		return "";
	}

	@DELETE
	@Path("/{id}")
	public String delete(News news, ModelAndView model)  {
		return "";
	}
}
