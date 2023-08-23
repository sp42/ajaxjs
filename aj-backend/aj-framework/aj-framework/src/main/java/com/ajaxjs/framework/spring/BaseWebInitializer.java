package com.ajaxjs.framework.spring;

import com.ajaxjs.framework.spring.filter.FileUploadHelper;
import com.ajaxjs.framework.spring.filter.UTF8CharsetFilter;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

/**
 * 配置 Spring MVC 实现该接口的类会在 Servlet 容器启动时自动加载并运行
 *
 * @author Frank Cheung
 */
public abstract class BaseWebInitializer implements WebApplicationInitializer {
    private static final LogHelper LOGGER = LogHelper.getLog(BaseWebInitializer.class);

    /**
     * 这是依赖 Tomcat（非嵌入式）的使用场景
     *
     * @param cxt the {@code ServletContext} to initialize
     */
    @Override
    public void onStartup(ServletContext cxt) {
        coreStartup(cxt, getClass());

//		String mainConfig = getMainConfig();
//		LOGGER.info(mainConfig);

//		cxt.setInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
//		cxt.setInitParameter("contextConfigLocation", mainConfig);
//		cxt.addListener(new CleanUpMySql()); // ServletContextListener

//		if (!"".equals(mainConfig)) {// 防呆设计
//			try {
//				Class.forName(mainConfig);
//			} catch (ClassNotFoundException e) {
//				String reverse = new StringBuffer(mainConfig).reverse().toString();
//				reverse = reverse.replaceFirst("\\.", "\\$"); // 对于内部类，我们需要像下面这样写代码
//				String _mainConfig = new StringBuffer(reverse).reverse().toString();
//
//				try {
//					Class.forName(_mainConfig);
//				} catch (ClassNotFoundException e1) {
//					LOGGER.warning("找不到 Component Scan 的配置类 " + mainConfig);
//				}
//			}
//		}

        LOGGER.info("Web 程序启动完毕");
    }

    public static void coreStartup(ServletContext ctx, Class<?>... clz) {
        if (ctx == null) // 可能在测试
            return;

        // 通过注解的方式初始化 Spring 的上下文，注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.setServletContext(ctx);
        if (!ObjectUtils.isEmpty(clz))
            ac.register(clz);
        ac.refresh();
        ac.registerShutdownHook();

        ctx.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
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