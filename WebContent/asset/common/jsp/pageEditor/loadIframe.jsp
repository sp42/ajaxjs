<%@ page pageEncoding="UTF-8"%>	
<!DOCTYPE html>
<html>	
	<head>
		<jsp:include page="/asset/common/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/ajaxjs-ui/less/admin.less" />
			<jsp:param name="title" value="浏览页面以选择编辑" />
		</jsp:include>
		<style type="text/css">
			html{
				height:100%;
				overflow:hidden;
			}
			body{
				height:99%;
			}
			
			iframe{
				border:0;
				width:100%;
				height:100%;
			}
			
			button{
				position: fixed;
				left: 20px;
				top:20px;
				padding:5px 20px;
			}
		</style>
	</head>
	<body>
		<button class="ajaxjs-btn">编辑此页面</button>
		<iframe src="${empty param.url ? '../../' :  param.url}"></iframe>
		
		<script>
			document.querySelector('button').onclick = function() {
				var url = document.querySelector('iframe').contentDocument.location.href;
				// 去掉 dom 返回的前缀
				url = url.replace(/http:\/\/[^\/]*/, '');
				url = url.replace('${pageContext.request.contextPath}', '');
				window.location.assign('loadPage/?url=' + url);
			}
		</script>
	</body>
</html>