<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/jsp/common/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="vue">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
			</ajaxjs-admin-header>
			
			<!-- 搜索 -->
			<aj-admin-filter-panel :no-catelog="true"></aj-admin-filter-panel>
		</div>
		
		<script>
			new Vue({el:'.vue'});
		</script>
	<table class="ajaxjs-niceTable" align="center" width="90%">
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
				<th class="name" width="500">${uiName}名称</th>
				<th>联系电话</th>
				<th>联系邮箱</th>
				<th>是否已回复</th>
				<th>留言日期</th>
				<th>控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="7"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult}">
				<tr>
					<td>${current.id}</td>
					<td>${current.name}</td>
					<td>${current.phone}</td>
					<td>${current.email}</td>
					<td>${empty current.feedback ? '否' : '是'}</td>
					<td><c:dateFormatter value="${current.createDate}" format="yyyy-MM-dd" /></td>
					<td>
						<a href="javascript:aj.admin.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
						<a href="${ctx}/admin/${tableName}/${current.id}/"><img src="${commonAssetIcon}/update.gif" style="vertical-align: sub;" />编辑</a>
					</td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
		<div style="text-align:center;margin:2%;">
			<%@include file="/asset/common/jsp/pager.jsp" %>
		</div>
	</body>
</html>
