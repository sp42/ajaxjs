<%@page import="com.ajaxjs.user.admin.Utils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Aj-SSO</title>
<%@include file="/common/head.jsp"%>
<style type="text/css">
table {
	margin-top: 16%;
}

.panel {
	background-color: #e3e3e3;
	border: 1px solid lightgray;
	width: 4300px;
	border-radius: 6px;
	padding: 5%;
	width: 300px;
	padding: 2%;
}

h1{
	font-weight:bold;
	margin:15px 0;
}
</style>
</head>
<body>
	<table border="0" width="100%">
		<tr>
			<td align="center">
				<div class="panel">
					<%
					if (Utils.isLogined(request)) {
					%>
					<h1>你已登录</h1>
					<br /> <a href="#">注销</a>
					<%
					} else {
					%>
					<h1>登录 SSO 管理系统</h1>
					<form class="form" method="post" action="https://www.ajaxjs.com/sso/user/login">
						<input type="text" name="userID" placeholder="请输入管理员账号" /> 
						<br />
						<input type="password" name="password" placeholder="请输入管理员密码" />

						<button style="display: block;margin:auto;">登录</button>
					</form>
					<%
					}
					%>
				</div>
			</td>
		</tr>

	</table>

</body>
</html>