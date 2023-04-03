package com.ajaxjs.spring;

import com.ajaxjs.Version;
import com.ajaxjs.framework.spring.filter.FileUploadHelper;
import com.ajaxjs.framework.spring.filter.GlobalExceptionHandler;
import com.ajaxjs.web.WebHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.logger.LogHelper;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
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

import javax.servlet.*;
import javax.servlet.ServletRegistration.Dynamic;
import java.io.File;
import java.io.IOException;

/**
 * 通用的配置，可供 classic/little 使用
 *
 * @author Frank Cheung sp42@qq.com
 */
public abstract class BaseSpringWebInitializer implements WebApplicationInitializer {
    private static final LogHelper LOGGER = LogHelper.getLog(BaseSpringWebInitializer.class);

    {
        LOGGER.info("WEB 程序启动完毕。耗时：" + (System.currentTimeMillis() - startedTime) + "ms");
    }

    /**
     * 获取配置类
     *
     * @return
     */
    public Class<?> getConfigClz() {
        return null;
    }

    /**
     * 基础设置
     */
    @Override
    public void onStartup(ServletContext cxt) throws ServletException {
        if (cxt == null) // 可能在测试
            return;

        // 通过注解的方式初始化 Spring 的上下文
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        // 注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
        if (getConfigClz() != null) ac.register(getConfigClz());
        ac.setServletContext(cxt);
        ac.refresh();

        cxt.setAttribute("ctx", cxt.getContextPath()); // 为 JSP 提供 shorthands

        // 绑定 servlet
//		ServletRegistration.Dynamic registration = cxt.addServlet("dispatcherServlet", new DispatcherServlet(ac));
        Dynamic registration = cxt.addServlet("dispatcher", new DispatcherServlet(ac));
        registration.setLoadOnStartup(1);// 设置 tomcat 启动立即加载 servlet
        registration.addMapping("/"); // 浏览器访问 uri。注意不要设置 /*

        // 字符过滤器
        FilterRegistration.Dynamic filterReg = cxt.addFilter("InitMvcRequest", new CharacterEncodingFilter("UTF-8"));
        filterReg.addMappingForUrlPatterns(null, true, "/*");

        FileUploadHelper.initUpload(cxt, registration);

        LOGGER.info("WEB 程序启动完毕");
    }

    /**
     * 文件上传
     *
     * @return
     */
    @Bean(name = "multipartResolver") // 此处 id 为固定写法，不能随便取名
    public MultipartResolver multipartResolver() {
        return FileUploadHelper.multipartResolver();
    }

    /**
     * YAML 配置文件
     *
     * @return YAML 配置文件
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer cfger = new PropertySourcesPlaceholderConfigurer();
        cfger.setIgnoreUnresolvablePlaceholders(true);// Don't fail if @Value is not supplied in properties. Ignore if not found
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        ClassPathResource c = new ClassPathResource("application.yml");

        if (c.exists()) {
            yaml.setResources(c);
            cfger.setProperties(yaml.getObject());
        } else LOGGER.warning("未设置 YAML 配置文件");

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

    public static long startedTime;

    public static class F implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8"); // 避免乱码
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }

    /**
     * 嵌入式使用 Tomcat
     *
     * @param port
     */
    public static void init(int port, Class<?>... clz) {
        startedTime = System.currentTimeMillis();
        Tomcat tomcat = initTomcat();
        StandardContext ctx = initContext(tomcat);

        // 通过注解的方式初始化 Spring 的上下文，注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
////        if (getConfigClz() != null)

        // CharacterEncodingFilter
//        FilterRegistration.Dynamic filterReg = c.addFilter("InitMvcRequest", new CharacterEncodingFilter("UTF-8"));
//        filterReg.addMappingForUrlPatterns(null, true, "/*");
        FilterDef filter1definition = new FilterDef();
        filter1definition.setFilterName(F.class.getSimpleName());
        filter1definition.setFilterClass(F.class.getName());
        ctx.addFilterDef(filter1definition);

        FilterMap filter1mapping = new FilterMap();
        filter1mapping.setFilterName(F.class.getSimpleName());
        filter1mapping.addURLPattern("/*");
        ctx.addFilterMap(filter1mapping);

        ac.setServletContext(ctx.getServletContext());
        ctx.addApplicationEventListener(new ContextLoaderListener(ac));

        //注册前端控制器
        Wrapper servlet = tomcat.addServlet(ctx, "dispatcherServlet", new DispatcherServlet(ac));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        setTomcatDisableScan(ctx);
        initConnector(tomcat, port);

        try {
            tomcat.start(); // tomcat 启动
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }

        ac.register(clz);
        ac.refresh();
        ac.registerShutdownHook();

        tomcat.getServer().await(); // 保持主线程不退出，让其阻塞，不让当前线程结束，等待处理请求
    }

    /**
     * 读取项目路径
     *
     * @param tomcat
     * @return
     */
    private static StandardContext initContext(Tomcat tomcat) {
        String jspFolder = Resources.getResourcesFromClasspath("META-INF\\resources");// 开放调试阶段，直接读取
        if (jspFolder == null) {
            jspFolder = Resources.getJarDir() + "/../webapp"; // 部署阶段。这个并不会实际保存 jsp。因为 jsp 都在 META-INF/resources 里面。但因为下面的 addWebapp() 又需要
            FileHelper.mkDir(jspFolder);
        }

//        System.out.println("jspFolder::::::" + Resources.getJarDir());
//        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("/mycar/mycar-service-4.0/security-oauth2-uam/sync/jsp").getAbsolutePath());
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", jspFolder);
        ctx.setReloadable(false);// 禁止重新载入

        // seems not work
        WebResourceRoot resources = new StandardRoot(ctx);// 创建 WebRoot
        String classDir = new File("target/classes").getAbsolutePath();
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", classDir, "/"));// tomcat 内部读取 Class 执行

        return ctx;
    }

    private static Tomcat initTomcat() {
        Tomcat tomcat = new Tomcat();
        if (Version.isDebug)
            tomcat.setBaseDir(Resources.getJarDir()); // 设置 tomcat 启动后的工作目录
        tomcat.enableNaming();
        tomcat.getHost().setAutoDeploy(false);
        tomcat.getHost().setAppBase("webapp");

        return tomcat;
    }

    /**
     * 创建连接器，并且添加对应的连接器，同时连接器指定端口 设置 IO 协议
     *
     * @param tomcat
     * @param port
     */
    private static void initConnector(Tomcat tomcat, int port) {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(port);
        connector.setThrowOnFailure(true);

        tomcat.getService().addConnector(connector);// 只能设置一个 service,直接拿默认的
        tomcat.setConnector(connector); // 设置执行器
    }

    /**
     * 禁止 Tomcat 自动扫描 jar 包，那样会很慢
     *
     * @param ctx
     */
    private static void setTomcatDisableScan(StandardContext ctx) {
        StandardJarScanFilter filter = (StandardJarScanFilter) ctx.getJarScanner().getJarScanFilter();
//        filter.setDefaultTldScan(false);
        /*
            这个对启动 tomcat 时间影响很大
            又
            很多 Servlet 3.0 新特性，不能禁掉，比如在 jar 里面放 jsp（部署时候就会这样，但开放阶段不用）。
            故，用 isDebug 判断下
        */
        if (Version.isDebug)
            filter.setDefaultPluggabilityScan(false);
//        String oldTldSkip = filter.getTldSkip();
//        System.out.println("-------" + oldTldSkip);
//        String newTldSkip = oldTldSkip == null || oldTldSkip.trim().isEmpty() ? "pdq.jar" : oldTldSkip + ",pdq.jar";
//        filter.setTldSkip(newTldSkip);
    }
}
