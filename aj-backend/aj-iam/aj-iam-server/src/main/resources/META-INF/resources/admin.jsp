<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>AJ-IAM 管理后台</title>
		<%@ include file="pages/common.jsp" %>
		<style>
		html,
        body {
            height: 100%;
            overflow: hidden;
        }
		</style>
	</head>

	<body>
		<menu>
			<h3 style="margin:20px 0 0 13px;color:#2f518c">AJ-IAM 管理后台</h3>
			<br />
			<ul>
				<li>
					<a href="pages/welcome.jsp" target="iframe">首页</a>
				</li>
				<li>
					<a href="pages/app.jsp" target="iframe">应用管理</a>
				</li>
				<li>
					<a href="pages/object-type.jsp" target="iframe">对象类型管理</a>
				</li>
				<li>
					<a href="pages/seat-objectType.jsp" target="iframe">席位与对象类型关系</a>
				</li>
				<li>
					<a href="pages/menu-group.jsp" target="iframe">配置菜单与编组</a>
				</li>
				<li>
					<a href="pages/user.jsp" target="iframe">用户管理</a>
				</li>
				<li>
				    <a href="#" onclick="localStorage.removeItem('accessToken');localStorage.removeItem('userInfo');location.assign('.')">退出</a>
				</li>
			</ul>
		</menu>
		<div class="container">
		    <iframe src="pages/welcome.jsp" name="iframe"></iframe>
		</div>
	</body>
</html>