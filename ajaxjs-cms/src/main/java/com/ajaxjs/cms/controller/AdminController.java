package com.ajaxjs.cms.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.annotation.TableName;

@Path("/admin")
public class AdminController implements IController, Constant {
	@GET
	public String admin() {
		return Constant.cms("admin");
	}

	@GET
	@Path("/workbench")
	public String workbench() {
		return Constant.cms("admin-workbench");
	}
	
	@TableName(value = "general_log", beanClass = EntityMap.class)
	public static interface GlobalLogDao extends IBaseDao<EntityMap> {
	}
	
	public static GlobalLogDao dao = new Repository().bind(GlobalLogDao.class);

	@GET
	@Path("GlobalLog")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		model.put("uiName", "操作日志");
		model.put(PageResult, dao.findPagedList(start, limit));
		return jsp_perfix + "/system/GlobalLog";
	}

	@GET
	@Path("/Logger")
	public String show(ModelAndView mv) {
		return jsp_perfix + "/system/Logger";
	}
}
