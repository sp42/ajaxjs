package com.ajaxjs.framework.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import com.ajaxjs.framework.spring.filter.FileUploadHelper;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.framework.spring.boot.EmbeddedTomcatStarter;
import com.ajaxjs.framework.spring.filter.GlobalExceptionHandler;
import com.ajaxjs.framework.spring.filter.UTF8CharsetFilter;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.util.logger.LogHelper;

public class WebAppInitializer implements WebApplicationInitializer {
	private Class<?>[] clz;

	public WebAppInitializer() {
	}

	public WebAppInitializer(Class<?>[] clz) {
		this.clz = clz;
	}

	@Override
	public void onStartup(ServletContext ctx) {
		// 通过注解的方式初始化 Spring 的上下文，注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
		AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
		ac.setServletContext(ctx);
		if (!ObjectUtils.isEmpty(clz))
			ac.register(clz);
		ac.refresh();
		ac.registerShutdownHook();
		
		ctx.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
//		cxt.setInitParameter("contextConfigLocation", mainConfig);
		ctx.addListener(new ContextLoaderListener()); // 监听器
		ctx.setAttribute("ctx", ctx.getContextPath()); // 为 JSP 提供 shorthands

		// 绑定 servlet
		Dynamic registration = ctx.addServlet("dispatcher", new DispatcherServlet(ac));
		registration.setLoadOnStartup(1);// 设置 tomcat 启动立即加载 servlet
		registration.addMapping("/"); // 浏览器访问 uri。注意不要设置 /*

		// 字符过滤器
//        new CharacterEncodingFilter("UTF-8")
		FilterRegistration.Dynamic filterReg = ctx.addFilter("InitMvcRequest", new UTF8CharsetFilter());
		filterReg.addMappingForUrlPatterns(null, true, "/*");

		FileUploadHelper.initUpload(ctx, registration);
	}
}