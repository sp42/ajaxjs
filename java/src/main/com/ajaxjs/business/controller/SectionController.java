package com.ajaxjs.business.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.business.model.Catalog;
import com.ajaxjs.business.model.Section;
import com.ajaxjs.business.service.CatalogService;
import com.ajaxjs.business.service.SectionService;
import com.ajaxjs.framework.controller.AbstractController;

@Controller
@RequestMapping(value = "/service/section")
public class SectionController  extends AbstractController<Section> {
	public SectionController() {
		setService(new SectionService());
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "limit", required = false, defaultValue = "8") int limit,
			HttpServletRequest request, Model model
		) {
		super.list(start, limit, request, model);
		
		return formatDectect(request, "common/entity/catalog");
	}

}