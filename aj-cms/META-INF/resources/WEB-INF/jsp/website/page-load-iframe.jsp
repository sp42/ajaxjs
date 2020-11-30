<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="浏览页面以选择编辑" />
	</jsp:include>
	<style type="text/css">
		html, body {
			height: 100%;
			overflow: hidden;
		}
		
		body>div {
			height: 99%;
		}
		
		iframe {
			border: 0;
			width: 100%;
			height: 95%;
		}
		
		button {
			padding: 5px 20px;
			margin-left: 1%!important;
		}
		
		input{
			width: 80%;
		}
	</style>
</head>
<body>
	<div>
		<button @click="clk" class="aj-btn">编辑此页面</button> 
		<input type="text" :value="url" class="aj-input" />
		<iframe src="${empty param.url ? '../../../' :  param.url}" @load="onchange($event)"></iframe> 
	</div>

	<script>
		new Vue({
			el: 'body > div',
			data: {
				url : ""
			},
			methods: {
				onchange(e) {
					var el = e.target;
					this.url = location.origin + el.contentWindow.location.pathname;
				},
				clk() {
					var url = aj('iframe').contentDocument.location.href;
					// 去掉 dom 返回的前缀
					url = url.replace(/http:\/\/[^\/]*/, '');
					url = url.replace('${ctx}', '');
					
					window.location.assign('loadPage/?url=' + url);
				}
			}
		});
	</script>
</body>
</html>