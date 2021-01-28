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
			<aj-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</aj-admin-header>
	
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :no-catalog="true">
				<!-- 选择商家 -->
				商家过滤器：<select class="aj-select" name="sellerId" onchange="location.assign('?filterField=sellerId&filterValue=' + this.selectedOptions[0].value);">
						<option value="null">不指定商家</option>
					<c:foreach items="${sellers}" var="item">
						<option value="${item.key}" ${(param.filterValue != 'null') && (param.filterValue == item.key)  ? 'selected' : ''}>
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
		<table class="aj-niceTable listTable">
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
					<th>创建/修改时间</th>
					<th>开始/结束时间</th>
					<th>已售/库存</th>
					<th>标题价格/实际价格</th>
					<th>商家</th>
					<th>状态</th>
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
						<td title="${current.subTitle}" style="text-align:left;">
						<c:if test="${not empty current.cover}">
							<img src="${ctx}${current.cover}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
						 		onmouseenter="aj.widget.imageEnlarger.singleInstance.imgUrl = '${ctx}${current.cover}';" onmouseleave="aj.widget.imageEnlarger.singleInstance.imgUrl = null;" />
						</c:if>
							${current.name}
						</td>
						<td>
							<c:dateFormatter value="${current.createDate}"  /><br />
							<c:dateFormatter value="${current.updateDate}"  /> 
						</td>
						<td>
							<c:dateFormatter value="${current.beginTime}"  /><br />
							<c:dateFormatter value="${current.endTime}"  /> 
						</td>
						<td>${current.soldNumber}/${current.maxGoodsNumber}</td>
						<td>￥${current.coverPrice}/￥${current.titlePrice}</td>
						<td><a href="?filterField=sellerId&filterValue=${current.sellerId}">${sellers[current.sellerId].name}</a></td>
						<td>${statusMap[current.stat]}</td>
						<td>
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
			aj.widget.img.initImageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>