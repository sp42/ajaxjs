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

