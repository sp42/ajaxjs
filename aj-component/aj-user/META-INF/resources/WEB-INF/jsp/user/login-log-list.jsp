<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
		<script src="${aj_static_resource}/dist/admin/admin.js"></script>
	</head>
	<body>
		<div>
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${uiName}一览</template>
			</aj-admin-header>
		</div>
		
		<script>
			new Vue({el:'body > div'});
		</script>

		<!-- 列表渲染，采用传统后端 MVC 渲染 -->
		<table class="aj-niceTable listTable">
			<colgroup>
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th class="name">用户名称</th>
					<th>登录类型</th>
					<th>登录ip</th>
					<th>ip地区</th>
					<th>客户端标识</th>
					<th>是否登录后台</th>
					<th>登录时间</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="8">
						<aj-form-between-date :is-ajax="false"></aj-form-between-date>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach items="${PageResult}">
					<tr>
						<td>${item.id}</td>
						<td><a href="${ctx}/admin/user/${item.userId}/">${item.userName}</a></td>
						<td>${LoginType[item.loginType]}</td>
						<td>${item.ip}</td>
						<td>${item.ipLocation}</td>
						<td style="width:430px;">${item.userAgent}</td>
						<td>${item.adminLogin ? '是' : '否'}</td>
						<td><c:dateFormatter value="${item.createDate}" /></td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			new Vue({el: '.listTable'});
		</script>
	</body>
</html>

