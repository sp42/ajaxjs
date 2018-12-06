<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

	<meta charset="UTF-8">
	<title>AJAXJS Web UI</title>
	<link rel="icon" type="image/x-icon" href="/framework/asset/images/favicon.ico" />
	<link rel="shortcut icon" type="image/x-icon" href="/framework/asset/images/favicon.ico" />
	<meta name="keywords" content="ajaxjs java js javascript web ui framework html5 ioc aop orm restful" />
	<meta name="description" content="AJAXJS Web 是一款基于 Java 平台的开源 Web 框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势， 但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网小型网站的快速应用。" />
	<meta name="viewport" content="width=320, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
	
	<%request.setAttribute("ajaxjsui", request.getContextPath()); %>
	<script src="${pageContext.request.contextPath}/js/libs/code-prettify.min.js"></script>  
	
	<script src="${ajaxjsui}/js/libs/vue.js"></script>
    <script src="${ajaxjsui}/js/ajaxjs-base.js"></script>
    <script src="${ajaxjsui}/js/widgets/admin.js"></script>
    <script src="${ajaxjsui}/js/widgets/form.js"></script>
    <script src="${ajaxjsui}/js/widgets/list.js"></script>
    <script src="${ajaxjsui}/js/widgets/menu.js"></script>
    <script src="${ajaxjsui}/js/widgets/msg.js"></script>
    <script src="${ajaxjsui}/js/widgets/page.js"></script>
    <script src="${ajaxjsui}/js/widgets/tree.js"></script>
    <script src="${ajaxjsui}/js/widgets/upload.js"></script>
    <script src="${ajaxjsui}/js/widgets/carousel.js"></script>
    <script src="${ajaxjsui}/js/widgets/marquee.js"></script>

	<link rel="stylesheet" type="text/css" href="index.css" />
	<link rel="stylesheet" type="text/css" href="less.css" />
</head>
 
<body>
	<section class="page-header">
		<h1>AJAXJS UI</h1>
		<h2 class="project-tagline">Vue.js, Less.js &amp; Clean Code</h2>
	</section>

	<section class="main-content">

		<menu>
			<ul>
				<li>
					<a href=".">Getting Start</a>
				</li>
				<li>
					<a href="?g=base">Base</a>
				</li>
				<li>
					<a href="?g=msgbox">Modal</a>
				</li>
				<li>
					<a href="?g=tab">Tab</a>
				</li>
				<li>
					<a href="?g=tree">Tree</a>
				</li>
				<li>
					<a href="?g=carousel">Carousel</a>
				</li>
				<li>
					<a href="?g=navmenu">Menu</a>
				</li>
				<li>
					<a href="?g=form">Form</a>
				</li>
				<li>
					<a href="?g=formcontrol">Form Control</a>
				</li>
				<li>
					<a href="?g=list">List</a>
				</li>
				<li>
					<a href="?g=pages">PageTools</a>
				</li>
		
				<li>
					<a href="?g=marquee">Marquee</a>
				</li>
				<li>
					<a href="?g=css">CSS Layout</a>
				</li>
			</ul>
		</menu>
		
		<section>
	<%if("msgbox".equals(request.getParameter("g"))) { %>		
			<%@include file="include/msgbox.jsp" %>
	<%} else if("base".equals(request.getParameter("g"))) { %>		
			<%@include file="include/base.jsp" %>
	<%} else if("tab".equals(request.getParameter("g"))) { %>		
			<%@include file="include/tab.jsp" %>
	<%} else if("tree".equals(request.getParameter("g"))) { %>		
			<%@include file="include/tree.jsp" %>
	<%} else if("navmenu".equals(request.getParameter("g"))) { %>		
			<%@include file="include/navmenu.jsp" %>
	<%} else if("form".equals(request.getParameter("g"))) { %>		
			<%@include file="include/form.jsp" %>
	<%} else if("formcontrol".equals(request.getParameter("g"))) { %>		
			<%@include file="include/formcontrol.jsp" %>
	<%} else if("carousel".equals(request.getParameter("g"))) { %>		
			<%@include file="include/carousel.jsp" %>
	<%} else if("list".equals(request.getParameter("g"))) { %>		
			<%@include file="include/list.jsp" %>
	<%} else if("marquee".equals(request.getParameter("g"))) { %>		
			<%@include file="include/marquee.jsp" %>
	<%} else if("pages".equals(request.getParameter("g"))) { %>		
			<%@include file="include/pages.jsp" %>
	<%} else if("css".equals(request.getParameter("g"))) { %>		
			<%@include file="include/css.jsp" %>
	<%} else { %>	
			<h4>Welcome!</h4>
			<p>&nbsp;&nbsp;&nbsp;&nbsp;AJAXJS Framework 旨在打造一个极简风格的、全栈的、简单明了的  Web 框架——此 UI 库正是该框架的前端组件库部分。AJAXJS UI = vue.js + less.js + 原生组件。
			而关于后台框架更多信息，可访问 <a href="https://framework.ajaxjs.com">AJAXJS Framework</a> 主页。</p>

			<p>特点简介：</p>
			<ul class="list">
				<li>鼓励绑定式语法，声明式编程， UI 还是推荐多使用标签，这点和 Vue.js 不谋而合。</li>
				<li>组件化封装，灵活使用 mixins，避免冗余逻辑</li>
				<li>没有复杂的配置，避免陷入前端复杂的工具深渊，仅仅通过 &lt;script src="vue.js"&gt; 使用</li>
				<li>代码简洁清爽，思路清晰，逻辑简单</li>
			</ul>
			<p>支持浏览 IE10+/Android4.4+/iOS8+。</p>
			<p><a href="https://gitee.com/sp42_admin/ajaxjs">Git/SVN 源码</a>，前端源码位于 /asset/ajaxjsui 下 | <a href="http://blog.csdn.net/zhangxin09">作者博客</a> | QQ 群：<a href="https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">3150067</a> </p>
<!-- 			<img src="head.jpg" width="350" style="margin: 0 auto;display: block;" /> -->
			<p>Apache License Version--2.0. 免费使用、修改，引用请保留头注释。</p>
			
			<p>推荐项目：极致性能的 JS 模板引擎 <a href="https://github.com/jojoin/tppl" target="_blank">TPPL</a> | 
			简易异步流程控制器 <a href="https://github.com/creationix/step" target="_blank">Step.js</a>| 早期大神 <a href="http://cloudgamer.cnblogs.com/" target="_blank">cloudgamer</a> |  
			<a href="http://bluegriffon.org/" target="_blank">BlueGriffon HTML Editor</a> </p>

			
	<%} %>	
		</section>
	</section>


	<!-- CNZZ 统计 -->
	<div style="display: none;">
		<script
			src="https://s11.cnzz.com/z_stat.php?id=1261173351&web_id=1261173351"></script>
	</div>
	<!-- // CNZZ 统计 -->
	
	<footer>
		<div class="center" style="text-align:center;">
			©copyright 2018 QQ 群：<a href="https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">3150067</a> | <a href="../../">AJAXJS Framework</a> | <a href="http://blog.csdn.net/zhangxin09">作者博客</a>
			<br />
			粤ICP备18053388号
		</div>
	</footer>
</body>

</html>