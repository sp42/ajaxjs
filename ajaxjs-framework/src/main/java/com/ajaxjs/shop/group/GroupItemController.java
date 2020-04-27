package com.ajaxjs.shop.group;

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
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.filter.MvcFilter;

/**
 * 
 * 控制器
 */
@Bean
@Path("/admin/groupItem")
public class GroupItemController extends BaseController<GroupItem> {
	@Resource("GroupItemService")
	private GroupItemService service;

	@GET
	@MvcFilter(filters = DataBaseFilter.class)
	public String listByGroupId(@QueryParam("groupId") long groupId) {
		return toJson(service.findByGroupId(groupId));
	}

	@GET
	@Path("/listDetailByGroupId")
	@MvcFilter(filters = DataBaseFilter.class)
	public String listDetailByGroupId(@QueryParam("groupId") long groupId) {
		return toJson(service.dao.findDetailByGroupId(groupId));
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(GroupItem entity) {
		return super.create(entity);
	}
	
	@PUT
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(ID) Long id, GroupItem entity) {
		return super.update(id, entity);
	}

	@DELETE
	@MvcFilter(filters = DataBaseFilter.class)
	@Path(ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(ID) Long id) {
		return delete(id, new GroupItem());
	}

	@Override
	public IBaseService<GroupItem> getService() {
		return service;
	}
}
