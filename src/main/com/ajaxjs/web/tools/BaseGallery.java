package com.ajaxjs.web.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.web.WebUtil;

@Controller
@Path("/admin/gallery")
public class BaseGallery implements IController {
	@GET
	public String gallery(HttpServletRequest request) {
		String folder = WebUtil.Mappath(request, "/images");

		File[] files = new File(folder).listFiles();
		if (files == null)
			return null;

		List<String> filenames = new ArrayList<>();

		for (File file : files) {
			if (!file.isDirectory()) {
				String fileName = file.getName();
				if (fileName.contains(".jpg") || fileName.contains(".gif") || fileName.contains(".png"))
					filenames.add("\"" + fileName + "\"");
			}
		}

		String json = "json::[" + StringUtil.stringJoin(filenames, ",") + "]";
		return json;
	}
}
