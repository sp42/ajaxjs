package com.ajaxjs.web.website;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ajaxjs.json_db.map_traveler.MapUtils;

@WebListener
public class SiteStruStartUp implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		String path = context.getRealPath("/WEB-INF/website-stru.json");
		SiteStru json = new SiteStru();
		json.setFilePath(path);
		json.load();

		if (json.isLoaded()) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) json.getJsonMap().get("website");
			MapUtils.buildPath(list);
		}

		context.setAttribute("WEBSITE_STRU", json);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
