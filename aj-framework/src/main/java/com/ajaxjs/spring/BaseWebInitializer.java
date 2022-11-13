package com.ajaxjs.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 配置 Spring MVC 实现该接口的类会在 Servlet 容器启动时自动加载并运行
 *
 * @author Frank Cheung
 */
public abstract class BaseWebInitializer implements WebApplicationInitializer, BaseWebInitializerExtender {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseWebInitializer.class);

	/**
	 * 可以不用子类实现
	 */
	@Override
	public String getMainConfig() {
//		System.out.println(getClass().getName());
//		return "com.ajaxjs.adp.ADPWebInit.ScanComponent";
		return getClass().getName() + ".ScanComponent";
	}

	@Override
	public void onStartup(ServletContext cxt) {
		if (cxt == null) // 可能在测试
			return;

		String mainConfig = getMainConfig();
		cxt.setAttribute("ctx", cxt.getContextPath());
//		servletCxt.setInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
		cxt.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
		cxt.setInitParameter("contextConfigLocation", mainConfig);
		cxt.addListener(new ContextLoaderListener()); // 监听器

		if (!"".equals(mainConfig)) {// 防呆设计
			try {
				Class.forName(mainConfig);
			} catch (ClassNotFoundException e) {
				String reverse = new StringBuffer(mainConfig).reverse().toString();
				reverse = reverse.replaceFirst("\\.", "\\$"); // 对于内部类，我们需要像下面这样写代码
				String _mainConfig = new StringBuffer(reverse).reverse().toString();

				try {
					Class.forName(_mainConfig);
				} catch (ClassNotFoundException e1) {
					LOGGER.warning("找不到 Component Scan 的配置类 " + mainConfig);
				}
			}
		}

		FilterRegistration.Dynamic filterReg = cxt.addFilter("InitMvcRequest", new CharacterEncodingFilter("UTF-8"));
		filterReg.addMappingForUrlPatterns(null, true, "/*");

//		GenericWebApplicationContext webCxt = new GenericWebApplicationContext();
		AnnotationConfigWebApplicationContext webCxt = new AnnotationConfigWebApplicationContext();
		initWeb(cxt, webCxt); // 如果不想在 WebApplicationInitializer 当前类中注入，可以另设一个类专门注入组件

		ServletRegistration.Dynamic registration = cxt.addServlet("dispatcher", new DispatcherServlet(webCxt));
		registration.addMapping("/");
		registration.setLoadOnStartup(1);

		initUpload(cxt, registration);
		LOGGER.info("WEB 程序启动完毕");
	}

	/**
	 * 初始化文件上传
	 * 
	 * @param cxt
	 * @param registration
	 */
	private static void initUpload(ServletContext cxt, Dynamic registration) {
		String tempDir = WebHelper.mappath(cxt, "upload_temp");
		// 如果不存在则创建
		FileHelper.mkDir(tempDir);
		registration.setMultipartConfig(new MultipartConfigElement(tempDir, 50000000, 50000000, 0));// 文件上传
	}

	/**
	 * 文件上传
	 * 
	 * @return
	 */
	@Bean(name = "multipartResolver") // 此处 id 为固定写法，不能随便取名
	public MultipartResolver multipartResolver() {
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		resolver.setResolveLazily(true);// resolveLazily 属性启用是为了推迟文件解析，以在在 UploadAction 中捕获文件大小异常

		return resolver;
	}

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