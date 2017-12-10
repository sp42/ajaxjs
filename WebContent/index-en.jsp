<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<html>
	<commonTag:head title="Welcome to Ajaxjs Web Framework's Homepage">
	    <meta name="keywords"    content="ajaxjs java js javascript web ui framework html5 ioc aop orm restful" />
	    <meta name="description" content="AJAXJS Web 是一款基于 Java 平台的开源 Web 框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势， 但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网小型网站的快速应用。" />
	   
		<link rel="stylesheet" type="text/css" href="asset/css.css" />
        <style>.green{height:480px;}</style>
	</commonTag:head>

<!-- 	<img style="width:60%;max-width: 400px;margin: 0 auto;margin-top:50px;display: block;" src="asset/images/under_construction.jpg" /> -->
<%-- 	<%=new com.ajaxjs.web.config.InitConfig()%> --%>
<body>
    <div class="top"></div>
	<nav class="body">
		<h1>AJAXJS Web Framework</h1>
		<ul>
			<li style="border-bottom-color: #ffed20;"><a href="#">Home</a></li>
			<li style="border-bottom-color: #2096ff;"><a
				href="http://ajaxjs.mydoc.io/">Manual</a></li>
			<li style="border-bottom-color: #ff8820;"><a href="ui_demo">UI</a></li>
			<li style="border-bottom-color: #c720ff;"><a
				href="http://blog.csdn.net/zhangxin09">Blog</a></li>
			<li style="border-bottom-color: #ff0024;"><a href="javadoc">Document</a></li>
		</ul>
	</nav>
	<div class="green">
		<div class="body">
			<div class="left">
				<h2 style="margin-top: 6%; margin-bottom: 2%;">A Full Stack Web
					Framework</h2> 
				<p style="color: #fffbac;">AJAXJS Web aims to full-stack, not only the server-side framework, but also integrates the front-end library. It’s written in HTML5 + Java,
				 a successor to the JVM platform, efficient, secure, stable, cross-platform and many other advantages, but it abandoned the traditional enterprise architecture brought about by the large and bloated, emphasizing the lightweight, and fast, very suitable for the Internet fast application.</p>
				<div style="text-align: center; color: #fffbac; font-weight: normal; letter-spacing: 2px;">
					<img src="images/index_06.jpg"> <br>
					<h3>Apache License v2 OpenSource</h3>
					<h4>
						<a href="http://git.oschina.net/sp42/ajaxjs">Git/SVN@OSC Checkout</a>
					</h4>
				</div>
			</div>
			<div class="right">
				<div class="points" style="margin-bottom: 10px; width: 98%;">
					<br>
					<ul>
						<li>Design principles: Clean, Simple, Fast & Lightweight. Features: full stack, native, comprehensive, practical.</li>
						<li>Follow the "enough to use, suitable for teaching" thought, give priority to native API, basically don't rely on other 3rd-party library. Thanks to modern browser, the front end doesn't use jQuery, but the native API calls, more efficient and fast.</li>
						<li>With the style of SpringMVC, the code is simple but the core functions of MVC are basically available, it's suitable for those who want to understand MVC. Providing code comment, unit test and documentation as much as possible.</li> 
					</ul>
					<br> <br> 
				</div>
			</div>
		</div>
	</div>
	<ul class="body cols">
		<li style="border-right-color: #ff8820;">
			<h5>
				<img src="images/icon_inkboard.png"> About FrontEnd
			</h5>
			<p>
			
			Collecting many years of various HTML/DHTML/CSS best practices, 
			providing many widgets, including responsive CSS/LESS.js for mobile. 
			  Small size, 13kb for all base and widgets <a href="ui_demo">»HTML/CSS/JS Demo</a>  
			</p>
		</li>
		<li style="border-right-color: #2096ff;">
			<h5>
				<img src="images/icon_source.png"> About BackEnd
			</h5>
			<p>Using IOC/AOP/MVC/ORM/RESTful,
Generic Controller/Service/DAO/Model, SpringMVC-like Controller, MyBatis-like annotation SQL service, supports Java Bean or Map as entity, auto-paging, auto-query and validtion<a
					href="javadoc">»JavaDoc</a></p>
		</li>
		<li style="border-right-color: #c720ff;">
			<h5>
				<img src="images/icon_database.png">Download &amp; Run
			</h5>
			<p>Win/Mac/Linux/JRE1.7+/Tomcat7+/IE8+</p>
			<p>
				<a href="http://git.oschina.net/sp42/ajaxjs">Source on Git/SVN</a> <a
					href="javadoc">JavaDoc</a> <a
					href="http://framework.ajaxjs.com/framework/docs/docs.html">Manual</a>
			</p>
			<p>
				QQ Group：3150067 <a href="mailto:support@ajaxjs.com">Email</a> <a href="http://blog.csdn.net/zhangxin09">Author's blog</a>
				<p>Free to use, modify.</p>
			<p> Apache License Version--2.0</p>
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
