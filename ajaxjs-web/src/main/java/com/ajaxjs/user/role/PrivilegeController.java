package com.ajaxjs.user.role;

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
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;

@Path("/admin/user/privilege")
@Component
public class PrivilegeController extends BaseController<Privilege> {

	@Resource("UserRolePrivilegeService")
	private PrivilegeService service;

	@GET
	@Path(LIST)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam(START) int start, @QueryParam(LIMIT) int limit, ModelAndView mv) {
		return toJson(service.findList());
	}

	@GET
	public String jsp(ModelAndView mv) {
		prepareData(mv);
		return jsp("user/role/assign-right");
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Privilege entity) {
		return super.create(entity);
	}

	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, Privilege entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new Privilege());
	}

	@Override
	public IBaseService<Privilege> getService() {
		return service;
	}
}
