package com.ajaxjs.framework.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.framework.spring.filter.CleanUpMySql;
import com.ajaxjs.framework.spring.filter.FileUploadHelper;
import com.ajaxjs.framework.spring.filter.UTF8CharsetFilter;
import com.ajaxjs.spring.easy_controller.ServiceBeanDefinitionRegistry;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 配置 Spring MVC 实现该接口的类会在 Servlet 容器启动时自动加载并运行
 * <p>
 * 这是依赖 Tomcat（非嵌入式）的使用场景
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

		coreStartup(cxt, null);

		String mainConfig = getMainConfig();

		cxt.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
		cxt.setInitParameter("contextConfigLocation", mainConfig);
//		servletCxt.setInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
		cxt.addListener(new CleanUpMySql());

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

//		initWeb(cxt, webCxt); // 如果不想在 WebApplicationInitializer 当前类中注入，可以另设一个类专门注入组件
		LOGGER.info("Web 程序启动完毕");
	}

	public static void coreStartup(ServletContext ctx, Class<?>[] clz) {
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

	public ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry(Class<? extends BaseWebInitializer> clz) {
		return new ServiceBeanDefinitionRegistry(clz.getPackage().getName());
	}
}