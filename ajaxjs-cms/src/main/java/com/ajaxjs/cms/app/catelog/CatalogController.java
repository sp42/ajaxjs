package com.ajaxjs.cms.app.catelog;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.controller.CommonController;
import com.ajaxjs.cms.controller.CommonEntryAdminController;
import com.ajaxjs.cms.service.CatelogService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.dao.PageResult;

@Path("/admin/catelog")
@Bean("CatelogAdminController")
public class CatalogController extends CommonController<Catelog, Long> implements CommonEntryAdminController<Catelog, Long> {
	@Resource("CatelogService")
	private CatelogService service;

	@GET
	@Override
	public String createUI(ModelAndView model) {
		prepareData(model);
		return jsp_perfix + "/system/Catelog";
	}

	@GET
	@Path("list")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		PageResult<Catelog> result = getService().findPagedList(start, limit);
		return listJson(start, limit, model, (_start, _limit) -> result);
	}

	@GET
	@Path("getListAndSubByParentId")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String getListAndSubByParentId(@QueryParam("parentId") int parentId, ModelAndView model) {
		return outputJson(getService().getAllListByParentId(parentId), model);
	}

	@Override
	public String editUI(Long id, ModelAndView model) {
		return null;
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Catelog entity, ModelAndView mv) {
		return create(entity, mv, _entity -> service.create(_entity));
	}

	@PUT
	@Path("/{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam("id") Long id, Catelog entity, ModelAndView mv) {
		return update(id, entity, mv, _entity -> service.update(_entity));
	}

	@DELETE
	@Path("{id}")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView mv) {
		return delete(id, new Catelog(), mv, catelog -> service.delete(catelog));
	}
}
