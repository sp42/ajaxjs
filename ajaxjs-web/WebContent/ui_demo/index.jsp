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
		<p class="p">请点击上面导航中的菜单进行浏览：）
		
		<br />
		<center class="p">
      <img src="../head.jpg" width="60%" align="center" />
		</center>
		</p>
		
		<%@include file="public/footer.jsp" %>
	</body>
</html>