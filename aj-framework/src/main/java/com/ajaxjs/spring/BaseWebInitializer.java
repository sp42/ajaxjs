package com.ajaxjs.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.spring.easy_controller.ServiceBeanDefinitionRegistry;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 配置 Spring MVC 实现该接口的类会在 Servlet 容器启动时自动加载并运行
 *
 * @author Frank Cheung
 */
public abstract class BaseWebInitializer extends BaseSpringWebInitializer implements BaseWebInitializerExtender {
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

	public ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry(Class<? extends BaseWebInitializer> clz) {
		return new ServiceBeanDefinitionRegistry(clz.getPackage().getName());
	}
}