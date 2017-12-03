package com.ajaxjs.web.widget;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.web.HtmlHead;

@Controller
@Path("/admin/config")
public class Config implements IController {
	@GET
	public String show() {
		return IController.jsp_perfix + "/config/config.jsp";
	}

	@POST
	public String sava(HttpServletRequest request) {
		try {
			if (request.getParameter("url") == null)
				throw new NullPointerException("缺少必填参数 url！");

			if (request.getParameter("contentBody") == null)
				throw new NullPointerException("缺少必填参数 contentBody！");

			String contentBody = request.getParameter("contentBody"),
					path = HtmlHead.Mappath(request, request.getParameter("url"));

			PageEditor.save_jsp_fileContent(path, contentBody);

			return String.format(json_ok, "修改页面成功！");
		} catch (Throwable e) {
			return String.format(json_not_ok, e.toString());
		}
	}
}
