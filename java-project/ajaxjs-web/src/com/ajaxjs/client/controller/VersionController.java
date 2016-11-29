package com.ajaxjs.client.controller;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.client.model.Version;
import com.ajaxjs.client.service.VersionService;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.ReadOnlyController;
import com.ajaxjs.web.Requester;

@Controller
@Path("/version")
public class VersionController extends ReadOnlyController<Version> {
	public VersionController() {
		setJSON_output(true);
		setService(new VersionService());
	}

	@Override
	public void prepareData(HttpServletRequest request, ModelAndView model) {
		Requester req = (Requester)request;
		String perfix = req.getBasePath();

		if (model.get("info") != null) {
			Version version = (Version) model.get("info");
			version.setDownloadUrl(perfix + version.getDownloadUrl());

		}
	}

	@GET
	@Path("/getNew")
	public String getNew(HttpServletRequest request, ModelAndView model) {
		VersionService service = (VersionService) getService();

		try {
			Version bean = service.getNew();
			model.put("info", bean);
			
			prepareData(request,  model);
		} catch (ServiceException e) {
			model.put("ServiceException", e);
		}
		
		return showInfo;
	}
}
