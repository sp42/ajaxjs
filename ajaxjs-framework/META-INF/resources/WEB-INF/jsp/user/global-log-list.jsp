<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<jsp:useBean id="Encode" class="com.ajaxjs.util.Encode" scope="request" /> 
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
			</ajaxjs-admin-header>
		</div>
		<script>
			new Vue({el:' body > div'});
		</script>
		
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
					<th style="min-width:90px;" class="name">${uiName}名称</th>
					<th style="min-width:90px;">${uiName}内容</th>
					<th style="min-width:120px;">创建时间</th>
					<th style="min-width:60px;">操作者</th>
					<th>ip</th>
					<th>sql</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="7">
						<aj-form-betweenDate></aj-form-betweenDate>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach items="${PageResult}">
					<tr>
						<td>${item.id}</td>
						<td>${item.name}</td>
						<td title="${item.content}">${item.content}</td>
						<td><c:dateFormatter value="${item.createDate}" /></td>
						<td>${item.userId}</td>
						<td>${item.ip}</td>
						<td>${Encode.base64Decode(item._sql)}</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		<script>
			new Vue({el: '.listTable'});
		</script>
	</body>
</html>