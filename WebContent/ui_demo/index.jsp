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
		<h4>欢迎使用 AJAXJS Framework 的 UI 库！</h4>
		<p>
		A lightweight JS UI library! 它是原生 JS（es v3）。主要有以下 js:</p>
		<ul class="center list">
			<li>dom.js 基础库。大小=13kb（未压缩、包含注释）</li>
			<li>widget.js 组件库，依赖 dom.js，大小=40kb（未压缩、包含注释）</li>
			<li>list.js 列表控件，依赖 widget.js，大小=33kb（未压缩、包含注释）</li>
		</ul>
		<p >
		请点击上面导航中的菜单进行浏览：）
		</p>
		
		<br />
		<div class="center" style="text-align:center;">
      		<img src="../head.jpg" width="50%" align="center" />
		</div>
		
		<p>AJAXJS UI 鸣谢以下库的支持：1、<a href="https://github.com/jojoin/tppl" target="_blank">tppl.js 纳米级模版</a>；2、<a href="https://github.com/AlloyTeam/AlloyFinger" target="_blank">AlloyFinger
		</a>。</p>
		
		<%@include file="public/footer.jsp" %>
	</body>
</html>