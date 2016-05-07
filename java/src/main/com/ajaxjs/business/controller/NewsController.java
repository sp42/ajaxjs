package com.ajaxjs.business.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ajaxjs.business.model.Catalog;
import com.ajaxjs.business.model.News;
import com.ajaxjs.business.service.CatalogService;
import com.ajaxjs.business.service.NewsService;
import com.ajaxjs.framework.controller.AbstractController;
import com.ajaxjs.framework.model.JspModel;

@org.springframework.stereotype.Controller
@RequestMapping(value = "/service/news")
public class NewsController extends AbstractController<News> {

	public NewsController() {
		setService(new NewsService());
	}

	@Override
	public void prepareData(HttpServletRequest reqeust, Model model) {
		// 内容分类
		CatalogService catalogService = new CatalogService();
		
		List<Catalog> catalogs = catalogService.getAll();
		
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("catalogs_map", JspModel.list_bean2map_id_as_key(catalogs));
	}
}
