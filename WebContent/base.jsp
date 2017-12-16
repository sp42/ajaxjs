<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AJAXJS-Base</title>
<link rel="icon" type="image/x-icon"
	href="https://framework.ajaxjs.com/framework/asset/images/favicon.ico" />
<link rel="shortcut icon" type="image/x-icon"
	href="https://framework.ajaxjs.com/framework/asset/images/favicon.ico" />
<meta name="keywords" content="ajaxjs base java ioc aop orm " />
<meta name="description"
	content="AJAXJS-Base 基础模块包，为纯 Java 项目与特定 jvm 环境脱离，面向 Web 开发，但可运行在 web、swing、android 环境中。" />

<style>
.top {
	width: 100%;
	background-color: white;
	height: 300px;
	border-bottom: 1px solid lightgray;
}

.bottom {
	width: 1000px;
	height: 400px;
	border: 1px solid lightgray;
	border-top: 0;
}

.box2 {
	width: 1000px;
	height: 500px;
	border: 1px solid lightgray;
	border-radius: 4px;
	background-color: #f7f7f9;
}

fieldset {
	width: 1000px;
	position: relative;
	height: 500px;
	border: 1px solid rgba(255, 255, 255, .5);
	border-radius: 5px;
	text-align: left;
}

legend {
	font-size: 2rem;
	color: white;
	margin-left: 2%;
}

fieldset div.box {
	position: absolute;
	top: 3%;
	right: -10%;
	/* 	background-color: rgba(255, 255, 255, .1); */
	background-image: linear-gradient(90deg, rgba(255, 255, 255, .1) 0,
		rgba(89, 195, 232, .9) 100%);
	height: 92%;
	width: 95%;
	color: white;
	padding: 3% 10% 4% 4%;
	box-sizing: border-box;
}

body {
	background-image: linear-gradient(45deg, #1832a2 0, #4ae9f2 100%);
}

legend span {
	font-size: 1.5rem;
}

h1 {
	margin-top: 0;
}

.links {
	position: absolute;
	bottom: -10%;
	left: 20%;
	color: white;
	opacity: .5;
	width: 60%;
	letter-spacing: 2px;
}

a {
	color: white;
}

ul.section {
	position: absolute;
	right: -9%;
	top: 68%;
	text-align: left;
	list-style-type: none;
	letter-spacing: 2px;
}
</style>
</head>
<body>
	<%
		String show = request.getParameter("show");
		if (show == null)
			show = "";
	%>
	<table width="100%" height="100%">
		<tr>
			<Td align="center" valign="middle">
				<fieldset>
					<legend>
						AJAXJS-Base, <span>a pure Java library</span>
					</legend>

					<div class="box">

						<%
							if (show.equals("")) {
						%>

						<h1>
							<a id="ajaxjs-framework-base-基础库" class="anchor"
								href="#ajaxjs-framework-base-%E5%9F%BA%E7%A1%80%E5%BA%93"></a>AJAXJS
							Framework Base 基础库
						</h1>
						<p>这是一个平淡无奇的库，相信不会为绝大多数大神们带来一点惊喜——只是自己业余累积的一些代码库（“轮子”），希望可以通过简单的手段做一件事（没啥三方依赖），不过可能就是考虑的情况不是很足，方法不是最主流的，性能也不是最优的。</p>
						<p>基础模块包 ajaxjs-base 为纯 Java 项目与特定 jvm 环境脱离，面向 Web 开发，但可运行在
							web、swing、android 环境中。base 包含了相当多的工具类或静态方法，有以下子模块：</p>

						<ul>
							<li><a href="?show=utils">Utils</a></li>
							<li><a href="?show=jdbc">JDBC</a></li>
							<li><a href="?show=js">JS/JSON</a></li>
							<li><a href="?show=net">Net</a></li>
						</ul>
						<p>运行系统：Win/Mac/Linux；要求：JRE1.7+/Tomcat7+</p>
						<p>
							<a href="https://gitee.com/sp42/ajaxjs-base">Git/SVN 源码</a> | <a
								href="">JavaDoc</a> | <a href="http://ajaxjs.mydoc.io/">手册</a> |
							<a href="http://blog.csdn.net/zhangxin09">博客</a> | <a
								target="_blank"
								href="//shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">QQ
								群：315006</a> | <a href="mailto:support@ajaxjs.com">联系邮箱</a>
						<p>基于 Apache License Ver2.0 开源协议，免费使用、修改，引用请保留头注释</p>
						<p>
							关联项目： <a href="https://framework.ajaxjs.com">AJAXJS-Web</a> | <a
								href="">AJAXJS-CMS</a>
						</p>

						<%
							}
							if (show.equals("utils")) {
						%>

						<h2>
							<a id="util" class="anchor" href="#util"></a>Utils
						</h2>
						<p>常规工具包，有字符串工具类、常用日期处理类、值处理工具类等，还有该模块包含以下子模块：</p>
						<ul class="task-list">
							<li>logger，基于 java.util.logger 封装的日志</li>
							<li>collection，集合工具类，map 的工具方法，还有 map/list 的遍历器</li>
							<li>io，处理流 Stream 的包，使用了链式调用的风格。有文件、流工具类和简易图片处理器；</li>
							<li>reflect，反射工具包，进行反射操作封装；</li>
							<li>ioc，一个简易的 ioc 依赖注射实现；/aop，一个简易的 aop 实现；</li>
						</ul>
						<p>
							<a href="http://ajaxjs.mydoc.io/?t=207309">&gt;&gt;详细用法参见手册</a>。
						</p>
						<%
							}
							if (show.equals("jdbc")) {
						%>
						<h2>
							<a id="jdbc" class="anchor" href="#jdbc"></a>jdbc
						</h2>
						<p>围绕数据库而设的一些工具包，大体上分为两种模块，一种是简单的增删改查函数，基于 JDBC 浅浅的封装；另外一种是基于
							ORM 思想的方案，类似于 MyBatis 方式。</p>
						<p>
							<a href="http://ajaxjs.mydoc.io/?t=145194">&gt;&gt;详细用法参见手册</a>。
						</p>

						<h2>
							<a id="framework" class="anchor" href="#framework"></a>framework
						</h2>
						<p>数据业务层框架，提供泛型封装 Service/DAO/Model 提供快速增删改查服务，包含分页，查询条件，整合
							Validator 后端验证。该模块包含以下子模块：</p>
						<ul class="task-list">
							<li>dao，Data Access Object 数据访问对象</li>
							<li>model，数据模型</li>
							<li>service，业务服务层</li>
						</ul>
						<p>
							<a href="http://ajaxjs.mydoc.io/?t=203095">&gt;&gt;详细用法参见手册</a>。
						</p>
						<%
							}
							if (show.equals("js")) {
						%>
						<h2>
							<a id="js" class="anchor" href="#js"></a>js
						</h2>
						<p>小型 JSON 解析器，实现 JSON 与 Map/List 互换，是了解 JSON 解析的好例子。</p>
						<p>
							<a href="http://ajaxjs.mydoc.io/?t=208700">&gt;&gt;详细用法参见文档</a>。
						</p>
						<h2>
							<a id="config" class="anchor" href="#config"></a>config
						</h2>
						<p>小巧、灵活、通用、基于 JSON 的配置系统，完全可以代替 properties 文件实现配置模块。</p>
						<p>
							<a href="http://ajaxjs.mydoc.io/?t=208700">&gt;&gt;详细用法参见文档</a>。
						</p>
						<%
							}
							if (show.equals("net")) {
						%>
						<h2>
							<a id="net" class="anchor" href="#net"></a>net
						</h2>
						<p>网络通讯类。该模块包含以下子模块：</p>
						<ul class="task-list">
							<li>http 可发送 GET/POST/PUT/DELETE 请求。远没有 Apache HttpClient
								完善，但足以满足一般的请求；</li>
							<li>mail 无须 JavaMail 发送邮件。原理是通过最简单的 telnet 发送；</li>
							<li>ip IP 工具类</li>
						</ul>
						<p>
							<a href="http://ajaxjs.mydoc.io/?t=203095">&gt;&gt;详细用法参见手册</a>。
						</p>

						<%
							}
							if (show.equals("copyright")) {
						%>
						<h1>
							<a id="版权声明-license" class="anchor"
								href="#%E7%89%88%E6%9D%83%E5%A3%B0%E6%98%8E-license"></a>版权声明
							LICENSE
						</h1>
						<pre style="overflow: hidden;">/**
 * 版权所有  Frank Cheung frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */

</pre>
						<%
							}
						%>
					</div>

					<div class="links">©1999-2018 Sp42，粤 ICP 证 09002462 号</div>

					<ul class="section">
						<li><a href="?">Intro</a></li>
						<li><a href="?show=utils">Utils</a></li>
						<li><a href="?show=jdbc">JDBC</a></li>
						<li><a href="?show=js">JS/JSON</a></li>
						<li><a href="?show=net">Net</a></li>
						<li><a href="?show=copyright">开源许可</a></li>
					</ul>
				</fieldset>

			</Td>
		</tr>
	</table>
	<!-- CNZZ 统计 -->
	<div style="display: none";>
		<script
			src="https://s11.cnzz.com/z_stat.php?id=1261173351&web_id=1261173351"></script>
	</div>
	<!-- // CNZZ 统计 -->
</body>
</html>