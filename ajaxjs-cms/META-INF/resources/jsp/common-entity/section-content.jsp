<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/jsp/common/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="vue">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${tableName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
	
			 
		</div>
		
		<script>
			new Vue({el:'.vue'});
		</script>

		 
		<div style="text-align:center;margin:2%;">
			<%@include file="/jsp/common/pager.jsp" %>
		</div>
	</body>
</html>