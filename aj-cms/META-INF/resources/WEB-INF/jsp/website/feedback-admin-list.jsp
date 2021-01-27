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
		<div class="vue">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
			</ajaxjs-admin-header>
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
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th class="name">留言者名称</th>
					<th>留言时间</th>
					<th>关联用户</th>
					<th>联系方式</th>
					<th>是否回复</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="8">提示：点击“关联用户”记录可以过滤显示特定的数据。</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach items="${PageResult}">
					<tr>
						<td>${item.id}</td>
						<td title="${item.name}">${item.name}</td>
						<td><c:dateFormatter value="${item.createDate}" /></td>
						<td>	
							<!-- 读取用户标识 -->
							<c:if test="${empty item.userId || item.userId == 0}">
								匿名用户
							</c:if>
							<c:if test="${item.userId > 0}">
								<a href="?filterField=e.userId&filterValue=${item.userId}">
									${empty item.extractData.userName ? item.extractData.userNickName : item.extractData.userName}
								</a>
							</c:if>
						</td>
						<td>${item.contact}</td>
						<td>${empty item.feedback ? '未回复': '已回复'}</td>
						
						<td is="aj-admin-control" id="${item.id}" name="${item.name}">
							<c:if test="${item.userId > 0}">
								<a target="_blank" href="${ctx}/admin/user/${item.userId}/">
									<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />用户详情
								</a>
							</c:if>
						</td>
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