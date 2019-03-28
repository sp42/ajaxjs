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
			<!-- // 后台头部导航 -->
			
			<!-- 搜索 -->
			<aj-admin-filter-panel :no-catelog="true"></aj-admin-filter-panel>
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
					<th class="name">${uiName}名称</th>
					<th>安卓版本号 </th>
					<th>iOS版本号</th>
					<th>安卓 apk 下载连接</th>
					<th>苹果商店连接</th>
					<th>创建时间</th>
					<th>修改时间</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="9"></td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td>${current.name}</td>
						<td>${current.apkVersion}</td>
						<td>${current.iosVersion}</td>
						<td><a href="${current.downUrl}" target="_blank" download>下载 apk 包</a></td>
						<td><a href="${current.iosAppStoreUrl}" target="_blank">AppStoer</a></td>
						<td><c:dateFormatter value="${current.createDate}" format="yyyy-MM-dd" /></td>
						<td><c:dateFormatter value="${current.updateDate}" format="yyyy-MM-dd" /></td>
						<td>
							<a href="../../../${shortName}/${current.id}/" target="_blank">浏览</a>
							<a href="${ctx}/admin/appUpdate/${current.id}/"><img src="${commonAssetIcon}/update.gif" style="vertical-align: sub;" />编辑</a>
							<a href="javascript:aj.admin.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
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