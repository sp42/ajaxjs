<%@page pageEncoding="UTF-8" isErrorPage="true" import="java.io.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=320,user-scalable=0,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0" />
<title>404 Not found</title>
<style>
td {
	font-size: 9pt;
	padding: 6px;
}

p {
	padding-left: 30px;
}

hr {
	width: 98%;
}

textarea {
	width: 90%;
	min-height: 600px;
	outline: none;
	border: 1px solid gray;
	padding: 1%;
	margin: 1%;
}
</style>
</head>
<body leftMargin="0" topMargin="0">
	<table cellSpacing="0" cellPadding="0" border="0" width="100%">
		<tr>
			<td bgColor="#ff6600" rowSpan="2">
				<table cellPadding="12">
					<tr>
						<td width="100%"><B> <FONT color="#ffffff" size="4">找不到你请求-HTTP
									StatusCode:404</FONT>
						</B></td>
					</tr>
				</table>
			</td>
			<td width="32" bgColor="#ff6600" height="32"></td>
			<td width="32" bgColor="#ff9933" height="32"></td>
			<td width="32" bgColor="#ffcc66" height="32"></td>
			<td width="32" bgColor="#ffee99" height="32"></td>
		</tr>
		<tr>
			<td width="32" bgColor="#ff9933" height="32"></td>
			<td width="32" bgColor="#ffcc66" height="32"></td>
			<td width="32" bgColor="#ffee99" height="32"></td>
			<td width="32" height="32"></td>
		</tr>
	</table>
	<%
		String uri = request.getAttribute("javax.servlet.error.request_uri").toString(),
				fUri = request.getAttribute("javax.servlet.forward.request_uri").toString();
		if (uri.equals(fUri)) {
	%>
	<p>
		404 Not found:<%=uri%><br />亲，你的网址是不是打错了哦？
	</p>
	<%
		} else {
	%>
	<p>
		MVC 绑定 jsp 模版的路径错误！模版:<%=fUri%>不存在；请求路径：<%=uri%>
	</p>
	<%} %>
	<hr />
	<div align="center">
		<a href="${pageContext.request.contextPath}">回首页</a> | <a
			href="javascript:history.go(-1);">上一页</a>
	</div>

</body>
</html>