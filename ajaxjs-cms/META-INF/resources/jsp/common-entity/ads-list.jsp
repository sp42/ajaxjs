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
				<template slot="btns"><a :href="ajResources.ctx + '/admin/ads/'">新建</a> | </template>
			</ajaxjs-admin-header>
	
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :catelog-id="${domainCatalog_Id}" :selected-catelog-id="${empty param.catalogId ? 'null' : param.catalogId}"></aj-admin-filter-panel>
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
					<th class="name">${uiName}标题</th>
					<th>广告类型</th>
					<th>广告链接</th>
					<th>创建时间</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="6"></td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td>${current.name}</td>
						<td>${current.catelogName}</td>
						<td>
							<a href="${current.link}" target="_blank">${current.link}</a>
						</td>
						<td>
							<c:dateFormatter value="${current.createDate}" format="yyyy-MM-dd" />
						</td>
						<td>
							<a href="../../../${tableName}/${current.id}/" target="_blank">浏览</a>
							<a href="${ctx}/admin/ads/${current.id}/"><img src="${commonAsset}/icon/update.gif" style="vertical-align: sub;" />编辑</a>
							<a href="javascript:aj.admin.del('${current.id}', '${current.name}');"><img src="${commonAsset}/icon/delete.gif" style="vertical-align: sub;" />删除</a>
						</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/asset/common/jsp/pager.jsp" %>
		</div>
	</body>
</html>