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
			
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th>头像</th>
					<th class="name">${uiName}账号</th>
					<th>用户组</th>
					<th>订单</th>
					<th>购物车</th>
					<th>地址簿</th>
					<th>点赞</th>
					<th>收藏</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="10"></td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td>
							<c:if test="${not empty current.avatar}">
								<img src="${ctx}/${current.avatar}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
							 		onmouseenter="aj.imageEnlarger.singleInstance.imgUrl = '${ctx}/${current.avatar}';" onmouseleave="aj.imageEnlarger.singleInstance.imgUrl = null;" />
							</c:if>
						</td>
						<td>${current.name}</td>
						<td>${UserGroups[current.roleId].name}</td>
						<td><a href="${ctx}/admin/order/list/?userId=${current.id}">查看订单</a></td>
						<td><a href="${ctx}/admin/cart/list/?userId=${current.id}">查看购物车</a></td>
						<td><a href="${ctx}/admin/address/?userId=${current.id}">查看地址簿</a></td>
						<td><a href="${ctx}/admin/like/list/?userId=${current.id}">查看点赞</a></td>
						<td><a href="${ctx}/admin/bookmark/?userId=${current.id}">查看收藏</a></td>
						<td><a href="${ctx}/admin/user/${current.id}/">用户详情</a></td> 
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			aj.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>