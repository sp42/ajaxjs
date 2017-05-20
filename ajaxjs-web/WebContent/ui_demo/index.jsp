<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<%request.setAttribute("title", "欢迎！"); %>
		<%@include file="public/common.jsp" %>
		<base href="demo/" />
	</head>
	<body>
		<%@include file="public/nav.jsp" %>
		<h1 class="p">欢迎使用 AJAXJS Framework 的 UI 库！</h1>
		<p class="p">
		A lightweight JS UI library! 它是原生 JS（es v3）、原创（95%），主要有以下 js:</p>
		<ul class="p">
			<li>dom.js 基础库。大小=包含源码 13kb</li>
			<li>widget.js 组件库，依赖 dom.js，大小=包含源码 40kb</li>
			<li>list.js 列表控件，依赖 widget.js，大小=包含源码 33kb</li>
		</ul>
		<p class="p">
		请点击上面导航中的菜单进行浏览：）
		</p>
		
		<br />
		<center class="p">
      		<img src="../head.jpg" width="50%" align="center" />
		</center>
		
		<%@include file="public/footer.jsp" %>
	</body>
</html>