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
		<div class="edit">
			<aj-form-html-editor field-name="content" base-path="../" ref="htmleditor">
				<textarea class="hide" name="content">${contentBody}</textarea>
			</aj-form-html-editor>
			
			<!-- 弹出层上传对话框 -->
			<aj-popup-upload ref="uploadLayer" upload-url="${ctx}/admin/attachmentPicture/upload/staticPageUsedImg/" img-place="${commonAsset.concat('/images/imgBg.png')}">
			</aj-popup-upload>
		</div>
		<script>
			App = new Vue({el:'.edit'});
			
			// 返回
			aj('.backBtn').onclick = function(e) {
				//window.history.go(-1);
				window.location = document.referrer;
			}
	
			// 提交数据
			aj('.saveBtn').onclick = function(e) {
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
			aj('.perviewBtn').onclick = function(e) {
				window.open('${pageContext.request.contextPath}${param.url}', '_blank');
			}
		</script>
	</body>
</html>