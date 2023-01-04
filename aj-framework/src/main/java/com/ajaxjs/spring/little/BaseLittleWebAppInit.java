package com.ajaxjs.spring.little;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.spring.BaseSpringWebInitializer;
import com.ajaxjs.util.logger.LogHelper;

public abstract class BaseLittleWebAppInit extends BaseSpringWebInitializer {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseLittleWebAppInit.class);

	public abstract Class<?> getConfigClz();

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// 通过注解的方式初始化Spring的上下文
		AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
		// 注册spring的配置类（替代传统项目中xml的configuration）
		ac.register(getConfigClz());
		ac.refresh();

		// 绑定 servlet
		ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcherServlet", new DispatcherServlet(ac));
		registration.setLoadOnStartup(1);// 设置 tomcat 启动立即加载 servlet
		registration.addMapping("/*"); // 浏览器访问 uri

		initUpload(servletContext, registration);
		LOGGER.info("WEB 程序启动完毕");
	}
}
