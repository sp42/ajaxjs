package test.com.ajaxjs.framework.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.framework.controller.AbstractController;

import test.com.ajaxjs.framework.model.Catalog;
import test.com.ajaxjs.framework.service.CatalogService;

@Controller
@RequestMapping(value = "/service/catalog")
public class CatalogController extends AbstractController<Catalog> {
	public CatalogController() {
		setService(new CatalogService());
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
