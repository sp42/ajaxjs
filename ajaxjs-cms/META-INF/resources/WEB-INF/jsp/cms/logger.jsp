<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="后台日誌管理" />
	</jsp:include>
</head>
<body>
	<div>
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">后台日誌</template>
		</ajaxjs-admin-header>
		<pre style="margin:0 2%;">
三月 28, 2018 1:59:34 下午 com.ajaxjs.config.SiteStruService .contextInitialized(SiteStruService.java:84)
信息: 加载网站的结构文件成功
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.MvcDispatcher .init(MvcDispatcher.java:88)
信息: AJAXJS MVC 服务启动之中……
三月 28, 2018 1:59:34 下午 com.ajaxjs.ioc.BeanContext .getBeanInstance(BeanContext.java:203)
信息: 需要 AOP 处理，类：class com.ajaxjs.cms.service.ArticleServiceImpl
三月 28, 2018 1:59:34 下午 com.ajaxjs.ioc.BeanContext .getBeanInstance(BeanContext.java:203)
信息: 需要 AOP 处理，类：class com.ajaxjs.cms.service.HrServiceImpl
三月 28, 2018 1:59:34 下午 com.ajaxjs.ioc.BeanContext .init(BeanContext.java:125)
警告: IOC 已经初始化
三月 28, 2018 1:59:34 下午 com.ajaxjs.ioc.BeanContext .init(BeanContext.java:127)
警告: IOC 传入的类 [] 为空！请检查包名是否正确
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/config" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin/config" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/page_editor" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin/page_editor" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/article" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin/article" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/catalog" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin/catalog" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/DataBaseConnection" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin/DataBaseConnection" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/DataDict" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin/DataDict" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/GlobalLog" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)
信息: The controller "admin/GlobalLog" was parsed and registered
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:62)
信息: This controller "admin/Hr" is being parsing
三月 28, 2018 1:59:34 下午 com.ajaxjs.mvc.controller.ControllerScanner .add(ControllerScanner.java:89)		
		
		</pre>
	</div>

	<script>
		new Vue({el:'body > div'});
	</script>

</body>
</html>
