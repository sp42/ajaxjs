<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<html>
	<commonTag:head title="Welcome to Ajaxjs Web Framework's Homepage">
		<style>
			a {
				color: inherit; /* 继承颜色 */
				transition: color 0.5s ease;
			}
			
			a:hover {
				color: black;
			}
			
			h1 {
				font-size: 2.5rem;
				text-shadow: 2px 5px 6px rgba(0, 0, 0, .5);
				font-weight: normal;
				float: left;
			}
			
			.body {
				max-width: 1000px;
				margin: 0 auto;
			}
			
			nav.body {
				margin-top: 20px;
				overflow: hidden;
			}
			
			.green {
				width: 100%;
				background-color: #147c45;
				height: 444px;
			}
			
			.green .body {
				overflow: hidden;
				background: url(images/index_03.jpg) no-repeat 76% 0;
			}
			
			.green .left {
				width: 45%;
				float: left;
			}
			
			.green .right {
				width: 55%;
				float: left;
				padding-top: 240px;
			}
			
			h2 {
				color: #fff329;
				font-weight: normal;
				text-shadow: 2px 5px 6px rgba(0, 0, 0, .5);
				letter-spacing: 2px;
			}
			
			p {
				line-height: 180%;
				letter-spacing: 2px;
			}
			
			.points li {
				color: white;
				-webkit-text-stroke: 5px rgba(0, 0, 0, .15);
				text-stroke: 5px rgba(0, 0, 0, .15);
				list-style-type: disc;
				margin-left: 30px;
				font-size: .8rem;
				letter-spacing: 2px;
				margin-bottom: 6px;
			}
			
			nav ul {
				float: right;
				width: 500px;
				color: gray;
				margin-top: 18px;
			}
			
			nav ul li {
				float: left;
				width: 16%;
				margin-right: 4%;
				padding-bottom: 1%;
				text-align: center;
				border-bottom: 6px solid;
			}
			
			nav ul li a {
				text-decoration: none;
			}
			
			.cols {
				overflow: hidden;
				margin-top: 5px;
				padding: 1% 0;
				background-image:
					url("data:image/gif;base64,R0lGODlhBAAEAMQBANPT0wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0MzQyLCAyMDEwLzAxLzEwLTE4OjA2OjQzICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjlBMENCMzdFQzYzMDExRTU4Njg1RUM5MzhFNDBENkU4IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjlBMENCMzdGQzYzMDExRTU4Njg1RUM5MzhFNDBENkU4Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OUEwQ0IzN0NDNjMwMTFFNTg2ODVFQzkzOEU0MEQ2RTgiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6OUEwQ0IzN0RDNjMwMTFFNTg2ODVFQzkzOEU0MEQ2RTgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAABAAEAAAFCGAgAmMZkGIIADs=");
			}
			
			.cols li {
				float: left;
				width: 33.3%;
				border-right: 5px solid;
				box-sizing: border-box;
				padding: 0.5% 2% 2% 2%;
			}
			
			.cols li p {
				font-size: .9rem;
				letter-spacing: 0px;
				color: #333;
			}
			
			h5 {
				font-size: 1.2rem;
				vertical-align: middle;
			}
			
			footer {
				font-size: .9rem;
				height: 122px;
				text-align: center;
				margin-top: 3px;
				padding-top: 30px;
				color: gray;
				background: repeat-x
					url("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAB6AAQDASIAAhEBAxEB/8QAFwABAQEBAAAAAAAAAAAAAAAAAAMCCP/EABYQAQEBAAAAAAAAAAAAAAAAAAASEf/EABQBAQAAAAAAAAAAAAAAAAAAAAD/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwDogADBuQFcFZAWkWkBSRTAAAAAAAH/2Q==");
			}
			
			h5 img {
				height: 30px;
			}
			
			@media screen and (max-width:480px) {
				/*手机端浏览器所显示的网页CSS*/
				.body {
					max-width: 330px;
				}
				nav.body {
					margin-top: 10px;
				}
				h1 {
					padding-left: 2%;
					padding-bottom: 2%;
					font-size: 2rem;
				}
				h2 {
					display: none;
				}
				nav ul {
					float: none;
					overflow: hidden;
					margin: 0 auto;
					margin-left: 0%;
					width: 100%;
				}
				nav ul li {
					width: 20%;
					margin-right: 0%;
					letter-spacing: 0;
				}
				.green {
					width: 100%;
					background-color: #147c45;
					height: auto;
					padding: 2% 1%;
					box-sizing: border-box;
				}
				.green .body {
					overflow: hidden;
					background: none;
				}
				.green .left {
					width: 100%;
					float: none;
					text-indent: 0;
				}
				.green .left p {
					text-indent: 2em;
				}
				.green .right {
					width: 100%;
					float: none;
					padding-top: 3%;
				}
				.cols {
					max-width: 100%;
				}
				.cols li {
					width: 100%;
					float: none;
				}
				footer {
					height: 60px;
				}
			}
		</style>
		<meta name="keywords" content="ajaxjs">
		<meta name="description" content="">
	</commonTag:head>

<!-- 	<img style="width:60%;max-width: 400px;margin: 0 auto;margin-top:50px;display: block;" src="asset/images/under_construction.jpg" /> -->
<%-- 	<%=new com.ajaxjs.web.config.InitConfig()%> --%>
<body>
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
				<h2 style="margin-bottom: 5%; margin-left: 35%;">Java 快速开发框架</h2>
				<p style="color: #fffbac;">AJAXJS Web 是一款基于 Java 平台的开源 Web
					框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势，
					但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网小型网站的快速应用。</p>
				<div
					style="text-align: center; color: #fffbac; font-weight: normal; letter-spacing: 2px;">
					<br> <img src="images/index_06.jpg"> <br>
					<h3>Open Source 开放源代码</h3>
					<h4>
						<a href="http://git.oschina.net/sp42/ajaxjs">Git/SVN Checkout</a>
					</h4>
				</div>
			</div>
			<div class="right">
				<div class="points" style="margin-bottom: 10px; width: 95%;">
					<br>
					<ul>
						<li>标配 IOC/AOP/MVC/ORM/DAO/RESTful</li>
						<li>参照 Spring MVC 风格的 Web 框架，功能简单但是 MVC 的核心功能基本具备了，很适合想了解 MVC
							的学习者。尽量多提供源码注释和文档，包括单元测试</li>
						<li>快速 CRUD 开发，基于 Controller/Service/DAO/Model 进行泛型封装，类似 MyBatis 注解 的 SQL 服务，支持 Java Bean 或更简单的 Map，自动分页，查询条件和验证。</li>
						<li>全栈，不仅仅是服务端框架，还整合了前端库。得益于现代浏览器成熟 API 甚至没有用 jQuery</li>
					</ul>
					<br> <br>
				</div>
			</div>
		</div>
	</div>
	<ul class="body cols">
		<li style="border-right-color: #ff8820;">
			<h5>
				<img src="images/icon_source.png">设计原则 Design Principles
			</h5>
			<p>快速、高效，轻量级维护，提倡精益开发；遵循“够用就好、就地取材”原则，优先考虑 JDK 原生 API，基本没依赖其他重型三方库。简单易学易用。Clean, Simple, Fast & Lightweight.</p>
		</li>
		<li style="border-right-color: #2096ff;">
			<h5>
				<img src="images/icon_inkboard.png"> 依赖选型 Foundation
			</h5>
			<p>表示层：JSP/Servlet/MVC/JSTL/Tag
				Files/Less.js；数据层：MySQL/SQLite/MyBatis/Shrio/Apache BVal/Tomcat
				JDBCPool；基础服务：Rhino/JavaUtilLogger</p>
		</li>
		<li style="border-right-color: #c720ff;">
			<h5>
				<img src="images/icon_database.png">下载 &amp; 运行 Download
			</h5>
			<p>Win/OS X/CentOS，JRE 1.7+/Tomcat 7</p>
			<p>
				<a href="http://git.oschina.net/sp42/ajaxjs">源码 Git/SVN</a> <a
					href="javadoc">JavaDoc 文档</a> <a
					href="http://framework.ajaxjs.com/framework/docs/docs.html">手册</a>
			</p>
			<p>
				QQ 群：3150067 <a href="http://blog.csdn.net/zhangxin09">作者博客</a>
			<p>基于 Apache License Version 2.0 开源协议</p>
		</li>
	</ul>
	<footer>
		<div class="body">粤 ICP 证 09002462 号 | Copyright © 1999-2016,
			Sp42</div>
	</footer>
	<!-- CNZZ 统计 -->
		<script src="https://s11.cnzz.com/z_stat.php?id=1261173351&web_id=1261173351"></script>
	<!-- // CNZZ 统计 -->
</body>
</html>
