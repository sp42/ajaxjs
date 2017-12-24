package com.ajaxjs.web.simple_admin;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryReadOnlyController;

@Controller
@Path("/news")
public class NewsController extends CommonController<Map<String, Object>, Long> implements CommonEntryReadOnlyController {
	public NewsController() {
		setService(service);
	}

	private NewsService service = new NewsServiceImpl();

	@GET
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		super.pageList(start, limit, model);
		return String.format(jsp_list, service.getTableName());
	}

	@GET
	@Override
	@Path("{id}")
	public String info(@PathParam("id") Long id, ModelAndView model) {
		return super.info(id, model);
	}

}
