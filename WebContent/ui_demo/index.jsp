<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%
	request.setAttribute("title", "欢迎！");
%>
<%@include file="public/common.jsp"%>
<base href="demo/" />
</head>
<body>
	<%@include file="public/nav.jsp"%>
	<h4>欢迎使用 AJAXJS Framework 的 UI 库！</h4>
	<p>原生的 JavaScript，请点击上面导航中的菜单进行浏览：）如果你决定使用这个库，本人能打包票这是一个非常明智的选择！</p>

	<br />
	<div class="center" style="text-align: center;">
		<img src="../head.jpg" width="50%" align="center" />
	</div>

	<p>
		AJAXJS UI 鸣谢以下库的支持：1、<a href="https://github.com/jojoin/tppl"
			target="_blank">tppl.js 纳米级模版</a>；2、<a
			href="https://github.com/AlloyTeam/AlloyFinger" target="_blank">AlloyFinger
		</a>。
	</p>

	<%@include file="public/footer.jsp"%>
</body>
</html>