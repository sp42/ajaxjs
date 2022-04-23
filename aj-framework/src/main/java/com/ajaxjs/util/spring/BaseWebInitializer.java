package com.ajaxjs.util.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 配置 Spring MVC 实现该接口的类会在 Servlet 容器启动时自动加载并运行
 *
 * @author Frank Cheung<sp42@qq.com>
 */
public abstract class BaseWebInitializer implements WebApplicationInitializer {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseWebInitializer.class);

	/**
	 * 子类可以配置的方法
	 * 
	 * @param servletContext Servlet 上下文
	 * @param webCxt         如果不想在 WebApplicationInitializer 当前类中注入，可以另设一个类专门注入组件
	 */
	public abstract void initWeb(ServletContext servletContext, GenericWebApplicationContext webCxt);

	@Override
	public void onStartup(ServletContext servletCxt) {
		LOGGER.info("WEB 程序启动中……");
		if (servletCxt == null) // 可能在测试
			return;

		servletCxt.setAttribute("ctx", servletCxt.getContextPath());
		servletCxt.setInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
		servletCxt.addListener(new ContextLoaderListener()); // 监听器

		FilterRegistration.Dynamic filterReg = servletCxt.addFilter("InitMvcRequest", new CharacterEncodingFilter("UTF-8"));
		filterReg.addMappingForUrlPatterns(null, true, "/*");

		GenericWebApplicationContext webCxt = new GenericWebApplicationContext();
		initWeb(servletCxt, webCxt); // 如果不想在 WebApplicationInitializer 当前类中注入，可以另设一个类专门注入组件

		ServletRegistration.Dynamic registration = servletCxt.addServlet("dispatcher", new DispatcherServlet(webCxt));
		registration.addMapping("/");
		registration.setLoadOnStartup(1);
//		registration.setMultipartConfig(new MultipartConfigElement("c:/temp", 50000000, 50000000, 0));// 文件上传
	}

	/**
	 * 文件上传
	 * 
	 * @return
	 */
//	@Bean(name = "multipartResolver") // 此处 id 为固定写法，不能随便取名
//	public MultipartResolver multipartResolver() {
//		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
//		resolver.setResolveLazily(true);// resolveLazily 属性启用是为了推迟文件解析，以在在 UploadAction 中捕获文件大小异常
//
//		return resolver;
//	}

	/**
	 * YAML 配置文件
	 * 
	 * @return YAML 配置文件
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer cfger = new PropertySourcesPlaceholderConfigurer();
		// Don't fail if @Value is not supplied in properties. Ignore if not found
		cfger.setIgnoreUnresolvablePlaceholders(true);
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("application.yml"));
		cfger.setProperties(yaml.getObject());

		return cfger;
	}

	/**
	 * 全局异常拦截器
	 * 
	 * @return 全局异常拦截器
	 */
	@Bean
	public GlobalExceptionHandler GlobalExceptionHandler() {
		return new GlobalExceptionHandler();
	}

	/**
	 * Spring IoC 工具
	 * 
	 * @return IoC 工具
	 */
	@Bean
	public DiContextUtil DiContextUtil() {
		return new DiContextUtil();
	}
}