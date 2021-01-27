<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp"></jsp:include>
		<style>
			body {
				padding: 2%;
			}
		</style>
			
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
		<script src="${aj_static_resource}/dist/admin/admin.js"></script>
	</head>
	<body class="pageEditor">
		<nav>
			<h3 class="head">页面编辑器</h3>
		</nav>
	
		<div class="btns">
			<button class="aj-btn backBtn">返回</button>
			<button class="aj-btn saveBtn">保存</button>
			<button class="aj-btn perviewBtn">预览</button>
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
		<div class="edit">
			<aj-form-html-editor field-name="content" base-path="${ctx}${param.url.replaceAll('/$', '')}" ref="htmleditor" upload-image-action-url="${ctx}/admin/attachmentPicture/upload/staticPageUsedImg/?url=${param.url}">
				<textarea class="hide" name="content">${contentBody}</textarea>
			</aj-form-html-editor>
			
			<!-- 弹出层上传对话框 -->
			<aj-popup-upload ref="uploadLayer" upload-url="${ctx}/admin/attachmentPicture/upload/staticPageUsedImg/?url=${param.url}" img-place="${commonAsset.concat('/images/imgBg.png')}">
			</aj-popup-upload>
		</div>
		<script>
			App = new Vue({el:'.edit'});
			
			// 返回
			document.querySelector('.backBtn').onclick = function(e) {
				//window.history.go(-1);
				window.location = document.referrer;
			}
	
			// 提交数据
			document.querySelector('.saveBtn').onclick = function(e) {
				aj.xhr.post('../save.do', function(json) {
					if (json.isOk)
						aj.msg.show('修改页面成功！');
					else
						aj.msg.show(json.msg);
				}, {
					url : '${param.url}',
					contentBody : App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true})
				});
			}
	
			// 提交数据
			document.querySelector('.perviewBtn').onclick = function(e) {
				window.open('${pageContext.request.contextPath}${param.url}', '_blank');
			}
		</script>
	</body>
</html>