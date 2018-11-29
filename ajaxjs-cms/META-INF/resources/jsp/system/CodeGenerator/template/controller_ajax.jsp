&lt;%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;package ${packageName}.controller;

import java.util.Map;
import java.util.HashMap;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.simpleApp.CommonEntryAdminController;
import ${packageName}.service.${beanName}Service;

/**
 * 
 *
 */
@Controller
@Path("/admin/user_admin/privilege")
@Bean("${beanName}Controller")
public class ${beanName}Controller extends CommonController&lt;Map&lt;String, Object&gt;, Integer, ${beanName}Service&gt; implements CommonEntryAdminController&lt;Map&lt;String, Object&gt;, Integer&gt; {
	@Resource("${beanName}Service")
	private ${beanName}Service service;
	
	@GET
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		PageResult&lt;Map&lt;String, Object&gt;&gt; p = super.pageList(start, limit, model);
		return outputPagedJsonList(p, model);
	}

	@GET
	@Override
	public String createUI(ModelAndView model) {
		model.put("uiName", getService().getName());
		return jsp_perfix + "user-admin/privilege";
	}
	
	@Override
	public String editUI(Integer id, ModelAndView model) {
		return show405();
	}

	@POST
	@Override
	public String create(Map&lt;String, Object&gt; entity, ModelAndView model) {
		return super.create(entity, model);
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(@PathParam("id") Integer id, Map&lt;String, Object&gt; entity, ModelAndView model) {
		return super.update(id, entity, model);
	}

	@DELETE
	@Path("{id}")
	@Override
	public String delete(@PathParam("id") Integer id, ModelAndView model) {
		Map&lt;String, Object&gt; entity = new HashMap&lt;&gt;();
		entity.put("id", id);
		return super.delete(entity, model);
	}
}
