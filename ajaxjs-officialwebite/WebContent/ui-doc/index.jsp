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
	
	<script src="js/ajaxjs-base.js"></script>
	<script src="js/ajaxjs-ui.js"></script>
	<script src="js/ajaxjs-list.js"></script>
	<script src="${pageContext.request.contextPath}/asset/common/js/libs/run_prettify.js"></script>  

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
					<a href="?g=tags">List w. Thumb</a>
				</li>
				<li>
					<a href="?g=tags">Tags</a>
				</li>
				<li>
					<a href="?g=marquee">Marquee</a>
				</li>
			</ul>
		</menu>
		
		<section>
	<%if("msgbox".equals(request.getParameter("g"))) { %>		
			<%@include file="include/msgbox.jsp" %>
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
	<%} else if("tags".equals(request.getParameter("g"))) { %>		
			<%@include file="include/tags.jsp" %>
	<%} else if("list".equals(request.getParameter("g"))) { %>		
			<%@include file="include/list.jsp" %>
	<%} else if("marquee".equals(request.getParameter("g"))) { %>		
			<%@include file="include/marquee.jsp" %>
	<%} else { %>	
			<h4>Welcome!</h4>
			<p>&nbsp;&nbsp;&nbsp;&nbsp;AJAXJS Framework 旨在打造一个极简风格的、全栈的、简单明了的  Web 框架——此 UI 库正是该框架的前端组件库部分。
			关于后台框架更多信息，可访问 <a href="https://framework.ajaxjs.com">AJAXJS Framework</a> 主页。</p>
			<ul class="list">
				<li>AJAXJS UI = vue.js + less.js + 原生组件</li>
				<li>大胆拥抱 MVVM + H5 标准，无须复杂的配置，“代码是怎样就怎样”。</li>
				<li>但 JavaScript 则保守得多，很多认知仍停留在 ECMAScript 3 的时代上。且简单起见，能够用前面两者（HTML/CSS）解决的，就不用 JS。</li>
				<li>代码务求简明爽快直观，不添加所谓“奇技淫巧”，追求用最小行数代码实现。</li>
				<li>CSS 的代码管理是头痛问题，为此，我们使用预编译器 <a href="https://less.bootcss.com/">LESS.js</a> 来改进原生落后的管理方法。</li>
				<li>作者本业是搞 Java Web 的，故提供了  Tag Files 或 SimpleTag 的标签库。</li>
				<li>更多特性简介<a href="http://blog.csdn.net/zhangxin09/article/details/79508450">参见博文</a>和浏览我 CSDN 博客了解动态。</li>
			</ul>
			<p>用法简介如下：</p>
			<ul class="list">
				<li>定义一空白 LESS 文件，导入所需的包。然后在这 LESS 文件定义或覆盖样式。</li>
				<li>引入 js <a href="ajaxjs-base.js">ajaxjs-base.js</a> 和<a href="ajaxjs-ui.js"> ajaxjs-ui.js</a>，某些大型组件还要额外 js，参见各详细页面的介绍。</li>
				<li>页面复制相关组件的 HTML，然后修改。</li>
				<li>不直接提供代码在文档上了，大家可以 F12 看代码或者右键查看源代码。</li>
			</ul>
			<p>支持浏览 IE8+（打补丁） Android4+ iOS6+。</p>
			<p><a href="https://gitee.com/sp42/ajaxjs">Git/SVN 源码</a> | <a href="http://blog.csdn.net/zhangxin09">作者博客</a> | QQ 群：<a href="https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">3150067</a> </p>
<!-- 			<img src="head.jpg" width="350" style="margin: 0 auto;display: block;" /> -->
			<p>Apache License Version--2.0. 免费使用、修改，引用请保留头注释。</p>
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