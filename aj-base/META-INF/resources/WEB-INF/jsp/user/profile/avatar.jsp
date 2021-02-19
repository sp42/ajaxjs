<%@ page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>

<tags:user-center>
	<h3 class="aj-center-title">修改头像</h3>
	<script src="${ajaxjs_ui_output}/lib/exif.min.js"></script>
	
	<form method="put" action="${ctx}/user/profile/" class="avatar" style="width:600px;margin:0 auto;text-align: center;">
		<input type="hidden" name="id" value="${userId}" />
		
		<aj-xhr-upload action="${ctx}/user/profile/avatar/${userUid}/?id=${userId}" :is-img-upload="true" 
			hidden-field="avatar" 
			hidden-field-value="${userAvatar}" 
			img-place="${empty userAvatar ? commonAsset.concat('/images/imgBg.png') : userAvatar}">
		</aj-xhr-upload>
		
		<p class="aj-note">
			先选择图片然后点击上传，然后请记得按下保存方能生效。<br />
			支持jpg、png、jpeg、bmp，图片大小5M以内 </p>
			
		<br />
		<br />
		<br />
		<div class="aj-btnsHolder">
			<button>保存</button> <button onclick="history.back();" class="aj-btn">返回</button>
		</div>
	</form>
	<script>
		new Vue({
			el : 'form.avatar'
		});
		
		aj.xhr.form('form.avatar', json => {
			aj.xhr.get('${ctx}/user/profile/avatar/updateAvatar/', json => {
				aj.msg.show("修改头像成功");
				document.body.$('.avatar img').src = "${aj_allConfig.uploadFile.imgPerfix}" + document.body.$("input[name=avatar]").value;
			}, {
				avatar: document.body.$("input[name=avatar]").value
			});
		});
	</script>
</tags:user-center>