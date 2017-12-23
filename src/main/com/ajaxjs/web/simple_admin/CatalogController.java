package com.ajaxjs.web.simple_admin;

import java.util.List;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.web.CommonController;
import com.ajaxjs.web.CommonEntryAdminController;

/**
 * 没有使用 Service
 * 
 * @author admin
 *
 */
@Controller
@Path("/admin/catalog")
public class CatalogController extends CommonController<Catalog, Long> implements CommonEntryAdminController<Catalog> {
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
		QueryParams param = new QueryParams(MvcRequest.getHttpServletRequest().getParameterMap());
		List<Catalog> result = service.findAll(param);

		return outputListBeanAsJson(result);
	}

	@Override
	public String editUI(Long id, ModelAndView model) {
		return null;
	}

	@POST
	@Override
	public String create(Catalog entity, ModelAndView model) {
		initDb();
		prepareData(model);
		return super.create(entity, model);
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(Catalog entity, ModelAndView model) {
		initDb();
		prepareData(model);
		return super.update(entity, model);
	}

	@DELETE
	@Path("{id}")
	@Override
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		initDb();
		Catalog catalog = new Catalog();
		catalog.setId(id);

		return super.delete(catalog, model);
	}

}
