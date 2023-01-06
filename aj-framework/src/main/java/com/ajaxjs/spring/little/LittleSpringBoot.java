//package com.ajaxjs.spring.little;
//
//import java.io.File;
//
//import org.apache.catalina.Engine;
//import org.apache.catalina.Host;
//import org.apache.catalina.LifecycleException;
//import org.apache.catalina.WebResourceRoot;
//import org.apache.catalina.connector.Connector;
//import org.apache.catalina.core.StandardContext;
//import org.apache.catalina.core.StandardEngine;
//import org.apache.catalina.core.StandardHost;
//import org.apache.catalina.startup.Tomcat;
//import org.apache.catalina.webresources.DirResourceSet;
//import org.apache.catalina.webresources.StandardRoot;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//
//public class LittleSpringBoot {
//    public static void init(int port) {
//        Tomcat tomcat = new Tomcat();
//        tomcat.getHost().setAutoDeploy(false);
//        tomcat.enableNaming();
//
//        setTomcatContext(tomcat);
//
////        WebApplicationContext springCtx = getSpringWebAppContext();
////        // 设置 tomcat 启动后的工作目录
////        Context context1 = tomcat.addContext("/", new File(System.getProperty("java.io.tmpdir")).getAbsolutePath());
////        Tomcat.addServlet(context1, "my", new DispatcherServlet(springCtx)).setLoadOnStartup(1);
////        context1.addServletMappingDecoded("/", "my");
//
//        setConnector(tomcat, port);
//
//        try {
//            tomcat.start(); // tomcat 启动
//        } catch (LifecycleException e) {
//            throw new RuntimeException(e);
//        }
//
//        tomcat.getServer().await(); // 保持主线程不退出，让其阻塞，不让当前线程结束，等待处理请求
//
////        setBaseDir(tomcat);
//    }
//
//    /**
//     * 通过注解的方式初始化 Spring 的上下文
//     *
//     * @return
//     */
//    private static WebApplicationContext getSpringWebAppContext() {
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
////        context.register(AutoConfig.class);
//        context.refresh();
//
//        return context;
//    }
//
//    /**
//     * 一般不用设置，使用默认即可
//     *
//     * @param tomcat
//     */
//    private static void setEngineHost(Tomcat tomcat) {
//        Engine engine = new StandardEngine();// 创建一个引擎,放入 service 中
//        engine.setDefaultHost("localhost");
//        engine.setName("myTomcat");
//
//        Host host = new StandardHost();    // 添加 host
//        host.setName("localhost");
//
//        engine.addChild(host);
//        tomcat.getService().setContainer(engine);
//    }
//
//    /**
//     * 在对应的 host 下面创建一个 context 并制定他的工作路径,会加载该目录下的所有 class 文件,或者静态文件
//     *
//     * @param tomcat
//     */
//    private static void setTomcatContext(Tomcat tomcat) {
//        tomcat.getHost().setAppBase("webapp");
//
//        // 读取项目路径
//        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
//        ctx.setReloadable(false);// 禁止重新载入
//
//        // tomcat 内部读取 Class 执行
//        File additionWebInfClasses = new File("target/classes");// class 文件读取地址
//        WebResourceRoot resources = new StandardRoot(ctx);// 创建WebRoot
//        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
//
////        WebApplicationContext springCtx = getSpringWebAppContext();
//        // 初始化 ContextConfig 配置
////        ctx.addLifecycleListener(new ContextConfig());
////        ctx.addApplicationEventListener(new ContextLoaderListener(springCtx));
//    }
//
//    /**
//     * 设置 tomcat 启动后的工作目录
//     *
//     * @param tomcat
//     */
//    private static void setBaseDir(Tomcat tomcat) {
//        String baseDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        tomcat.setBaseDir(baseDir);
//    }
//
//    /**
//     * 创建连接器,并且添加对应的连接器,同时连接器指定端口 设置 io 协议
//     *
//     * @param tomcat
//     * @param port
//     */
//    private static void setConnector(Tomcat tomcat, int port) {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setPort(port);
//        connector.setThrowOnFailure(true);
//
//        tomcat.getService().addConnector(connector);// 只能设置一个 service,直接拿默认的
//        tomcat.setConnector(connector); // 设置执行器
//    }
//}
