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
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
	
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :catalog-id="${domainCatalog_Id}" :selected-catalog-id="${empty param.catalogId ? 'null' : param.catalogId}"></aj-admin-filter-panel>
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
					<th class="name">${uiName}标题</th>
					<th>广告类型</th>
					<th>广告链接</th>
					<th>广告图片</th>
					<th>创建时间</th>
					<th class="control">控 制</th>
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
						<td>
						
						${current.name}</td>
						<td>${current.catalogName}</td>
						<td>
							<a href="${current.link}" target="_blank">${current.link}</a>
						</td>
						
						<td>
						<c:if test="${not empty current.cover}">
							<img src="${ctx}${current.cover}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
						 		onmouseenter="aj.widget.imageEnlarger.singleInstance.imgUrl = '${ctx}${current.cover}';" onmouseleave="aj.widget.imageEnlarger.singleInstance.imgUrl = null;" />
						</c:if>
						</td>
						<td>
							<c:dateFormatter value="${current.createDate}" />
						</td>
						<td>
							<a href="../../../${tableName}/${current.id}/" target="_blank">浏览</a>
							<a href="${ctx}/admin/${shortName}/${current.id}/"><img src="${commonAssetIcon}/update.gif" style="vertical-align: sub;" />编辑</a>
							<a href="javascript:aj.admin.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
						</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			aj.widget.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>