<%@ page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>	
	<commonTag:head lessFile="/asset/common/less/admin.less">
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
	</commonTag:head>
	<body>
		<button class="ajaxjs-btn">编辑此页面</button>
		<iframe src="../../"></iframe>
		
		<script>
			document.querySelector('button').onclick = function() {
				var url = document.querySelector('iframe').contentDocument.location.href;
				// 去掉 dom 返回的前缀
				url = url.replace(/http:\/\/[^\/]*/, '');
				url = url.replace('${pageContext.request.contextPath}', '');
				window.location.assign('loadPage/edit.do?url=' + url);
			}
		</script>
	</body>
</html>