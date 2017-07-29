package com.ajaxjs.cms.app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.cms.app.service.ChannelService;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.CommonController;

@Controller
@Path("/admin/channel")
public class ChannelController extends CommonController<Map<String, Object>, Long> {
	public ChannelController() {
		setJSON_output(true);
	}

//	@GET
//	public String get(ModelAndView model) {
//		model.put("uiName", "渠道");
//		return jsp_perfix + "simple";
//	}
//
//	@Override
//	public ChannelService getService() {
//		return new ChannelService();
//	}
//
//	@GET
//	@Path("list")
//	@Override
//	public String list_all(ModelAndView model) {
//		return super.list_all(model);
//	}
//	
//	@POST
//	@Override
//	public String create(Map<String, Object> bean, ModelAndView model) {
//		return super.create(bean, model);
//	}
//	
//	@PUT
//	@Path("{id}")
//	public String update(@PathParam("id") Long id, Map<String, Object> bean, ModelAndView model) {
//		bean.put("id", id);
//		return super.update(bean, model);				
//	}
//	
//	 @DELETE
//	 @Path("{id}")
//	 public String delete(@PathParam("id") Long id, ModelAndView model) {
//		 Map<String, Object> bean = new HashMap<>();
//		 bean.put("id", id);
//		 return delete(bean, model);
//	 }
}
