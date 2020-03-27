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
		
		<table class="ajaxjs-niceTable" align="center" width="90%">
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
						<form action="." method="GET" class="dateRange" @submit="valid($event)">
							起始时间：
							<aj-form-calendar-input field-name="startDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
							截至时间：
							<aj-form-calendar-input field-name="endDate" :date-only="true" :position-fixed="true"></aj-form-calendar-input>
							<button class="aj-btn">查询</button>
						</form>
						<script type="text/javascript">
							aj.form.betweenDate('.dateRange');
						</script>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td>${current.name}</td>
						<td title="${current.content}">${current.content}</td>
						<td>
							<c:dateFormatter value="${current.createDate}" />
						</td>
						<td>${current.userId}</td>
						<td>${current.ip}</td>
						<td>${Encode.base64Decode(current._sql)}</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		
		<div style="text-align:center;margin:2%;">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
	</body>
</html>