<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
</jsp:include>
</head>
<body>
	<div class="center userCenter">
		<%@include file="user-center-menu.jsp"%>
		
		<div class="right avater">
			<h3 class="jb">用户头像</h3>

			<form class="user-form center" action="${ctx}/user/add_information"
				method="post">
				<div class="formBox">
					<p>支持jpg、png、jpeg、bmp，图片大小5M以内 </p>
<!-- 图片上传 -->
<ajaxjs-img-upload-perview ref="uploadControl" img-place="${empty avatar ? commonAsset.concat('/images/imgBg.png'): ctx.concat(avatar.path)}" />
				</div>
				<dl>
					<dt></dt>
					<dd>
						<button>提交</button>
					</dd>
				</dl>
			</form>
<!-- 图片上传（iframe 辅助） -->
<ajaxjs-fileupload-iframe upload-url="${ctx}/user/info/avatar" ref="uploadIframe"></ajaxjs-fileupload-iframe>
		</div>
	</div>
	
	
	<script>
		new Vue({
			el : '.avater'
		});
	</script>
</body>
</html>