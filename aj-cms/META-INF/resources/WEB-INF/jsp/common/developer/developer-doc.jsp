<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" 	uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="title" value="全局配置" />
	</jsp:include>
	<style>
		.aj-simple-tab-vertical>ul{
			width:30px;
		}
		.aj-simple-tab-vertical>ul>li{
			height: 150px;
		}
		html, body, .tab,.aj-simple-tab-vertical > div>div{
			height:100%;
		}
		.aj-simple-tab-vertical > div {
			height:95%;
			width:96%;
		}
	</style>
</head>
<body class="admin-entry-form">
	<div>
		<aj-admin-header> 
			<template slot="title">实用工具</span></template>
		</aj-admin-header>
	</div>
		<div class="aj-simple-tab-vertical tab" style="padding: 1% 5%;">
				<ul>
					<li :class="{'selected': 0 === selected}" @click="selected = 0">前端文档</li>
					<li :class="{'selected': 1 === selected}" @click="selected = 1">数据库文档</li>
				</ul>
			<div class="content">
				<div :class="{'selected': 0 === selected}">
					<!-- TAB 内容 -->
						<iframe src="https://framework.ajaxjs.com/"  frameborder="no" width="100%" height="96%"></iframe>
					<!-- // TAB 内容 -->
				</div>
				
				<div :class="{'selected': 1 === selected}">
					<!-- TAB 内容 -->

					<iframe src="${ctx}/admin/common/DataBaseShowStru/"  frameborder="no" width="100%" height="96%"></iframe>
					<!-- // TAB 内容 -->
				</div>
			</div>
		</div>

	<script>
		TAB = new Vue({
			el : '.tab',
			data:{
				selected:0
			}
		});
	</script>
</body>
</html>