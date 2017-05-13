package com.ajaxjs.cms.controller;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.cms.model.Portal;
import com.ajaxjs.cms.service.PortalService;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.CommonController;

@Controller
@Path("/admin/portal")
public class PortalAdminController extends CommonController<Portal, Long> {
	public PortalAdminController() {
		setJSON_output(true);
	}

	@GET
	public String get(ModelAndView model) {
		model.put("uiName", "门户");
		return jsp_perfix + "simple";
	}

	@Override
	public PortalService getService() {
		return new PortalService();
	}

	@GET
	@Path("list")
	@Override
	public String list_all(ModelAndView model) {
		return super.list_all(model);
	}
	
	
	@POST
	@Override
	public String create(Portal catalog, ModelAndView model) {
		return super.create(catalog, model);
	}
	
	@PUT
	@Path("{id}")
	public String update(@PathParam("id") Long id, Portal catalog, ModelAndView model) {
		catalog.setId(id);
		return super.update(catalog, model);				
	}
	
	 @DELETE
	 @Path("{id}")
	 public String delete(@PathParam("id") Long id, ModelAndView model) {
		 Portal catalog = new Portal(); // 删除比较特殊
		 catalog.setId(id);
		 return delete(catalog, model);
		 
		 
	 }
}
