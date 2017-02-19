#AJAXJS base-util


IOC 注入
-------
支持简易的反转控制，参见[《极简版 Java 依赖注射 》](http://blog.csdn.net/zhangxin09/article/details/43161215)。

Controller 中 service 需要被注入：

	import com.ajaxjs.util.ioc.Bean;
	import com.ajaxjs.util.ioc.Resource;
	@Controller
	@Path("/version")
	@Bean("versionController") 
	public class VersionController extends ReadOnlyController<Version> {
		@Resource("versionService") // 指定 service id
		private VersionService service;
	 
	}
	
Service

	import com.ajaxjs.util.ioc.Bean;

	/**
	 * 版本更新
	 * @author frank
	 *
	 */
	@Bean("versionService")
	public class VersionService extends BaseCrudService<Version, VersionDAO> {
	 
	}

测试注入：

	import com.ajaxjs.util.ioc.BeanContext;
	import com.ajaxjs.util.ioc.Scanner;
	
	public class TestIOC {
	
		@Test
		public void test() {
			// 指定扫描的包名
			// 下面 service 被注入到 controller 中
			Set<Class<?>> classes = new Scanner().scanPackage("com.ajaxjs.app.controller", new Scanner().scanPackage("com.ajaxjs.app.service"));
			
			// 注入
			BeanContext.me().init(classes);
	
			// 按照 bean 名称获取实例
			VersionController versionController = (VersionController) BeanContext.me().getBean("versionController");
	
			// 依赖的 versionService 注入到 versionController
			System.out.println(versionController.getService());
			assertNotNull(versionController.getService());
		}
	}
	
