package com.ajaxjs.framework.spring;

import com.ajaxjs.framework.embeded_tomcat.TomcatConfig;
import com.ajaxjs.framework.embeded_tomcat.TomcatStarter;
import com.ajaxjs.framework.spring.filter.FileUploadHelper;
import com.ajaxjs.framework.spring.filter.UTF8CharsetFilter;
import com.ajaxjs.util.logger.LogHelper;
import org.apache.catalina.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 嵌入式使用 Tomcat
 */
public class EmbeddedTomcatStarter extends TomcatStarter {
    private static final LogHelper LOGGER = LogHelper.getLog(EmbeddedTomcatStarter.class);

    /**
     * LifecycleState.STARTING_PREP 会执行两次，不知为何
     */
    public boolean isStatedSpring;

    /**
     * 配置类
     */
    Class<?>[] clz;

    public EmbeddedTomcatStarter(TomcatConfig cfg, Class<?>[] clz) {
        super(cfg);
        this.clz = clz;
    }

    @Override
    public void onContextReady(Context context) {
        context.addLifecycleListener((LifecycleEvent event) -> {
            if (isStatedSpring || (event.getLifecycle().getState() != LifecycleState.STARTING_PREP))
                return;

            ServletContext ctx = context.getServletContext();

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
            ServletRegistration.Dynamic registration = ctx.addServlet("dispatcher", new DispatcherServlet(ac));
            registration.setLoadOnStartup(1);// 设置 tomcat 启动立即加载 servlet
            registration.addMapping("/"); // 浏览器访问 uri。注意不要设置 /*

            // 字符过滤器
            FilterRegistration.Dynamic filterReg = ctx.addFilter("InitMvcRequest", new UTF8CharsetFilter());
            filterReg.addMappingForUrlPatterns(null, true, "/*");

            FileUploadHelper.initUpload(ctx, registration);

            if (cfg.isEnableJMX())
                connectMBeanServer();

            isStatedSpring = true;
            springTime = System.currentTimeMillis() - startedTime;
        });
    }

    /**
     * 启动 Web 程序
     *
     * @param port 端口
     * @param clz  配置类列表
     */
    public static void start(int port, Class<?>... clz) {
        TomcatConfig cfg = new TomcatConfig();
        cfg.setPort(port);

        new EmbeddedTomcatStarter(cfg, clz).start();
    }

    /**
     * 启动 Web 程序
     *
     * @param port        端口
     * @param contextPath 程序上下文目录
     * @param clz         配置类列表
     */
    public static void start(int port, String contextPath, Class<?>... clz) {
        TomcatConfig cfg = new TomcatConfig();
        cfg.setPort(port);
        cfg.setContextPath(contextPath);

        new EmbeddedTomcatStarter(cfg, clz).start();
    }
}
