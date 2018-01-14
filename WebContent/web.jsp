<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AJAXJS Web</title>
<link rel="icon" type="image/x-icon"
	href="/framework/asset/images/favicon.ico" />
<link rel="shortcut icon" type="image/x-icon"
	href="/framework/asset/images/favicon.ico" />
<meta name="keywords"
	content="ajaxjs java js javascript web ui framework html5 ioc aop orm restful" />
<meta name="description"
	content="AJAXJS Web 是一款基于 Java 平台的开源 Web 框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势， 但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网小型网站的快速应用。" />

<meta name="viewport"
	content="width=320, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
<style>
body {
	padding: 0;
	margin: 0;
	font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 16px;
	line-height: 1.5;
	color: #606c71;
}

.page-header {
	padding: 5rem 6rem;
	color: #fff;
	text-align: center;
	background-color: #159957;
	background-image: linear-gradient(120deg, #b1cef6, #b59ba4);
}

a {
	color: #1e6bb8;
	text-decoration: none;
	font-size: 1rem;
}

h1 {
	font-size: 3.25rem;
	margin-top: 0;
	margin-bottom: 0.1rem;
	text-shadow: 1px 1px 1px gray;
}

h2 {
	font-size: 1.25rem;
	margin-bottom: 2rem;
	font-weight: normal;
	opacity: 0.7;
}

h3 {
	margin-top: 2rem;
	margin-bottom: 1rem;
	color: #b39ca4;
	letter-spacing: 2px;
}

.main-content {
	max-width: 64rem;
	padding: 1rem 6rem;
	margin: 0 auto;
	font-size: 1.1rem;
}

footer {
	padding-top: 2rem;
	margin-top: 2rem;
	border-top: solid 1px #eff0f1;
}

.language {
	float: right;
}

li {
	margin-bottom: 1%;
}

@media screen and (max-width:480px) {
	/*手机端浏览器所显示的网页CSS*/
	h1 {
		font-size: 2.25rem;
	}
	h2 {
		font-size: 1rem;
	}
	.body {
		max-width: 330px;
	}
	.page-header {
		padding: 1rem;
	}
	.main-content {
		padding: 0 1rem;
	}
}
</style>
<%
	java.util.Locale l = request.getLocale();
	String locate = l.toString(), targetLanguage = null;

	if (locate.indexOf("en") != -1) {
		targetLanguage = "eng";
	} else if (locate.indexOf("zh") != -1 || locate == null) {
		targetLanguage = "zhCN";
	}

	String force = request.getParameter("force");

	if (force != null) {
		targetLanguage = force;
	}
%>
</head>
<body>
	<%
		if (targetLanguage.equals("eng")) {
	%>
	<section class="page-header">
		<h1>AJAXJS Web Framework</h1>
		<h2 class="project-tagline">Clean, Simple, Native &amp;
			Lightweight</h2>
	</section>

	<section class="main-content">
		<h3>A Full Stack Web Framework</h3>

		<p>AJAXJS Web aims to full-stack, not only the server-side
			framework, but also integrates the front-end library. It's written
			in HTML5 + Java, a successor to the JVM platform, efficient, secure,
			stable, cross-platform and many other advantages, but it abandoned
			the traditional enterprise architecture brought about by the large
			and bloated, emphasizing the lightweight, and fast, very suitable for
			the Internet fast application.</p>

		<ul>
			<li>Follow the "enough to use, suitable for teaching" thought,
				give priority to native API, basically don't rely on other heavy
				3rd-party library.</li>
			<li>Using IOC/AOP/MVC/ORM/RESTful, Generic
				Controller/Service/DAO/Model</li>
			<li>With the style of SpringMVC, the code is simple but the core
				functions of MVC are basically available, it's suitable for those
				who want to understand MVC.</li>
			<li>MyBatis-like annotation SQL service, supports Java Bean or
				Map as entity, auto-paging, auto-query and validtion.</li>
			<li>Collecting many years of various HTML/DHTML/CSS best
				practices, providing many widgets. Small size, 13kb for all base and
				widgets.</li>
			<li>Thanks to modern browser, the front end doesn't use jQuery,
				but the native API calls, more efficient and fast.</li>
			<li>Reuse responsive CSS/LESS.js for mobile.</li>
			<li>Providing code comment, unit test and documentation as much
				as possible.</li>
		</ul>

		<p>
			AJAXJS Web is built on <a href="base.jsp">AJAXJS
				Base</a>, a pure Java library. Yet another high-level <a href="https://gitee.com/sp42/ajaxjs-cms">AJAXJS
				CMS</a> is built on AJAXJS Web.
		</p>
		<h3>Demos</h3>
		<a href="ui_demo">HTML/CSS/JS</a> | <a href="ui_demo">Simple Admin</a>

		<h3>Download &amp; Run</h3>
		<p>
			<a href="http://ajaxjs.mydoc.io/">User Manual</a> | <a
				href="https://gitee.com/sp42/ajaxjs">Source on Git/SVN</a> | <a
				href="ajaxjs-web-doc">JavaDoc</a>
		</p>
		<p>Win/Mac/Linux/JRE1.7+/Tomcat7+/IE8+ Android4+</p>
		<p>Apache License Version--2.0. Free to use, modify.</p>

		<h3>Contact</h3>

		<a target="_blank"
			href="//shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">QQ
			Group：3150067</a> | <a href="mailto:support@ajaxjs.com">Email</a> | <a
			href="http://blog.csdn.net/zhangxin09">Blog</a>



		<footer>
			<span class="language"> <a href="?force=zhCN">简体中文</a>|<a
				href="?force=big">正体中文</a>
			</span> ©1999-2018 Frank

		</footer>

	</section>
	<%
		} else if (targetLanguage.equals("zhCN")) {
	%>
	<style>
p, li {
	letter-spacing: 1px;
	text-align: justify;
	word-break: break-all;
}
</style>
	<section class="page-header">
		<h1>AJAXJS Web Framework</h1>
		<h2 class="project-tagline">极简、务实、原生 &amp; 轻量级</h2>
	</section>

	<section class="main-content">
	<!-- Editable AREA|START -->
		<h3>一个全栈的 Web 框架</h3>

		<p>AJAXJS Web 是一款全栈的开源 Web 框架，不仅是服务端框架，还整合了前端库。它使用 HTML5+Java
			方案，继承了 Java
			平台的高效、安全、稳定、跨平台等诸多优势，但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网的快速应用。</p>

		<ul>
			<li>简单易学易用，遵循“够用就好、就地取材、适合教学”，优先考虑 JDK 原生 API，基本没依赖其他重型三方库。</li>
			<li>标配有 IOC/AOP/MVC/ORM/RESTful，泛型 Controller/Service/DAO/Model
				分层。</li>
			<li>参照 Spring MVC 风格的 Web 框架，功能简单但具备了 MVC 的核心功能，很适合想了解 MVC 的学习者。</li>
			<li>提供类似 MyBatis 注解的 SQL 服务，返回数据支持 Java Bean 或更简单的
				Map，自动分页，查询条件和后端验证。</li>
			<li>轻量级 UI
				组件库，涵盖了目前互联网上各类常见的组件，经实践项目积累沉淀而成；移动端提供高性能的列表控件，高仿真原生效果。</li>
			<li>得益于现代浏览器，前端没有使用 jQuery 基础库，而是原生 API 的调用，更高效快速。</li>
			<li>复杂 CSS 采用 LESS.js 可复用封装。</li>
			<li>尽量多提供源码注释和文档，包括单元测试。</li>
		</ul>
		<p>
			AJAXJS Web 建基于纯 Java 库 <a href="base.jsp">AJAXJS
				Base</a>。 还有 <a href="https://gitee.com/sp42/ajaxjs-cms">AJAXJS
				CMS</a> 是建基于 AJAXJS Web 的。
		</p>
		<h3>演示</h3>
		<a href="ui_demo">HTML/CSS/JS</a> | <a href="ui_demo">简易后台</a>

		<h3>文档 &amp; 源码</h3>
		<p>
			<a href="http://ajaxjs.mydoc.io/">用户手册</a> | <a href="ajaxjs-web-doc">JavaDoc</a>
			| <a href="https://gitee.com/sp42/ajaxjs">Git/SVN 源码</a>
		</p>
		<p>Win/Mac/Linux/JRE1.7+/Tomcat7+/IE8+ Android4+.</p>
		<p>Apache License Version--2.0. 免费使用、修改，引用请保留头注释。</p>

		<h3>联系</h3>

		<a target="_blank"
			href="//shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">Q群：3150067</a>
		| <a href="mailto:support@ajaxjs.com">Email</a> | <a
			href="http://blog.csdn.net/zhangxin09">博客</a>

		<footer>
			粤 ICP 证 09002462 号 | ©1999-2018 Frank <span class="language">
				<a href="?force=eng">English</a>|<a href="?force=zhTW">正体中文</a>
			</span>

		</footer>
<!-- Editable AREA|END -->
	</section>

	<%
		}
	%>


	<!-- CNZZ 统计 -->
	<div style="display: none;">
		<script
			src="https://s11.cnzz.com/z_stat.php?id=1261173351&web_id=1261173351"></script>
	</div>
	<!-- // CNZZ 统计 -->
</body>
</html>
