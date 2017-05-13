package com.ajaxjs.cms.controller;

import java.util.List;
import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.cms.service.CatalogService;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.mvc.controller.CommonController;

@Controller
@Path("/admin/catalog")
public class CatalogController extends CommonController<Catalog, Long> {
	public CatalogController() {
		setJSON_output(true);
	}

	@GET
	public String get() {
		return jsp_perfix + "catalog";
	}

	@Override
	public CatalogService getService() {
		return new CatalogService();
	}

	@GET
	@Path("list")
	@Override
	public String list_all(ModelAndView model) {
		return super.list_all(model);
	}
	
	
	@POST
	@Override
	public String create(Catalog catalog, ModelAndView model) {
		return super.create(catalog, model);
	}
	
	@PUT
	@Path("{id}")
	public String update(@PathParam("id") Long id, Catalog catalog, ModelAndView model) {
		catalog.setId(id);
		return super.update(catalog, model);				
	}
	
	 @DELETE
	 @Path("{id}")
	 public String delete(@PathParam("id") Long id, ModelAndView model) {
		 Catalog catalog = new Catalog(); // 删除比较特殊
		 catalog.setId(id);
		 return delete(catalog, model);
		 
		 
	 }
	 

	public static void initCatalog(int parentId, ModelAndView model, HttpServletRequest request, boolean isNeedTobeMap) {
		CommonController.initDb();
		List<Catalog> catalogs = null;
		CatalogService service = new CatalogService();

		if (parentId == 0) {
			try {
				catalogs = service.getAll();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		} else {
			catalogs = service.getListByParentId(parentId);
		}

		if (request == null)
			model.put("catalogs", catalogs);
		else
			request.setAttribute("catalogs", catalogs);

		if (isNeedTobeMap) {
			Map<Integer, BaseModel> map = ModelAndView.list_bean2map_id_as_key(catalogs);

			if (request == null)
				model.put("catalogsMap", map);
			else
				request.setAttribute("catalogsMap", map);
		}

		if (request != null)
			CommonController.closeDb();
	}

	public static void initCatalog(int parentId, ModelAndView model) {
		initCatalog(parentId, model, false);
	}

	public static void initCatalog(int parentId, ModelAndView model, boolean isNeedTobeMap) {
		initCatalog(parentId, model, null, isNeedTobeMap);
	}
}
