<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/jsp/common/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
	<jsp:param name="title" value="${uiName}管理" />
</jsp:include>
<style>
.aj-form-html-editor iframe {
	height: 150px !important;
}

.label {
	width: 150px;
	vertical-align: top;
}

label>div {
	display: inline-block;
}

label>div .note {
	color: gray;
	font-size: .9rem;
}

label>div a {
	text-decoration: underline;
}

label>div input {
	margin-bottom: 5px;
}
</style>
</head>
<body>
	<div class="admin-entry-form">
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header> 
			<template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
			<template slot="btns"> <c:if test="${!isCreate}">
				<a :href="ajResources.ctx + '/admin/${tableName}/'">新建</a> | </c:if> <a :href="ajResources.ctx + '/admin/${tableName}/list/'">${uiName}列表</a> | 
			</template> 
		</ajaxjs-admin-header>
		<!-- // 后台头部导航 -->

		<form action="." method="${isCreate ? 'POST' : 'PUT'}"
			class="entityEdit">
			<c:if test="${!isCreate}">
				<input type="hidden" name="id" value="${info.id}" />
				<!-- 传送 id 参数 -->
			</c:if>

			<div>
				<label>
					<div class="label">名 称：</div> 
					<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" type="text" value="${info.name}" />
				</label> 
			</div>

			<div>
				<div class="label" style="vertical-align: top;">更新简介：</div>
				<div style="display: inline-block; width: 50%;">
					<!-- HTML 在线编辑器，需要 textarea 包裹着内容 -->
					<aj-form-html-editor field-name="content" base-path="${ctx}" ref="htmleditor"> <textarea class="hide" name="content">${info.content}</textarea> </aj-form-html-editor>
				</div>
			</div>
			

			<div>
				<label>
					<div class="label">安卓 apk 地址：</div> 
					<div>
						<input type="text" placeholder="请填入${uaiName}安卓链接" size="50" required="required" name="downUrl" value="${info.downUrl}"> 
						<div class="note"><a href="${info.downUrl}" download>下载</a></div>
					</div>
					
				</label> 
				<label>
					<div class="label">苹果商店连接：</div> 
					<div>
						<input TYPE="text" placeholder="请填入${uaiName}ios链接" size="50" required="required" name="iosAppStoreUrl" value="${info.iosAppStoreUrl}">
						<div class="note"><a href="${info.iosAppStoreUrl}" target="_blank">访问</a></div>
					</div>
				</label>
			</div>
			<div> 
				<label>
					<div class="label">App：</div> 
					<div>
						<select name="appId" class="ajaxjs-select" style="width:150px;">
							<option value="1" ${info.appId == 1 ? 'selected' : ''}>普通版</option>
							<option value="2" ${info.appId == 2 ? 'selected' : ''}>信贷经理版</option>
						</select>
					</div>
				</label> 
				</div><div>
				<label>
					<div class="label">安卓版本号：</div> 
					<div>
						<input TYPE="text" placeholder="请填入${uaiName}安卓版本号" size="30" required="required" name="apkVersion" value="${info.apkVersion}">
						<br />
						<div class="note">安卓版本号 YYYYMMDD+两位数字，例如2018090901</div>
					</div>
				</label> 
				<label>
					<div class="label">iOS 版本号：</div> 
					<div>
						<input TYPE="text" placeholder="请填入${uaiName}iOS版本号" size="30" required="required"name="iosVersion" value="${info.iosVersion}">
						<br />
						<div class="note">iOS版本号 YYYYMMDD+两位数字，例如2018090901</div>
					</div>
				</label>
			</div>

			<div>
				<div class="label" style="float: left;">Apk 上传：</div>
				<c:choose>
					<c:when test="${isCreate}">
						<span>请保存记录后再上传图片。</span>
					</c:when>
					<c:otherwise>
						<table>
							<tr>
								<td>
									<!-- 文件上传 --> 
									<ajaxjs-file-upload ref="uploadControl"></ajaxjs-file-upload> 
								</td>
								<td>
									上传成功后的地址会自动填入到 apk 包地址中
								</td>
							</tr>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
			<div>
				<!--按钮 -->
				<ajaxjs-admin-info-btns :is-create="${isCreate}"></ajaxjs-admin-info-btns>
			</div>
		</form>
		<!-- 图片上传（iframe 辅助） -->
		<ajaxjs-fileupload-iframe upload-url="${ctx}/admin/${tableName}/upload/" ref="uploadIframe" accpect-file-type=".apk"></ajaxjs-fileupload-iframe>

		<!-- 弹出层上传对话框 -->
		<aj-popup-upload ref="uploadLayer" upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=1" img-place="${commonAsset.concat('/images/imgBg.png')}"></aj-popup-upload>
	</div>
	<script>
		App = new Vue({
			el : '.admin-entry-form',
			mounted : function(){
				this.$refs.uploadIframe.uploadOk_callback = function(url) {
					aj('input[name=downUrl]').value = url;
				}
			}
		});

		// 表单提交
		aj.xhr.form('form.entityEdit', function(json) {
			if (json && json.msg)
				aj.alert.show(json.msg);
			
			${isCreate ? 'json && json.isOk && setTimeout(function(){location.assign(json.newlyId + "/");}, 2000);' : ''}
		}, {
			beforeSubmit : function(form, json) {
				json.content = App.$refs.htmleditor.getValue({
					cleanWord : eval('${aj_allConfig.article.cleanWordTag}'),
					encode : true
				});
			}
		});
	</script>
	<c:if test="${isCreate}">
		<script>
			window.isCreate = true;
		</script>
	</c:if>
</body>
</html>