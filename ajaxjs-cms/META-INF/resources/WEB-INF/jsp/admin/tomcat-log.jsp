<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="后台日誌管理" />
	</jsp:include>
	<script src="//cdn.bootcss.com/jquery/2.1.4/jquery.js"></script>
</head>
<body>
	<div>
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">后台日誌</template>
		</ajaxjs-admin-header>
	</div>
		
	<script>
		new Vue({el:'body > div'});
	</script>
	
	<div id="log-container" style="height: 450px; overflow-y: scroll; background: #333; color: #aaa; padding: 10px; margin: 0 auto;width: 89%;">
		<div></div>
	</div>

	<script>
		$(document).ready(function() {
			// 指定websocket路径
			var websocket = new WebSocket('ws://' + document.location.host + '/${ctx}/tomcat_log');
			websocket.onmessage = function(event) {
				// 接收服务端的实时日志并添加到HTML页面中
				$("#log-container div").append(event.data);
				// 滚动条滚动到最低部
				$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
			};
		});
	</script>
</body>
</html> 