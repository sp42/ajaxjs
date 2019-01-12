package com.ajaxjs.cms.user;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */

@Bean
@Path("/admin/user")
public class UserAdminController extends BaseController<User> {
	@Resource("UserService")
	private UserService service;

	@GET
	@Path("/list")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView mv) {
		mv.put("SexGender", UserDict.SexGender);
		listPaged(start, limit, mv, service.getDao()::findPagedList_Cover);
		return adminList();
	}

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("/{id}")
	@Override
	public String editUI(@PathParam("id") Long id, ModelAndView mv) {
		mv.put("SexGender", UserDict.SexGender);
		super.editUI(id, mv);
		return editUI();
	}

	@GET
	@Override
	public String createUI(ModelAndView mv) {
		mv.put("SexGender", UserDict.SexGender);
		super.createUI(mv);
		return editUI();
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(User entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, User entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam("id") Long id) {
		return delete(id, new User());
	}

	@GET
	@Path("bookmark/list")
	public String bookmarkList(ModelAndView mv) {
		return info("user-bookmark");
	}

	@GET
	@Path("{id}/bookmark")
	public String bookmark(@PathParam("id") Long userId, ModelAndView mv) {
		mv.put("userId", userId);
		return info("user-bookmark");
	}

	@GET
	@Path("address/list")
	public String addressList(ModelAndView mv) {
		return info("user-address");
	}

	@GET
	@Path("{id}/address")
	public String address(@PathParam("id") Long userId, ModelAndView mv) {
		mv.put("userId", userId);
		return info("user-address");
	}

	@Override
	public IBaseService<User> getService() {
		return service;
	}
}
