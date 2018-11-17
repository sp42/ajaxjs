package com.ajaxjs.user.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.simpleApp.CommonEntryAdminController;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserAdminService;
import com.ajaxjs.user.service.UserAdminServiceImpl; 

@Path("/admin/user")
@Bean("UserAdminController")
public class UserAdminController extends CommonController<User, Long> implements CommonEntryAdminController<User, Long> {
	@Resource("UserAdminService")
	private UserAdminService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) throws ServiceException {
		prepareData(model);
		model.put("PageResult", getService().findPagedList(start, limit));
		return jsp_perfix + "/user/list";
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		return super.createUI(model);
	}

	@GET
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView model) throws ServiceException {
		info(id, model);
		return jsp_perfix + "/user/user-info";
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String create(User entity, ModelAndView model) throws ServiceException {
		return super.create(entity, model);
	}

	@PUT
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String update(@PathParam("id") Long id, User entity, ModelAndView model) throws ServiceException {
		return super.update(id, entity, model);
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) throws ServiceException {
		User user = new User();
		user.setId(id);
		return super.delete(user, model);
	}

	@GET
	@Path("catalog")
	public String newsCatalogUI() {
		return commonJsp + "/simple_admin/edit-cataory";
	}

	@GET
	@Path("downloadXSL")
	public String downloadXSL() {
		return jsp_perfix + "/common/downloadXSL";
	}

	/**
	 * 用户登录日志
	 * 
	 * @param start
	 * @param limit
	 * @param model
	 * @return
	 */
	@GET
	@Path("login_log_list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String login_log_list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		model.put("uiName", "用户登录日志");
		model.put("PageResult", UserAdminServiceImpl.dao.findLoginLog(getParam(), start, limit));

		return jsp_perfix + "/user/login_log_list";
	}

	//	@GET
	//	@Path("catalog/list")
	//	public String getNewsCatalog(ModelAndView model, HttpServletRequest request) {
	//		initDb();
	//		prepareData(model);
	//		CatalogDao dao = new DaoHandler<CatalogDao>().bind(CatalogDao.class);
	//		List<User> result = dao.findAll(new QueryParams(request.getParameterMap()));
	//
	//		return outputListBeanAsJson(result);
	//	}
}
