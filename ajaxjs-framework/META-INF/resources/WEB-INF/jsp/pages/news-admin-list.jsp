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
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td title="${current.intro}">${current.name}</td>
						<td>
							<c:dateFormatter value="${current.createDate}" format="yyyy-MM-dd" />
						</td>
						<td>
							 <c:dateFormatter value="${current.updateDate}" format="yyyy-MM-dd" /> 
						</td>
						<td>
							<a href="?catalogId=${current.catalogId}">${newsCatalogs[current.catalogId].name}</a></td>
						<td>
							${(empty current.stat || current.stat == 1) ? '已上线': '已下线'}
						</td>
						<td>
						<c:if test="${not empty current.cover}">
							<a href="${current.cover.startsWith('http') ? current.cover : ctx.concat('/images/').concat(current.cover)}" target="_blank">
								<img src="${current.cover.startsWith('http') ? current.cover : ctx.concat('/images/').concat(current.cover)}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
							 		onmouseenter="aj.imageEnlarger.singleInstance.imgUrl = '${current.cover.startsWith('http') ? current.cover : ctx.concat('/images/').concat(current.cover)}';" 
							 		onmouseleave="aj.imageEnlarger.singleInstance.imgUrl = null;" />
							</a>
						</c:if>
						</td>
						<td>
							<a href="../../../${shortName}/${current.id}/" target="_blank">浏览</a>
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
			aj.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>