<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="vue">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
			
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :no-catalog="true">
				<aj-tree-user-role-select :value="${empty param.roleId ? '0' : param.roleId}" :json="${UserGroupsJSON}">
				</aj-tree-user-role-select>
			</aj-admin-filter-panel>
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
					<th>头像</th>
					<th class="name">${uiName}账号</th>
					<th>用户姓名</th>
					<th>性别</th>
					<th>邮件/手机</th>
					<th>注册日期</th>
					<th>用户组</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="9">
						<aj-form-betweenDate></aj-form-betweenDate>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach items="${PageResult}">
					<tr>
						<td>${item.id}</td>
						<td><tags:common type="thumb" thumb="${item.avatar}" /></td>
						<td>${item.name}</td>
						<td>${item.username}</td>
						<td>${SexGender[item.sex]}</td>
						<td>${item.email}<br />${item.phone}</td>
						<td><c:dateFormatter value="${item.createDate}" /></td>
						<td>${UserGroups[item.roleId].name}</td>
						<td is="aj-admin-control" id="${item.id}" name="${item.name}"></td>		
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			new Vue({el: '.listTable'});
			aj.widget.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>