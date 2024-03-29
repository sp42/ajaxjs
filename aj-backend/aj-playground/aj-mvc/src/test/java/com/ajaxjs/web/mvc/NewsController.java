package com.ajaxjs.web.mvc;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.BaseModel;

@Path("/news")
public class NewsController implements IController {
	public static class News extends BaseModel {
		private static final long serialVersionUID = 1L;
	}

	@GET
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit) {
		return "html::hihi";
	}

	@GET
	@Path("{id}")
	public String getInfo(@PathParam("id") Long id)  {
		return "home";
	}

	@POST
	public String create(News news)  {
		return "";
	}

	@PUT
	@Path("{id}")
	public String update(@PathParam("id") Long id, News news)  {
		return "";
	}

	@DELETE
	@Path("{id}")
	public String delete(News news)  {
		return "";
	}
}
