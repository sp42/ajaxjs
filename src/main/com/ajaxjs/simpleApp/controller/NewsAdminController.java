package com.ajaxjs.simpleApp.controller;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.service.NewsService;
import com.ajaxjs.simpleApp.service.NewsServiceImpl;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryAdminController;

@Controller
@Path("/admin/news")
public class NewsAdminController extends CommonController<Map<String, Object>, Long> implements CommonEntryAdminController<Map<String, Object>> {
	public NewsAdminController() {
		setService(service);
	}

	private NewsService service = new NewsServiceImpl();

	@GET
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		pageList(start, limit, model);
		return String.format(jsp_adminList, service.getName());
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		prepareData(model);
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

	@GET
	@Path("catalog")
	public String newsCatalogUI() {
		return common_jsp_perfix + "simple_admin/edit-cataory";
	}

//	@GET
//	@Path("catalog/list")
//	public String getNewsCatalog(ModelAndView model, HttpServletRequest request) {
//		initDb();
//		prepareData(model);
//		CatalogDao dao = new DaoHandler<CatalogDao>().bind(CatalogDao.class);
//		List<Map<String, Object>> result = dao.findAll(new QueryParams(request.getParameterMap()));
//
//		return outputListBeanAsJson(result);
//	}
}
