package com.ajaxjs.article.controller;

import javax.mvc.annotation.Controller;
import javax.ws.rs.Path;

import com.ajaxjs.article.model.Article;
import com.ajaxjs.article.service.ArticleService;
import com.ajaxjs.mvc.controller.ReadOnlyController;


@Controller
@Path("/news")
public class ArticleController extends ReadOnlyController<Article> {
	public ArticleController() {
//		setListTpl("/WEB-INF/jsp/entry/news/frontEnd_list.jsp");
		setService(new ArticleService());
	}
}
