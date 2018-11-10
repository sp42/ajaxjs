package com.ajaxjs.mvc.controller.testcase;

import com.ajaxjs.framework.News;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.CommonController;

import javax.ws.rs.*;

@Path("/news")
public class NewsController extends CommonController<News, Long> {

	@GET
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {

		return "";
	}

	@GET
	@Path("/{id}")
	public String getInfo(@PathParam("id") Long id, ModelAndView model) throws ServiceException {

		return "";
	}

	@POST
	public String create(News news, ModelAndView model) throws ServiceException {
		return "";
	}

	@PUT
	@Path("/{id}")
	public String update(@PathParam("id") Long id, News news, ModelAndView model) throws ServiceException {
		return "";
	}

	@DELETE
	@Path("/{id}")
	public String delete(News news, ModelAndView model) throws ServiceException {
		return "";
	}
}
