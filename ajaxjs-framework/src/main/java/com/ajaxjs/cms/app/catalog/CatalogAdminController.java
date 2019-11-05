package com.ajaxjs.cms.app.catalog;

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
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/admin/catelog")
@Bean
public class CatalogAdminController extends BaseController<Catalog> {
	@Resource("CatalogService")
	private CatalogService service;

	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = DataBaseFilter.class)
	public String list() {
		return toJson(service.getDao().findList());
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		prepareData(model);
		return editUI();
	}

	@GET
	@Path("getListAndSubByParentId")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String getListAndSubByParentId(@QueryParam("parentId") int parentId) {
		return toJson(service.findAllListByParentId(parentId));
	}

	@Override
	public String editUI(Long id, ModelAndView model) {
		return show405;
	}

	@POST
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String create(Catalog entity) {
		return super.create(entity);
	}

	@PUT
	@Path(idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String update(@PathParam(Constant.id) Long id, Catalog entity) {
		return super.update(id, entity);
	}

	@DELETE
	@Path(idInfo)
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(@PathParam(Constant.id) Long id) {
		return delete(id, new Catalog());
	}

	@Override
	public IBaseService<Catalog> getService() {
		return service;
	}
}
