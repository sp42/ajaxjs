<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/asset/common/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/ajaxjs-ui/less/admin.less" />
			<jsp:param name="title" value="全局配置" />
		</jsp:include>
	</head>
<body class="configForm admin-entry-form">
	<div>	
		<ajaxjs-admin-header>
			<template slot="title">全局配置：<span>请点击有+号菜单项以展开下一级的内容</span></template>
		</ajaxjs-admin-header>
	</div>
	
	<form>
		<button onclick="save();return false;" class="ajaxjs-btn" style="margin-left: 30px;">保存</button>
		<div class="tree">
			<div class="tipsNote hide">
				<div class="aj-arrow toLeft"></div>
				<span></span>
			</div>
		</div>
		<br />
		<br />
		<p>小白用户请谨慎修改配置参数！</p>
	</form>
	
	
	<script>
		new Vue({el : 'body>div'});
		var configJson = ${configJson};
		var jsonScheme = ${jsonSchemePath};
	</script>
	
	<script src="${ajaxjsui}/js/widgets/admin/allConfig-renderer.js"></script>
	<script src="${ajaxjsui}/js/widgets/admin/allConfig.js"></script>
</body>
</html>