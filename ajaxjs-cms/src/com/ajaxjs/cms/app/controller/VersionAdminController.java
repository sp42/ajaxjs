package com.ajaxjs.cms.app.controller;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.app.model.Version;
import com.ajaxjs.cms.app.service.ChannelService;
import com.ajaxjs.cms.app.service.VersionService;
import com.ajaxjs.cms.service.PortalService;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.mvc.controller.CommonController;

/**
 * just a json
 * 
 * @author xinzhang
 *
 */
@Controller
@Path("/admin/version")
public class VersionAdminController extends CommonController<Version, Long> {
	public VersionAdminController() {
		setAdminUI(true);
	}

	@Override
	public VersionService getService() {
		return new VersionService();
	}

	@GET
	@Path("list")
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return super.list(start, limit, model);
	}

	private static void prepareData(ModelAndView model) {
		initDb();

		try {
			model.put("portals", new PortalService().getAll());
			model.put("channels", new ChannelService().getAll());
		} catch (ServiceException e) {
			e.printStackTrace();
		} finally {
			closeDb();
		}
	}

	/**
	 * 新建
	 */
	@GET
	@Override
	public String createUI(ModelAndView model) {
		prepareData(model);
		return super.createUI(model);
	}

	@POST
	@Override
	public String create(Version version, ModelAndView model) {
		return super.create(version, model);
	}

	/**
	 * 编辑
	 */
	@GET
	@Path("{id}")
	@Override
	public String info(@PathParam("id") Long id, ModelAndView model) {
		model.put("isCreate", false);
		prepareData(model);
		return super.info(id, model);
	}

	@PUT
	@Path("{id}")
	@Override
	public String update(Version version, ModelAndView model) {
		return super.update(version, model);
	}

	@DELETE
	@Path("{id}")
	public String delete(@PathParam("id") Long id, ModelAndView model) {
		Version version = new Version(); // 删除比较特殊
		version.setId(id);
		return delete(version, model);
	}
}
