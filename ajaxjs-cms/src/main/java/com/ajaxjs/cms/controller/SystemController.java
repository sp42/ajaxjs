package com.ajaxjs.cms.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.dao.GlobalLogDao;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.orm.dao.DaoHandler;

@Path("/admin")
public class SystemController implements IController, Constant {
	GlobalLogDao dao = new DaoHandler<GlobalLogDao>().bind(GlobalLogDao.class);

	@GET
	@Path("GlobalLog")
	@MvcFilter(filters = DataBaseFilter.class)
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		model.put(PageResult, dao.findPagedList(start, limit));
		return jsp_perfix + "/system/GlobalLog";
	}

	@GET
	@Path("/Logger")
	public String show(ModelAndView mv) {
		return Constant.jsp_perfix + "/system/Logger";
	}
}
