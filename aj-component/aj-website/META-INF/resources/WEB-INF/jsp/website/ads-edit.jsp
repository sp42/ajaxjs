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
		<aj-admin-header> <template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
		<template slot="btns"> 
			<c:if test="${!isCreate}">
				<a href="../">新建</a> | 
			</c:if> 
			<a :href="ajResources.ctx + '/admin/cms/ads/list/'">${uiName}列表</a> | </template>
		</aj-admin-header>

		<form action="." method="${isCreate ? 'POST' : 'PUT'}">
			<c:if test="${!isCreate}">
				<input type="hidden" name="id" value="${info.id}" />
				<!-- 传送 id 参数 -->
			</c:if>

			<div>
				<label>
					<div class="label">广告标题：</div> <input placeholder="请填写${uiName}名称" size="40" name="name" type="text" value="${info.name}" />
					<span style="color:red;">*</span>
				</label>
				
			 	<label>
					<div class="label">创建日期：</div>  
					<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />" :show-time="true"></aj-form-calendar-input> 
				</label> 
			</div>
			
			<div>
				<label> 
					<div class="label">分类：</div>  
					<!-- 分类下拉 -->
					<aj-tree-catelog-select field-name="catalogId" :catalog-id="${domainCatalog_Id}" :selected-catalog-id="${empty info.catalogId ? 0 : info.catalogId}"></aj-tree-catelog-select>
					<span style="color:red;">*</span>
				</label>
				&nbsp; &nbsp; &nbsp; 
				<label>
					<div class="label">广告目标链接：</div> 
					<input type="text" placeholder="请填入${uiName}链接" size="60" required="required" name="link" value="${info.link}" v-model="adsLink" /> 
					<span style="color:red;">*</span>
					<a :href="adsLink" target="_blank">访问</a>
				</label>
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
			<div>
				<!--按钮 -->
				<aj-admin-info-btns :is-create="${isCreate}"></aj-admin-info-btns>
			</div>
		</form>
	</div>
	
	<script>
		App = new Vue({
			el : '.aj-form-row-holder',
			data : {
				adsLink : '${info.link}'
			}
		});

		// 表单提交
		aj.xhr.form('.aj-form-row-holder form', json => {
			if (json && json.msg)
				aj.alert(json.msg);
			${isCreate ? 'json && json.isOk && setTimeout(function(){location.assign(json.newlyId + "/");}, 2000);': ''}
		});
	</script>
	<c:if test="${isCreate}">
		<script>
			window.isCreate = true;
		</script>
	</c:if>
</body>
</html>