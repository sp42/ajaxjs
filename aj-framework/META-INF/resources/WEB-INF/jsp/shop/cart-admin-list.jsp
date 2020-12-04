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
						<aj-form-betweenDate></aj-form-betweenDate>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach items="${PageResult}">
					<tr>
						<td>${item.id}</td>
						<td style="text-align:left;">
							<tags:common type="thumb" thumb="${item.cover}" />
							<a href="../../goods/${item.goodsId}/">${item.goodsName}</a>
						</td>
						<td>${item.goodsFormat}</td>
						<td>￥${item.price}</td>
						<td>${item.goodsNumber}</td>
						<td>￥${item.goodsNumber * item.price}</td>
						<td>${empty item.groupId ? '否' : '是'}</td>
						<td title="过滤此用户购物车">
							<a href="?userId=${item.userId}">
								#${item.userId}
							</a>
						</td>
						<td><c:dateFormatter value="${item.createDate}" /></td>
						<td>
							<a href="${ctx}/admin/user/${item.userId}/">
								<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />用户详情
							</a> 
							<a href="javascript:aj.admin.del('${item.id}', '${item.goodsName}');">
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
			new Vue({el: '.listTable'});
			aj.widget.imageEnlarger();// 鼠标移动大图
		</script>
	</body>
</html>