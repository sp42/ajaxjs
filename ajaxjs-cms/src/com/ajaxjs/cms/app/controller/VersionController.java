package com.ajaxjs.cms.app.controller;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.app.model.Version;
import com.ajaxjs.cms.app.service.VersionService;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.CommonController;

/**
 * just a json
 * 
 * @author xinzhang
 *
 */
@Controller
@Path("/version")
public class VersionController extends CommonController<Version, Long> {
	public VersionController() {
		setJSON_output(true);
	}

	@Override
	public VersionService getService() {
		return new VersionService();
	}

	@GET
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		return super.list(start, limit, model);
	}

	@GET
	@Path("getNew")
	public String getNew(@QueryParam("portalId") int portalId, @QueryParam("channelId") String channelId,
			HttpServletRequest request, ModelAndView model) {
		initDb();
		VersionService service = getService();

		try {
			Version bean = service.getTop(portalId, channelId);
			model.put("info", bean);

			service.prepareData(model);
		} catch (Exception e) {
			model.put("ServiceException", e);
		} finally {
			closeDb();
		}

		return show_json_info;
	}
}
