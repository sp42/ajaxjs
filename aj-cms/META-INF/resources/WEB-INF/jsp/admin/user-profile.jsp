<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/user"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="个人信息" />
		</jsp:include>
		<style>
		
		</style>
	</head>
	<body>
		<div>
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">个人信息</template>
			</aj-admin-header>
		</div>
		<script>
			new Vue({el:' body > div'});
		</script>
 
		<tags:profile />
	</body>
</html>

