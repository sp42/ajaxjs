package com.ajaxjs.web.simple_admin;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.jdbc.ConnectionMgr;
import com.ajaxjs.util.mock.MockDataSource;
import com.ajaxjs.util.mock.News;
import com.ajaxjs.util.mock.NewsService;
import com.ajaxjs.util.mock.NewsServiceImpl;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryAdminController;

@Controller
@Path("/admin/news")
public class NewsAdminController extends CommonController<Map<String, Object>, Long> implements CommonEntryAdminController<Map<String, Object>> {
	//	@Override
	//	public NewsService getService() {
	//		ConnectionMgr.setConnection(MockDataSource.getTestConnection());
	//		return new NewsServiceImpl();
	//	}

	//		return common_jsp_perfix + "simple_admin/edit-single-entry";

	@GET
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return super.list(start, limit, model);
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		getService().prepareData(model);
		// list_all(model);
		return String.format(jsp_adminInfo, getService().getName());
	}

	@GET
	@Path("{id}")
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) {
		info(id, model);
		return String.format(jsp_adminInfo, getService().getName());
	}

	@POST
	@Override
	public String create(Map<String, Object> entity, ModelAndView model) {
		return super.create(entity, model);
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(Map<String, Object> entity, ModelAndView model) {
		return super.update(entity, model);
	}

	@DELETE
	@Path("{id}")
	@Override
	public String delete(Long id, ModelAndView model) {
		return super.delete(id, model);
	}
}
