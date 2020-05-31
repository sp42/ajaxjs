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
		<style>
			select {
				min-width:120px;
			}
		</style>
	</head>
	<body>
		<div class="vue">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
	
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :catalog-id="${domainCatalog_Id}" 
				:selected-catalog-id="${empty param.catalogId ? 'null' : param.catalogId}">
				
				<!-- 选择商家 -->
				商家过滤器：<select class="aj-select" 
	onchange="this.selectedOptions[0].value == 'null' ? location.assign('?') : location.assign('?sellerId=' + this.selectedOptions[0].value);">
						<option value="null">不指定商家</option>
					<c:foreach items="${sellers}" var="item">
						<option value="${item.key}" ${(param.sellerId != 'null') && (param.sellerId == item.key)  ? 'selected' : ''}>
							${item.value.name}
						</option>
					</c:foreach>
				</select>			
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
				<col />
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th class="name">${uiName}名称</th>
					<th>创建时间</th>
					<th>品牌</th>
					<th>封面价格/标题价格</th>
					<th>分 类</th>
					<th>商家</th>
					<th>是否上线</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="10">
						<!-- 表格底部菜单 -->
						<div style="float:left;margin-top: .5%;">
							<a href="?downloadXSL=true&<%=com.ajaxjs.web.ServletHelper.getAllQueryParameters(request) %>" target="_blank">
								<img src="${commonAssetIcon}/excel.png" width="16" style="vertical-align: middle;" /> 下载 Excel 格式
							</a>
						</div>
						<form action="." method="GET" class="dateRange" @submit="valid($event)">
							起始时间：
							<aj-form-calendar-input field-name="startDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
							截至时间：
							<aj-form-calendar-input field-name="endDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
							<button class="aj-btn">查询</button>
						</form>
						<script>
							aj.form.betweenDate('.dateRange');
						</script>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach items="${PageResult}">
					<tr>
						<td>${item.id}</td>
						<td title="${item.subTitle}" style="text-align:left;">
							<tags:common type="thumb" thumb="${item.cover}" />
							${item.name}
						</td>
						<td><c:dateFormatter value="${item.createDate}" /></td>
						<td>${item.brand}</td>
						<td>￥${item.coverPrice}/￥${item.titlePrice}</td>
						<td>${goodsCatalogs[item.catalogId].name}</td>
						<td><a href="?filterField=sellerId&filterValue=${item.sellerId}">${sellers[item.sellerId].name}</a></td>
						<td>${(empty item.stat || item.stat == 1) ? '已上线': '已下线'}</td>
						<td>
							<a href="${ctx}/admin/${shortName}/${item.id}/"><img src="${commonAssetIcon}/update.gif" style="vertical-align: sub;" />编辑</a>
							<a href="javascript:aj.admin.del('${item.id}', '${item.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
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