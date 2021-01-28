<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="账号中心" />
		</jsp:include>
		<style>
			.safe {
			    margin: 3% auto;
			    width: 60%;
			}
			
			/* 后台不需要这个登录日志 */
			.log-history{
				display: none;
			}
		</style>
	</head>
	<body>
		<div>
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">账号中心</template>
			</aj-admin-header>
		</div>
		<script>
			new Vue({el:' body > div'});
		</script>
 
		<%@include file="../user/account/account-inner.jsp" %>
	</body>
</html>

