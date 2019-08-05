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
					<th>头像</th>
					<th class="name">${uiName}账号名称</th>
					<th>用户姓名</th>
					<th>性别</th>
					<th>邮件/手机</th>
					<th>地区</th>
					<th>注册日期</th>
					<th>用户组</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="10"></td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td><img src="${current.avatar}" style="max-width:80px;" /></td>
						<td>
						<c:if test="${not empty current.cover}">
							<img src="${ctx}${current.cover}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
						 		onmouseenter="aj.imageEnlarger.singleInstance.imgUrl = '${ctx}${current.cover}';" onmouseleave="aj.imageEnlarger.singleInstance.imgUrl = null;" />
						</c:if>
							${current.name}
						</td>
						<td>${current.username}</td>
						<td>${SexGender[current.sex]}</td>
						<td>${current.email}<br />${current.phone}</td>
						<td>${current.location}</td>
						<td><c:dateFormatter value="${current.createDate}" /></td>
						<td>${UserGroups[current.roleId].name}</td>
						<td>
							<a href="../../userLoginLog/?filterField=userId&filterValue=${current.id}">登录日志</a> | 
							<a href="../${current.id}/bookmark/">收藏</a> | 
							<a href="../${current.id}/address/">地址簿</a> | 
							<a href="10000/order">订单</a> | 
							<a href="../../cart/list/${current.id}">购物车</a>
							| <a href="../${current.id}">详情</a> | <a href="javascript:aj.admin.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" />删除</a>
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