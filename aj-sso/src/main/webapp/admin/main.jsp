<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>

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
							<td>5</td>
							<td>6</td>
							<td>999</td>
							<td>55</td>
							<td>55</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>


