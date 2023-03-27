package com.ajaxjs.spring;

import java.io.File;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.spring.filter.UTF8CharsetFilter;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 嵌入式使用 Tomcat
 */
public class EmbeddedTomcatStarter {
	private static final LogHelper LOGGER = LogHelper.getLog(EmbeddedTomcatStarter.class);

	Tomcat tomcat;

	StandardContext context;

	public static long startedTime;
	public static long springTime;

	/**
	 * LifecycleState.STARTING_PREP 会执行两次，不知为何
	 */
	public boolean isStatedSpring;

	static class WebAppInitializer implements WebApplicationInitializer {
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

			// 绑定 servlet
			Dynamic registration = ctx.addServlet("dispatcher", new DispatcherServlet(ac));
			registration.setLoadOnStartup(1);// 设置 tomcat 启动立即加载 servlet
			registration.addMapping("/"); // 浏览器访问 uri。注意不要设置 /*

			// 字符过滤器
//	        new CharacterEncodingFilter("UTF-8")
			FilterRegistration.Dynamic filterReg = ctx.addFilter("InitMvcRequest", new UTF8CharsetFilter());
			filterReg.addMappingForUrlPatterns(null, true, "/*");

			springTime = System.currentTimeMillis() - startedTime;
		}
	}

	public void init(int port, Class<?>... clz) {
		initTomcat();
		initContext();

//        initFilterByTomcat(UTF8CharsetFilter.class);

		context.addLifecycleListener((LifecycleEvent event) -> {
			if (isStatedSpring || (event.getLifecycle().getState() != LifecycleState.STARTING_PREP))
				return;

			new WebAppInitializer(clz).onStartup(context.getServletContext());
//			anotherWayToStartStrping();

			isStatedSpring = true;
		});

		context.addLifecycleListener(new Tomcat.FixContextListener());// required if you don't use web.xml
		// 通过注解的方式初始化 Spring 的上下文，注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
//        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
//        ac.setServletContext(context.getServletContext());
//        context.addApplicationEventListener(new ContextLoaderListener(ac));

		// 注册前端控制器
//        Wrapper servlet = Tomcat.addServlet(context, "dispatcherServlet", new DispatcherServlet(ac));
//        servlet.setLoadOnStartup(1);
//        servlet.addMapping("/");

		setTomcatDisableScan();
		initConnector(port);

		try {
			tomcat.start(); // tomcat 启动
		} catch (LifecycleException e) {
			throw new RuntimeException(e);
		}

//        ac.register(clz);
//        ac.refresh();
//        ac.registerShutdownHook();

		String tpl = "Web 服务启动完毕。Spring 耗时：%sms，总耗时：%sms";
		tpl = String.format(tpl, springTime, System.currentTimeMillis() - startedTime);
		LOGGER.info(tpl);

		tomcat.getServer().await(); // 保持主线程不退出，让其阻塞，不让当前线程结束，等待处理请求
	}

	/**
	 * 另外一种方式启动 Spring。但不能加入配置类 clz，且更繁琐。 WebAppInitializer 需要实现
	 * WebApplicationInitializer
	 */
	private void anotherWayToStartStrping() {
		try {
			new SpringServletContainerInitializer().onStartup(new HashSet<Class<?>>() {
				private static final long serialVersionUID = 1L;

				{
					add(WebAppInitializer.class);
				}
			}, context.getServletContext());
		} catch (Throwable e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 在 Tomcat 初始化阶段设置 Filter
	 */
	private void initFilterByTomcat(Class<? extends Filter> filterClz) {
		FilterDef filter1definition = new FilterDef();
		filter1definition.setFilterName(filterClz.getSimpleName());
		filter1definition.setFilterClass(filterClz.getName());
		context.addFilterDef(filter1definition);

		FilterMap filter1mapping = new FilterMap();
		filter1mapping.setFilterName(filterClz.getSimpleName());
		filter1mapping.addURLPattern("/*");
		context.addFilterMap(filter1mapping);
	}

	/**
	 * 初始化 Tomcat 实例
	 */
	private void initTomcat() {
		startedTime = System.currentTimeMillis();

		tomcat = new Tomcat();
		tomcat.enableNaming();
		tomcat.getHost().setAutoDeploy(false);
		tomcat.getHost().setAppBase("webapp");

		close();
	}

	/**
	 * 读取项目路径
	 */
	private void initContext() {
		// 在对应的 host 下面创建一个 context 并制定他的工作路径,会加载该目录下的所有 class 文件,或者静态文件
//        tomcat.setBaseDir(Thread.currentThread().getContextClassLoader().getResource("").getPath()); // 设置 tomcat 启动后的工作目录
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());

//        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("C:\\code\\auth-git\\security-console-sync\\security-console-sync\\src\\main\\webapp").getAbsolutePath());
		System.out.println(System.getProperty("user.dir"));
		String jspDir = System.getProperty("user.dir")
				+ "\\security-console-sync\\security-console-sync\\src\\main\\webapp";
//        String jspDir = System.getProperty("user.dir") + "\\src\\main\\webapp";
		jspDir = "c:\\temp";
		context = (StandardContext) tomcat.addWebapp("", new File(jspDir).getAbsolutePath()); // 第一个参数不用设置 /
		context.setReloadable(false);// 禁止重新载入
		WebResourceRoot resources = new StandardRoot(context);// 创建WebRoot
		resources.addPreResources(
				new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));// tomcat
																														// 内部读取
																														// Class
																														// 执行
	}

	/**
	 * 创建连接器，并且添加对应的连接器，同时连接器指定端口 设置 IO 协议
	 *
	 * @param port
	 */
	private void initConnector(int port) {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(port);
		connector.setThrowOnFailure(true);

		tomcat.getService().addConnector(connector);// 只能设置一个 service,直接拿默认的
		tomcat.setConnector(connector); // 设置执行器
	}

	/**
	 * 禁止 Tomcat 自动扫描 jar 包，那样会很慢
	 */
	private void setTomcatDisableScan() {
		StandardJarScanFilter filter = (StandardJarScanFilter) context.getJarScanner().getJarScanFilter();
//        filter.setDefaultTldScan(false);
		filter.setDefaultPluggabilityScan(false);
//        String oldTldSkip = filter.getTldSkip();
//        System.out.println("-------" + oldTldSkip);
//        String newTldSkip = oldTldSkip == null || oldTldSkip.trim().isEmpty() ? "pdq.jar" : oldTldSkip + ",pdq.jar";
//        filter.setTldSkip(newTldSkip);
	}

	public void close() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				LOGGER.info("关闭 Tomcat");
				tomcat.destroy();
			} catch (LifecycleException e) {
				LOGGER.warning(e);
			}
		}));
	}
}
