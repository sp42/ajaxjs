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
		<div>
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"></template>
			</aj-admin-header>
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
			<c:foreach items="${PageResult}">
				<tr>
					<td>${item.id}</td>
					<td>${item.name}</td>
					<td><aj-img-thumb img-url="${item.name}"></aj-img-thumb></td>
					<td>${DICT[item.catalogId]}</td>
					<td><c:toFix value="${item.fileSize/1024}" />KB</td>
					<td>${item.owner}</td>
					<td><c:dateFormatter value="${item.createDate}" /></td>
					<td>
						<a href="${aj_allConfig.uploadFile.isLocalUpload ? ctx.concat(aj_allConfig.uploadFile.localImgFolder) : aj_allConfig.uploadFile.imgPerfix}${item.name}" download>下载</a> 
						<a href="javascript:aj.admin.helper.del('${item.id}', '${item.name}');">
							<img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" /> 删除
						</a>
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
			aj.widget.img.initImageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>