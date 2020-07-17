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
			<aj-admin-filter-panel :catalog-id="${domainCatalog_Id}" search-field-value="entry.name" 
				:selected-catalog-id="${empty param.catalogId ? 'null' : param.catalogId}">
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
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th class="name">${uiName}名称</th>
					<th>创建时间</th>
					<th>修改时间</th>
					<th>分 类</th>
					<th>是否上线</th>
					<th>封面图片</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="9">
						<aj-admin-xsl params="<%=com.ajaxjs.web.ServletHelper.getAllQueryParameters(request) %>"></aj-admin-xsl>					
						<aj-form-betweenDate></aj-form-betweenDate>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach items="${PageResult}">
					<tr>
						<td>${item.id}</td>
						<td title="${item.intro}">${item.name}</td>
						<td><c:dateFormatter value="${item.createDate}" format="yyyy-MM-dd" /></td>
						<td><c:dateFormatter value="${item.updateDate}" format="yyyy-MM-dd" /></td>
						<td><a href="?catalogId=${item.catalogId}">${newsCatalogs[item.catalogId].name}</a></td>
						<td>${(empty item.stat || item.stat == 1) ? '已上线': '已下线'}</td>
						<td><tags:common type="thumb" thumb="${item.cover}" /></td>
						<td is="aj-admin-control" id="${item.id}" name="${item.name}" preview="${aj_allConfig.data.entityProfile.article.previewUrl}"></td>			
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			new Vue({el: '.listTable'});
			aj.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>