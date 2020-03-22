<%@page pageEncoding="UTF-8"%>
<!doctype html>
<html>
	<head>
		<!-- 全局统一的 HTML 文件头 -->
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="网站结构" />
		</jsp:include>
	</head>
	<body>
		<div class="aj-json-form">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">配置参数</template>
			</ajaxjs-admin-header>
			
			<div class="aj-simple-tab-horizontal tab" style="padding: 1% 5%;">
				<ul>
					<li>文件上传配置</li>
					<li>用户配置</li>
					<li>邮件服务器配置</li>
					<li>加密配置</li>
					<li>短信服务配置</li>
					<li>网络安全防控</li>
					<li>开发者参数</li>
				</ul>
				<div>
					<div><aj-json-form :scheme="scheme" :config="config" path="uploadFile"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="user"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="mailServer"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="Symmetric"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="security"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="security"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="forDelevelopers"></aj-json-form></div>
				</div>
			</div>	
		</div>
		
		<script src="${ctx}/config/form.js"></script>
		<script>
			new Vue({
				el:'.aj-json-form',
				data: {
					scheme : ${schemeJson},
					config : ${configJson}
				},
				mixins: [aj.tabable]
			});
		</script>
	</body>
</html>
