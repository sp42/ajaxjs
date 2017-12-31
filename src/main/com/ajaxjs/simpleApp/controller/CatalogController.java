package com.ajaxjs.simpleApp.controller;

import java.util.List;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.simpleApp.model.Catalog;
import com.ajaxjs.simpleApp.service.CatalogService;
import com.ajaxjs.simpleApp.service.CatalogServiceImpl;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryAdminController;

/**
 *
 * 
 * @author admin
 *
 */
@Controller
@Path("/admin/catalog")
public class CatalogController extends CommonController<Catalog, Long, CatalogService> implements CommonEntryAdminController<Catalog> {
	CatalogService service = new CatalogServiceImpl();

	public CatalogController() {
		setService(service);
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		return common_jsp_perfix + "simple_admin/edit-cataory";
	}

	@GET
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		initDb();
		prepareData(model);
		List<Catalog> result = null;
		
		try {
			result = service.findAll(MvcRequest.factory());
		} finally {
			closeDb();
		}

		return outputListBeanAsJson(result);
	}

	@Override
	public String editUI(Long id, ModelAndView model) {
		return null;
	}

	@POST
	@Override
	public String create(Catalog entity, ModelAndView model) {
		prepareData(model);
		return super.create(entity, model);
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(Catalog entity, ModelAndView model) {
		prepareData(model);
		return super.update(entity, model);
	}

	@DELETE
	@Path("{id}")
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		Catalog catalog = new Catalog();
		catalog.setId(id);

		return super.delete(catalog, model);
	}

}
