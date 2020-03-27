package com.ajaxjs.user.role;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;

@Bean
@Path("/admin/user/user_group")
public class RoleController extends BaseController<Map<String, Object>> {
	@Resource("UserRoleService")
	private RoleService service;

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam(start) int start, @QueryParam(limit) int limit, ModelAndView mv) {
		return toJson(service.findList());
	}

	@GET
	public String jsp(ModelAndView mv) {
		prepareData(mv);
		return jsp("user/role/user-group");
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Map<String, Object> entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(id) Long id, Map<String, Object> entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(idInfo)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(id) Long id) {
		return delete(id, new HashMap<>());
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return service;
	}

	@POST
	@Path("updateResourceRightValue")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateResourceRightValue(@FormParam("userGroupId") long userGroupId, @FormParam("resId") int resId, @FormParam("isEnable") boolean isEnable) {
		try {
			service.updateResourceRightValue(userGroupId, resId, isEnable);
			return jsonOk("修改资源权限成功");
		} catch (ServiceException e) {
			e.printStackTrace();
			return jsonNoOk(e.getMessage());
		}

	}

	@GET
	@Path(idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam(id) Long id) {
		return toJson(service.getDao().findById(id));
	}
}
