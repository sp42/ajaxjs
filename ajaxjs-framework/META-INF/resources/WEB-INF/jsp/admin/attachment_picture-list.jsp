<%@page pageEncoding="UTF-8" import="java.util.HashMap"%>
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
				<th>${uiName}分类</th>
				<th>${uiName}尺寸<br/>${uiName}文件大小</th>
				<th>创建者</th>
				<th>创建时间</th>
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="8"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult}">
				<tr>
					<td>${current.id}</td>
					<td>${current.name}</td>
					<td>${DICT[current.catalog]}</td>
					<td>${current.picWidth}x${current.picHeight}<br />${current.fileSize}KB</td>
					<td>${current.owner}</td>
					<td><c:dateFormatter value="${current.createDate}" /></td>
					<td>
						<a href="${ctx}${current.path}" target="_blank">
							<img src="${ctx}${current.path}" style="max-width:100px; vertical-align: middle;" 
							onmouseenter="aj.imageEnlarger.singleInstance.imgUrl = '${ctx}${current.path}';" 
							onmouseleave="aj.imageEnlarger.singleInstance.imgUrl = null;" />
						</a>
						<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
					</td>
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