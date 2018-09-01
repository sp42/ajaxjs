<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/asset/common/jsp/head.jsp">
		<jsp:param name="title" value="AJAXJS UI" />
	</jsp:include>
	<script src="${ajaxjsui}/js/libs/run_prettify.js"></script>  

	<link rel="stylesheet" type="text/css" href="index.css" />
	<link rel="stylesheet" type="text/css" href="less.css" />
</head>
 
<body>
	<section class="page-header">
		<h1>AJAXJS UI</h1>
		<h2 class="project-tagline">LESS, Widgets &amp;
			Java Tags</h2>
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
			<p>&nbsp;&nbsp;&nbsp;&nbsp;AJAXJS Framework 旨在打造一个极简风格的、全栈的、简单明了的  Web 框架——此 UI 库正是该框架的前端组件库部分。AJAXJS UI = vue.js + less.js + 原生组件
			关于后台框架更多信息，可访问 <a href="https://framework.ajaxjs.com">AJAXJS Framework</a> 主页。</p>

			<p>用法简介如下：</p>
			<ul class="list">
				<li>定义一空白 LESS 文件，导入所需的包。然后在这 LESS 文件定义或覆盖样式。</li>
				<li>引入 js <a href="ajaxjs-base.js">ajaxjs-base.js</a> 和<a href="ajaxjs-ui.js"> ajaxjs-ui.js</a>，某些大型组件还要额外 js，参见各详细页面的介绍。</li>
				<li>页面复制相关组件的 HTML，然后修改。</li>
				<li>不直接提供代码在文档上了，大家可以 F12 看代码或者右键查看源代码。</li>
			</ul>
			<p>支持浏览 IE10+/Android4+/iOS6+。</p>
			<p><a href="https://gitee.com/sp42/ajaxjs">Git/SVN 源码</a> | <a href="http://blog.csdn.net/zhangxin09">作者博客</a> | QQ 群：<a href="https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">3150067</a> </p>
<!-- 			<img src="head.jpg" width="350" style="margin: 0 auto;display: block;" /> -->
			<p>Apache License Version--2.0. 免费使用、修改，引用请保留头注释。</p>
			
			<p>推荐项目：极致性能的 JS 模板引擎 <a href="https://github.com/jojoin/tppl" target="_blank">TPPL</a>；
			简易异步流程控制器 <a href="https://github.com/creationix/step" target="_blank">Step.js</a>；早期大神 <a href="http://cloudgamer.cnblogs.com/" target="_blank">cloudgamer</a></p>

			
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