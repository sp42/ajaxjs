package com.ajaxjs.app;

import javax.ws.rs.Path;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;

@Bean
@Path("/news")
public class NewsController extends ArticleController {
	@Resource("NewsService")
	public NewsService service;

	@Override
	public NewsService getService() {
		return service;
	}
}
