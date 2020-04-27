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
				<col />
				<col />
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th class="name">商品名称</th>
					<th>规格/分类</th>
					<th>单价</th>
					<th>数量</th>
					<th>总价</th>
					<th>是否团购</th>
					<th>用户 id</th>
					<th>加入日期</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="11">
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
						<td style="text-align:left;">
						<c:if test="${not empty current.cover}">
							<img src="${ctx}/${current.cover}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
						 		onmouseenter="aj.imageEnlarger.singleInstance.imgUrl = '${ctx}/${current.cover}';" onmouseleave="aj.imageEnlarger.singleInstance.imgUrl = null;" />
						</c:if>
						<c:if test="${empty current.groupId}">
							<a href="../../goods/${current.goodsId}">${current.goodsName}</a>
						</c:if>
						<c:if test="${not empty current.groupId}">
							<a href="../../simple-group/${current.groupId}">${current.goodsName}</a>
						</c:if>
						</td>
						<td>${current.goodsFormat}</td>
						<td>￥${current.price}</td>
						<td>${current.goodsNumber}</td>
						<td>￥${current.goodsNumber * current.price}</td>
						<td>${empty current.groupId ? '否' : '是'}</td>
						<td title="过滤此用户购物车">
							<a href="?userId=${current.userId}">
								#${current.userId}
							</a>
						</td>
						<td><c:dateFormatter value="${current.createDate}" /></td>
						<td>
							<a href="${ctx}/user/center/info/${current.userId}/">
								<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />用户详情
							</a> 
							<a href="javascript:aj.admin.del('${current.id}', '${current.goodsName}');">
								<img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
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