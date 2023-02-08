<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%
	String sql = "SELECT "+
			"(SELECT COUNT(id) FROM user_login_log) AS loginTimes,"+
	     	"(SELECT COUNT(id) FROM user) AS userNum,"+
	     	"(SELECT COUNT(id) FROM auth_client_details) AS clientNum,"+
	     	"(SELECT COUNT(id) FROM auth_access_token) AS onlineNum,"+
	     	"(SELECT COUNT(id) FROM sys_tenant) AS tenantNum";

	JspHelper.init(request);
	JspHelper.getOne(request, sql);
	JspHelper.closeConn(request);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<jsp:include page="/common/head.jsp" />
<style>
html, body, body>table {
	height: 100%;
}

.main {
	width: 100%;
	min-height: 600px;
	text-align: left;
}

h1 {
	font-size: 18pt;
	border-bottom: 1px solid lightgray;
	padding: 10px 0;
}

table.tj {
	width: 100%;
	margin: 20px 0;
}
</style>
</head>
<body class="middleHeight">
	<table>
		<tr>
			<td align="center">
				<div class="main">
					<h1>欢迎使用 SSO 管理中心 Welcome</h1>

					<table class="aj-table even tj">

						<tr>
							<th>租户数</th>
							<th>客户端数</th>
							<th>用户数量</th>
							<th>已登录用户数量</th>
							<th>登录次数</th>
						</tr>

						<tr>
							<td>${info.tenantNum}</td>
							<td>${info.clientNum}</td>
							<td>${info.userNum}</td>
							<td>${info.onlineNum}</td>
							<td>${info.loginTimes}</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>


