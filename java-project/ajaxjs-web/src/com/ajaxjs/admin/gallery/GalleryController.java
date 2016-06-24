package com.ajaxjs.cms.gallery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;

@org.springframework.stereotype.Controller
@RequestMapping(value = "/cms/gallery")
public class GalleryController {
	/**
	 * 系统配置界面
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String doGet(HttpServletRequest request) {
		if (request.getParameter("getJs") != null) {
			return "";
		} else {
			request.setAttribute("imgs", Service.getImgs(new Requester(request).Mappath("/images")));
			return "common/misc/gallery";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		// Service.save_jsp_fileContent(new Requester(request));//
		// 小心使用，改方法的权限很大！

		Responser rsp = new Responser(response);
		rsp.setRequest(request);
		rsp.outputAction();
	}

}
