<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content bodyClass="home" bannerText="Hello, World!" bannerImg="../images/home-1-748x528.png">
	<jsp:attribute name="left">
		<div class="baiduSearch">
			<aj-baidu-search></aj-baidu-search>
		</div>
		<script>
			new Vue({
				el : '.baiduSearch'
			});
		</script>
		<div style="height: 400px;display: flex; align-items:flex-end;">
			<img src="../images/home2.png" width="230" />
		</div>
	</jsp:attribute>
	<jsp:attribute name="body">
		<article>
			<h2>欢迎 Welcome!</h2>
			<p>&nbsp;&nbsp;&nbsp;&nbsp;AJAXJS Framework 旨在打造一个极简风格的、全栈的、简单明了的  Web 框架——此 UI 库正是该框架的前端组件库部分。AJAXJS UI = vue.js + less.js +
			 原生组件。而关于后台框架更多信息，可访问 <a href="https://framework.ajaxjs.com">AJAXJS Framework</a> 主页。</p>
	
			<p>特点简介：</p>
			<ul class="list">
				<li>鼓励绑定式语法，声明式编程， UI 还是推荐多使用标签，这点和 Vue.js 不谋而合。</li>
				<li>组件化封装，灵活使用 mixins，避免冗余逻辑</li>
				<li>没有复杂的配置，避免陷入前端复杂的工具深渊，仅仅通过 &lt;script src="vue.js"&gt; 使用</li>
				<li>代码简洁清爽，思路清晰，逻辑简单</li>
			</ul>
			<p><a href="https://gitee.com/sp42_admin/ajaxjs">Git/SVN 源码</a>，前端源码位于 /asset/ajaxjsui 下 | <a href="http://blog.csdn.net/zhangxin09">作者博客</a> | QQ 群：<a href="https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22">3150067</a> </p>
	<!-- 			<img src="head.jpg" width="350" style="margin: 0 auto;display: block;" /> -->
			<p>Apache License Version--2.0. 免费使用、修改，引用请保留头注释。</p>
			<img src="../images/home1.png" width="830" />
			<h2>最新产品</h2>
		</article>
	</jsp:attribute>
</tags:content>

