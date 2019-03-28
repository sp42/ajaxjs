<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div>
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/article/'">新建</a> | </template>
			</ajaxjs-admin-header>
		</div>
		
		<script>
			new Vue({el:'body > div'});
		</script>

		<!-- 列表渲染，采用传统后端 MVC 渲染 -->
		<table class="ajaxjs-niceTable listTable">
			<colgroup>
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
					<th class="name">${uiName}名称</th>
					<th>性别</th>
					<th>头像</th>
					<th>邮件</th>
					<th>手机</th>
					<th>创建时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="8">
						<a href="../downloadXSL/">下载 Excel</a>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td>${current.name}</td>
						<td>${empty current.sex ? '未知' : (current.sex == 1 ? '男' : '女')} </td>
						<td>${current.avatar}</td>
						<td>${current.email}</td>
						<td>${current.phone}</td>
						<td><c:dateFormatter value="${current.createDate}" /></td>
						<td><a href="../${current.id}/">详情</a>|<a href="../login_log_list/?filterField=userId&filterValue=${current.id}">登录信息</a></td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
	</body>
</html>
