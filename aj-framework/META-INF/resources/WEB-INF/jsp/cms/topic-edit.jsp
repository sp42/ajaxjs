<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
		<script src="${aj_static_resource}/dist/admin/admin.js"></script>
	</head>
	<body>
		<div class="aj-form-row-holder">
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
				<template slot="btns">
				<c:if test="${!isCreate}">
					<a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | 
				</c:if>
					<a :href="ajResources.ctx + '/admin/${shortName}/list/'">${uiName}列表</a> | 
				</template>
			</aj-admin-header>

			<form action="." method="${isCreate ? 'POST' : 'PUT'}">
				<c:if test="${!isCreate}">
					<!-- 传送 id 参数 -->
					<input type="hidden" name="id" value="${info.id}" />
				</c:if>
			
				<div>
					<label>
						<div class="label">名 称：</div> 
						<input placeholder="请填写${uiName}名称" size="60" required name="name" value="${info.name}" type="text" />
					</label> 
					<label>
 						<div class="label">创建日期：</div>  
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />"></aj-form-calendar-input> 
 					</label> 
				</div>
				<div>
					<div class="label" style="vertical-align: top;">正 文：</div>
					<div style="display: inline-block; width: 90%;">
						<!-- HTML 在线编辑器，需要 textarea 包裹着内容 -->
						<aj-form-html-editor field-name="content" base-path="${ctx}" ref="htmleditor" upload-image-action-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=2">
							<textarea class="hide" name="content">${info.content}</textarea>
						</aj-form-html-editor>
					</div>
				</div>
				<div>
					<table >
						<tr>
							<td><div class="label" style="float: left;">封面图：</div></td>
							<td>
								<c:choose>
									<c:when test="${isCreate}">
											<span>请保存记录后再上传图片。</span>
									</c:when>
									<c:otherwise>
											<!-- 图片上传 -->
											<aj-xhr-upload action="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catalog=2" 
												:is-img-upload="true" 
												hidden-field="cover" 
												hidden-field-value="${info.cover}" 
												img-place="${empty info.cover ? commonAsset.concat('/images/imgBg.png') : aj_allConfig.uploadFile.imgPerfix.concat(info.cover)}">
											</aj-xhr-upload>
									</c:otherwise>
								</c:choose>		
							</td>	
						</tr>
					</table>
				</div>
				<aj-admin-state :checked="${empty info.stat ? 9 : info.stat}"></aj-admin-state>
				
				<div>
					<!--按钮 -->
					<aj-admin-info-btns :is-create="${isCreate}"></aj-admin-info-btns>
				</div>
			</form>
		</div>
		<script>
			App = new Vue({el: '.aj-form-row-holder'});
			aj.xhr.form('.aj-form-row-holder form', aj.admin.defaultAfterCreate);
			${isCreate ? 'window.isCreate = true;' : ''}
		</script>
	</body>
</html>