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
        // 通过注解的方式初始化 Spring 的上下文
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        // 注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
        if (getConfigClz() != null)
            ac.register(getConfigClz());
        ac.setServletContext(servletContext);
        ac.refresh();


        // 绑定 servlet
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcherServlet", new DispatcherServlet(ac));
        registration.setLoadOnStartup(1);// 设置 tomcat 启动立即加载 servlet
        registration.addMapping("/"); // 浏览器访问 uri

        // 设置 tomcat 启动后的工作目录
//        Context context1 = tomcat.addContext("/", new File(System.getProperty("java.io.tmpdir")).getAbsolutePath());
//        Tomcat.addServlet(context1, "my", new DispatcherServlet(springCtx)).setLoadOnStartup(1);
//        context1.addServletMappingDecoded("/", "my");

        // 初始化 ContextConfig 配置
//        StandardContext ctx;
//        ctx.addLifecycleListener(new ContextConfig());
//        ctx.addApplicationEventListener(new ContextLoaderListener(ac));

        initUpload(servletContext, registration);
        LOGGER.info("WEB 程序启动完毕");
    }
}
