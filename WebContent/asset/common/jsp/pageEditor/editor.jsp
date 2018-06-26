<%@ page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common/UI"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/common/less/admin.less" />
	<body class="pageEditor">
		<style>
			body {
				padding: 2%;
			}
		</style>
		<nav>
			<h3 class="head">页面编辑器</h3>
		</nav>
	
		<div class="btns">
			<button class="ajaxjs-btn-1 backBtn">返回</button>
			<button class="ajaxjs-btn-1 saveBtn">保存</button>
			<button class="ajaxjs-btn-1 perviewBtn">预览</button>
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
				window.open('${pageContext.request.contextPath}${param.url}',
						'_blank');
			}
		</script>
	</body>
</html>