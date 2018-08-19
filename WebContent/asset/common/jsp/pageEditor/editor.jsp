<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/asset/common/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/ajaxjs-ui/less/admin.less" />
		</jsp:include>
		<style>
			body {
				padding: 2%;
			}
		</style>
	</head>
	<body class="pageEditor">
		<nav>
			<h3 class="head">页面编辑器</h3>
		</nav>
	
		<div class="btns">
			<button class="ajaxjs-btn backBtn">返回</button>
			<button class="ajaxjs-btn saveBtn">保存</button>
			<button class="ajaxjs-btn perviewBtn">预览</button>
		</div>
		<%
			// 是否不允许编辑的？
			if (request.getAttribute("contentBody") == null) {
		%>
		<script>
			alert("该页面没有任何编辑内容！");
			history.back();
		</script>
		<%
			}
		%>
		<script src="${commonAsset}js/widgets/htmlEditor.js"></script>
		<commonUI:htmlEditor name="content" basePath="../">${contentBody}</commonUI:htmlEditor>
		<script>
		
			var htmlEditor = new ajaxjs_HtmlEditor(document.querySelector('.htmlEditor'));
		
			// 返回
			document.querySelector('.backBtn').onclick = function(e) {
				window.history.go(-1);
			}
	
			// 提交数据
			document.querySelector('.saveBtn').onclick = function(e) {
				ajaxjs.xhr.post('../save.do', function(json) {
					if (json.isOk)
						alert('修改页面成功！');
					else
						alert(json.msg);
				}, {
					url : '${param.url}',
					contentBody : encodeURIComponent(htmlEditor.getValue())
				});
			}
	
			// 提交数据
			document.querySelector('.perviewBtn').onclick = function(e) {
				window.open('${pageContext.request.contextPath}${param.url}', '_blank');
			}
		</script>
	</body>
</html>