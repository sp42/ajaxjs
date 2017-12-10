<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<html>
	<commonTag:head title="Welcome to Ajaxjs Web Framework's Homepage">
	    <meta name="keywords"    content="ajaxjs java js javascript web ui framework html5 ioc aop orm restful" />
	    <meta name="description" content="AJAXJS Web 是一款基于 Java 平台的开源 Web 框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势， 但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网小型网站的快速应用。" />
	   
	
			<link rel="stylesheet" type="text/css" href="asset/css.css" />
	
	</commonTag:head>

<!-- 	<img style="width:60%;max-width: 400px;margin: 0 auto;margin-top:50px;display: block;" src="asset/images/under_construction.jpg" /> -->
<%-- 	<%=new com.ajaxjs.web.config.InitConfig()%> --%>
<body>
<script>
	;(function(){
		var s = (navigator.userLanguage || navigator.language).toLowerCase();
		var isForce = ${(not empty param.isForce) ? 'true' : 'false'};
	
		if(isForce || s == 'zh-cn' || s == 'zh-tw') {
			
		} else {
			location.assign('index-en.jsp');
		}	
	})();
</script>
    <div class="top"></div>
	<nav class="body">
        <h1>AJAXJS Web Framework</h1>
        <ul>
			<li style="border-bottom-color: #ffed20;"><a href="#">简 介</a></li>
			<li style="border-bottom-color: #2096ff;"><a
				href="http://ajaxjs.mydoc.io/">手 册</a></li>
			<li style="border-bottom-color: #ff8820;"><a href="ui_demo">前端演示</a></li>
			<li style="border-bottom-color: #c720ff;"><a
				href="http://blog.csdn.net/zhangxin09">博 客</a></li>
			<li style="border-bottom-color: #ff0024;"><a href="javadoc">文
					档</a></li>
		</ul>
		

	</nav>
	<div class="green">
		<div class="body">
			<div class="left">
				<h2 style="margin-top: 6%; margin-bottom: 2%;">AJAXJS Web
					Framework</h2>
				<h2 style="margin-bottom: 5%; margin-left: 35%;">全栈快速开发框架</h2>
				<p style="color: #fffbac;">AJAXJS Web 是一款全栈平台的开源 Web
					框架，不仅是服务端框架，还整合了前端库。它使用 HTML5+Java 方案，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势，
					但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网的快速应用。</p>
				<div style="text-align: center; color: #fffbac; font-weight: normal; letter-spacing: 2px;">
					<img src="images/index_06.jpg"> <br>
					<h3>Apache License v2 开源协议</h3>
					<h4>
						<a href="http://git.oschina.net/sp42/ajaxjs">开源中国 Git/SVN Checkout</a>
					</h4>
				</div>
			</div>
			<div class="right">
				<div class="points" style="margin-bottom: 10px; width: 95%;">
					<br>
					<ul>
						<li>极简、务实、高效风格，设计原则：Clean, Simple, Fast & Lightweight.框架特色：全栈、原生、全面、实用。</li>
						<li>遵循“够用就好、就地取材、适合教学”思想，优先考虑 JDK 原生 API，基本没依赖其他重型三方库。简单易学易用。得益于现代浏览器，前端没有使用 jQuery 基础库，而是原生 API 的调用，更高效快速。</li>
						<li>参照 Spring MVC 风格的 Web 框架，功能简单但是 MVC 的核心功能基本具备了，很适合想了解 MVC
							的学习者。尽量多提供源码注释和文档，包括单元测试。</li> 
					</ul>
					<br> <br>
				</div>
			</div>
		</div>
	</div>
	<ul class="body cols">
		<li style="border-right-color: #ff8820;">
			<h5>
				<img src="images/icon_inkboard.png"> 前端简介
			</h5>
			<p>轻量级 UI 组件库，涵盖了目前互联网上各类常见的组件，经实践项目积累沉淀而成；移动端提供高性能的列表控件，高仿真原生效果；
			极简风格，没有依赖其他库，原生开发。复杂 CSS 采用 LESS.js 可复用封装。<a href="ui_demo">»浏览 HTML/CSS/JS 演示</a>
			</p>
		</li>
		<li style="border-right-color: #2096ff;">
			<h5>
				<img src="images/icon_source.png"> 后端简介
			</h5>
			<p>标配有 IOC/AOP/MVC/ORM/RESTful，泛型 Controller/Service/DAO/Model 分层，类似 SpringMVC 的控制器，类似 MyBatis 注解的 SQL 服务，返回数据支持 Java Bean 或更简单的 Map，自动分页，查询条件和后端验证。<a
					href="javadoc">»JavaDoc</a></p>
		</li>
		<li style="border-right-color: #c720ff;">
			<h5>
				<img src="images/icon_database.png">下载 &amp; 运行
			</h5>
			<p>运行系统：Win/Mac/Linux；要求：JRE1.7+/Tomcat7+，浏览器支持 IE8+/Android4+。
				<a href="http://git.oschina.net/sp42/ajaxjs">源码 Git/SVN</a> <a
					href="javadoc">JavaDoc 文档</a> <a href="http://framework.ajaxjs.com/framework/docs/docs.html">手册</a>。QQ 群：3150067 <a href="mailto:support@ajaxjs.com">联系邮箱</a> <a href="http://blog.csdn.net/zhangxin09">作者博客</a>。免费使用、修改，引用请保留头注释。</p>
		</li>
	</ul>
	<footer>
		<div class="body">粤 ICP 证 09002462 号 | Copyright © 1999-2017,
			Sp42</div>
	</footer>
	<!-- CNZZ 统计 -->
	<div style="display:none";>
		<script src="https://s11.cnzz.com/z_stat.php?id=1261173351&web_id=1261173351"></script>
	</div>
	<!-- // CNZZ 统计 -->
</body>
</html>
