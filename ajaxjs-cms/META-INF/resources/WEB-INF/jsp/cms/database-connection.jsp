<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="数据库连接典管理" />
	</jsp:include>
	<style>
		form {
			padding: 1% 2%;
		}

		.panel{
			padding:2% 5%;
			margin:0 auto;
		}
		
		li{
			border-bottom:1px solid lightgray;
			margin:2% 0;
		}

	</style>
</head>
<body>
	<div>
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">数据库连接管理</template>
		</ajaxjs-admin-header>
	</div>
	<script>
		new Vue({el:' body > div'});
	</script>
		
	<div class="panel">
		当前数据库连接：
		<select class="ajaxjs-select" style="width: 200px;">
			<c:foreach items="${list}" var="current">
				<c:choose>
					<c:when test="${param.filterValue == current.id}">
						<option value="${current.name}" selected>${current.name}</option>
					</c:when>
					<c:otherwise>
						<option value="${current.name}">${current.name}</option>
					</c:otherwise>
				</c:choose>
			</c:foreach>
		</select>
		<button class="ajaxjs-btn">保存</button>
		<ul>
			<c:foreach items="${list}" var="item">
				<li>
					<form>
					<label>名称：<input type="text" class="ajaxjs-inputField" value="${item.name}" /></label>	
					<label>账号：<input type="text" class="ajaxjs-inputField" value="${item.username}" /></label>	
					<label>密码：<input type="text" class="ajaxjs-inputField" value="${item.password}" /></label>	
					<br />
					<br />
					<label>URL：<input type="text" class="ajaxjs-inputField" value="${item.url}" style="width:600px;" /></label>	
					</form>
				</li>
			</c:foreach>
		</ul>
		
		<div>
			Notes:数据库 url 注意相关字符串要转义，如：&amp;
		</div>
	</div></body>
</html>
