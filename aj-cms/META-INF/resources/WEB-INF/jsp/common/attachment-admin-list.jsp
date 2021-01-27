<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
	<!DOCTYPE html>
	<html>
		<head>
			<jsp:include page="/WEB-INF/jsp/head.jsp">
				<jsp:param name="title" value="${uiName}管理" />
			</jsp:include>
			
			<!-- Admin 公共前端资源 -->
			<link rel="stylesheet" href="${aj_static_resource}dist/css/admin/admin.css" />
			<script src="${aj_static_resource}dist/admin/admin.js"></script>
		</head>
	<body>
		<div>
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"></template>
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
			<col style="text-align: center;" align="center" />
		</colgroup>
		<thead>
			<tr>
				<th>#</th>
				<th class="name">${uiName}名称</th>
				<th>图片预览</th>
				<th>分类</th>
				<th>${uiName}文件大小</th>
				<th>上传者</th>
				<th>上传时间</th>
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="87"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult}">
				<tr>
					<td>${current.id}</td>
					<td>${current.name}</td>
					<td><tags:common type="thumb" thumb="${current.name}" /></td>
					<td>${DICT[current.catalogId]}</td>
					<td>${current.fileSize}KB</td>
					<td>${current.owner}</td>
					<td><c:dateFormatter value="${current.createDate}" /></td>
					<td>
						<a href="${aj_allConfig.uploadFile.imgPerfix}${current.name}" download>下载</a> 
						<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
					</td>
				</tr>
			</c:foreach>
		</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
	</body>
</html>