package com.ajaxjs.app;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;

@Bean
public class MyController {
	@Resource("autoWire:ioc.NewsService|NewsService")
	private NewsService service;

	public void foo() {
		service.showInfo(null, 1L);
	}
}
