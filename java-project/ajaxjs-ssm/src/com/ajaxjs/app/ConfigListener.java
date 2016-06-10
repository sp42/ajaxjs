package com.ajaxjs.app;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConfigListener implements ServletContextListener {
	public static final boolean isDebug = true;
	
	@Override
	public void contextInitialized(ServletContextEvent ce) {
		System.out.println("Starting Servlet");
		ServletContext cxt = ce.getServletContext();
		cxt.setAttribute("isAll_Free", true);// 全局免费开关

		cxt.setAttribute("downapp_link", "http://a.app.qq.com/o/simple.jsp?pkgname=smc.ng.gdtv.yd");

		Map<String, String> Config = new java.util.HashMap<String, String>();
		Config.put("title", "广东手机台");
		Config.put("site_description",
				"广东手机台是南方广播影视传媒集团推出的集视频图文为一体的资讯媒体类产品。为用户提供流畅、丰富、高品质的电视直播及视频、图文内容点播，满足随时随地获取资讯、影视、综艺、娱乐、科教等方面的信息需求。涵盖全国超过60个电视电台直播频道，随时随地可看掌上电视；以广府、客乡、潮汕本土特色为主打，汇聚特色民生纪实类点播节目，足不出户可了解广东的民生百态。");
		Config.put("site_keywords", "广东手机台 gdtv 3gtv");

		cxt.setAttribute("headTag", Config);
		cxt.setAttribute("bigfoot", cxt.getContextPath() + "/bigfoot");
//		cxt.setAttribute("PageUtil", new PageUtil());

	}

	@Override
	public void contextDestroyed(ServletContextEvent ce) {

	}

}
